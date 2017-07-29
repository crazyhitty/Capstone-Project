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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.fontSelection.FontSelectionContract;
import com.crazyhitty.chdev.ks.predator.core.fontSelection.FontSelectionPresenter;
import com.crazyhitty.chdev.ks.predator.core.userProfile.UserProfileContract;
import com.crazyhitty.chdev.ks.predator.core.userProfile.UserProfilePresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.UserFollowersFollowingEvent;
import com.crazyhitty.chdev.ks.predator.events.UserPostsEvent;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.models.UserFallback;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.FontPreviewPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.UserProfilePagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.UserProfilePostsFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.UserProfileUsersFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     07/23/2017 6:55 PM
 * Description: Unavailable
 */

public class FontSelectionActivity extends BaseAppCompatActivity implements FontSelectionContract.View{
    private static final String TAG = "FontSelectionActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager_font_previews)
    ViewPager viewPagerFontPreviews;

    private FontSelectionContract.Presenter mFontSelectionPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FontSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_font_selection);
        ButterKnife.bind(this);
        initToolbar();
        setPresenter(new FontSelectionPresenter(this));
        mFontSelectionPresenter.getDummyPosts(getApplicationContext());
    }

    private void applyTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void initToolbar() {
        attachToolbar(toolbar);

        setSupportActionBar(toolbar);
        hideToolbarTitle(false);

        toolbar.setTitle(R.string.activity_font_selection);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        // Change menu item icons based on theme
        changeMenuItemColorBasedOnTheme(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_refresh:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(FontSelectionContract.Presenter presenter) {
        mFontSelectionPresenter = presenter;
    }

    @Override
    public void showDummyPosts(List<Post> dummyPosts) {
        FontPreviewPagerAdapter fontPreviewPagerAdapter = new FontPreviewPagerAdapter(getResources().getStringArray(R.array.settings_change_font_entries),
                dummyPosts);
        viewPagerFontPreviews.setAdapter(fontPreviewPagerAdapter);
    }

    @Override
    public void dummyPostsUnavailable() {

    }
}
