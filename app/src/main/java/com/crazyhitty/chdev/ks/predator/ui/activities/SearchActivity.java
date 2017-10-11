/*
 * MIT License
 *
 * Copyright (c) 2016 Kartik Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.crazyhitty.chdev.ks.predator.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.search.SearchContract;
import com.crazyhitty.chdev.ks.predator.core.search.SearchPresenter;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.SearchPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.SearchCollectionsFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.SearchPostsFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/10/17 1:00 PM
 * Description: Unavailable
 */

public class SearchActivity extends BaseAppCompatActivity implements SearchContract.View {
    private static final String TAG = "SearchActivity";

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager_search)
    ViewPager viewPagerSearch;
    @BindView(R.id.tab_layout_search)
    TabLayout tabLayoutSearch;
    @BindView(R.id.edit_text_search)
    EditText editTextSearch;

    private SearchContract.Presenter mSearchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();
        initViewPager();
        setPresenter(new SearchPresenter(this));
        mSearchPresenter.subscribe();
        initSearching();
    }

    private void applyTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void initAppBarLayout() {
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                switch (state) {
                    case COLLAPSED:
                        showToolbarTitle(true);
                        break;
                    case EXPANDED:
                        hideToolbarTitle(true);
                        break;
                }
            }
        });
    }

    private void initToolbar() {
        attachToolbar(toolbar);

        setSupportActionBar(toolbar);
        hideToolbarTitle(false);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        SearchPagerAdapter searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(),
                new BaseSupportFragment[]{SearchPostsFragment.newInstance(), SearchCollectionsFragment.newInstance()},
                new String[]{getString(R.string.activity_search_posts), getString(R.string.activity_search_collections)});
        viewPagerSearch.setAdapter(searchPagerAdapter);

        tabLayoutSearch.setupWithViewPager(viewPagerSearch);
        changeTabTypeface(tabLayoutSearch);
    }

    private void initSearching() {
        RxTextView.textChanges(editTextSearch)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (!NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
                            networkUnavailable();
                        } else if (TextUtils.isEmpty(charSequence)) {
                            noPostsAvailable();
                            noCollectionsAvailable();
                            mSearchPresenter.cancelOngoingRequest();
                        } else {
                            mSearchPresenter.search(charSequence.toString());
                            searchingStarted();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        // Done.
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        changeMenuItemColorBasedOnTheme(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_search_info:
                showDialog(getString(R.string.activity_search_info_dialog_title),
                        getString(R.string.activity_search_info_dialog_message),
                        true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mSearchPresenter = presenter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showPostResults(List<Post> posts) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).updatePosts(posts);
                ((SearchPostsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void noPostsAvailable() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).noPostsAvailable();
                ((SearchPostsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showCollectionResults(List<Collection> collections) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).updateCollections(collections);
                ((SearchCollectionsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void noCollectionsAvailable() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).noCollectionsAvailable();
                ((SearchCollectionsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void networkUnavailable() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).networkUnavailable();
            } else if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).networkUnavailable();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void searchingStarted() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).searchingStarted();
            } else if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).searchingStarted();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.unSubscribe();
    }
}
