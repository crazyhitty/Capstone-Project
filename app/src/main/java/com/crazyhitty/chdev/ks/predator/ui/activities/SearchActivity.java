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
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.search.SearchContract;
import com.crazyhitty.chdev.ks.predator.core.search.SearchPresenter;
import com.crazyhitty.chdev.ks.predator.events.NetworkEvent;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.SearchPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.SearchCollectionsFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.SearchPostsFragment;
import com.crazyhitty.chdev.ks.predator.ui.views.LoadingView;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/10/17 1:00 PM
 * Description: Unavailable
 */

public class SearchActivity extends BaseAppCompatActivity implements SearchContract.View,
        SearchPostsFragment.OnFragmentInteractionListener,
        SearchCollectionsFragment.OnFragmentInteractionListener {
    private static final String TAG = "SearchActivity";

    private static final String ARG_SEARCH_TYPE = "search_type";

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

    public static void startActivity(Context context, @SEARCH_TYPE int searchType) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_SEARCH_TYPE, searchType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();
        initViewPager();
        setPresenter(new SearchPresenter(this,
                ContextCompat.getColor(getApplicationContext(), R.color.color_accent)));
        mSearchPresenter.subscribe();
        initSearching();
        EventBus.getDefault().register(this);
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

        // Change tab selection based on user selection from the previous screen.
        switch (getIntent().getIntExtra(ARG_SEARCH_TYPE, SEARCH_TYPE.POSTS)) {
            case SEARCH_TYPE.POSTS:
                viewPagerSearch.setCurrentItem(0, false);
                break;
            case SEARCH_TYPE.COLLECTIONS:
                viewPagerSearch.setCurrentItem(1, false);
                break;
            default: viewPagerSearch.setCurrentItem(0, false);
        }
    }

    private void initSearching() {
        RxTextView.textChanges(editTextSearch)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (!isNetworkAvailable(true)) {
                            networkUnavailable();
                        } else if (TextUtils.isEmpty(charSequence) ||
                                TextUtils.isEmpty(charSequence.toString().trim())) {
                            editTextSearch.post(new Runnable() {
                                @Override
                                public void run() {
                                    noPostsAvailable(false);
                                    noCollectionsAvailable(false);
                                    mSearchPresenter.cancelOngoingRequest();
                                }
                            });
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
                return true;
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
    public void showPostResults(List<Post> posts, boolean loadMore) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).updatePosts(posts, loadMore);
                ((SearchPostsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void noPostsAvailable(boolean loadMore) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).noPostsAvailable(loadMore);
                ((SearchPostsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showCollectionResults(List<Collection> collections, boolean loadMore) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).updateCollections(collections, loadMore);
                ((SearchCollectionsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void noCollectionsAvailable(boolean loadMore) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).noCollectionsAvailable(loadMore);
                ((SearchCollectionsFragment) fragment).searchingStopped();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void networkUnavailable() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).onNetworkConnectivityChanged(false);
            } else if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).onNetworkConnectivityChanged(false);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadMorePosts() {
        if (!TextUtils.isEmpty(editTextSearch.getText().toString()) &&
                !mSearchPresenter.isLoadingMorePosts()) {
            mSearchPresenter.loadMorePosts(editTextSearch.getText().toString());
        }
    }

    @Override
    public void loadMoreCollections() {
        if (!TextUtils.isEmpty(editTextSearch.getText().toString()) &&
                !mSearchPresenter.isLoadingMorePosts()) {
            mSearchPresenter.loadMoreCollections(editTextSearch.getText().toString());
        }
    }

    @SuppressLint("RestrictedApi")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkConnectivityChanged(NetworkEvent networkEvent) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SearchPostsFragment) {
                ((SearchPostsFragment) fragment).onNetworkConnectivityChanged(networkEvent.isConnected());
            } else if (fragment instanceof SearchCollectionsFragment) {
                ((SearchCollectionsFragment) fragment).onNetworkConnectivityChanged(networkEvent.isConnected());
            }
        }
        if (networkEvent.isConnected() &&
                !TextUtils.isEmpty(editTextSearch.getText().toString())) {
                mSearchPresenter.search(editTextSearch.getText().toString());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SEARCH_TYPE.POSTS, SEARCH_TYPE.COLLECTIONS})
    public @interface SEARCH_TYPE {
        int POSTS = 0;
        int COLLECTIONS = 1;
    }
}
