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

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
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
import com.crazyhitty.chdev.ks.predator.core.userProfile.UserProfileContract;
import com.crazyhitty.chdev.ks.predator.core.userProfile.UserProfilePresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.UserFollowersFollowingEvent;
import com.crazyhitty.chdev.ks.predator.events.UserPostsEvent;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.User;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/16/2017 3:45 PM
 * Description: Unavailable
 */

public class UserProfileActivity extends BaseAppCompatActivity implements UserProfileContract.View {
    private static final String TAG = "UserProfileActivity";
    private static final String ARG_USERS_TABLE_USER_ID = "user_id";
    private static final int DELAY_MS = 600;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_view_user)
    SimpleDraweeView imgViewUser;
    @BindView(R.id.text_view_name)
    TextView txtName;
    @BindView(R.id.text_view_headline)
    TextView txtHeadline;
    @BindView(R.id.text_view_extra_details)
    TextView txtExtraDetails;
    @BindView(R.id.view_pager_user_profile)
    ViewPager viewPagerUserProfile;
    @BindView(R.id.tab_layout_user_profile)
    TabLayout tabLayoutUserProfile;

    private UserProfileContract.Presenter mUserProfilePresenter;

    private boolean mRefreshMenuEnabled = false;
    private boolean mRefreshing = false;

    private View mMenuItemRefreshActionView = null;

    // Viewpager fragments.
    private UserProfilePostsFragment mUserProfilePostsFragmentUpvotes;
    private UserProfilePostsFragment mUserProfilePostsFragmentSubmitted;
    private UserProfilePostsFragment mUserProfilePostsFragmentMade;
    private UserProfileUsersFragment mUserProfileUsersFragmentFollowers;
    private UserProfileUsersFragment mUserProfileUsersFragmentFollowing;

    public static void startActivity(Context context, int userId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_USERS_TABLE_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();
        initViewPager();
        setPresenter(new UserProfilePresenter(this));
        mUserProfilePresenter.subscribe();
        mUserProfilePresenter.getOfflineData(getIntent().getExtras().getInt(ARG_USERS_TABLE_USER_ID));
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
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
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

        toolbar.setTitle(R.string.activity_user_profile);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        mUserProfilePostsFragmentUpvotes = UserProfilePostsFragment.newInstance(UserProfilePostsFragment.POSTS_TYPE_UPVOTES);
        mUserProfilePostsFragmentSubmitted = UserProfilePostsFragment.newInstance(UserProfilePostsFragment.POSTS_TYPE_SUBMITTED);
        mUserProfilePostsFragmentMade = UserProfilePostsFragment.newInstance(UserProfilePostsFragment.POSTS_TYPE_MADE);
        mUserProfileUsersFragmentFollowers = UserProfileUsersFragment.newInstance(UserProfileUsersFragment.USERS_TYPE_FOLLOWERS);
        mUserProfileUsersFragmentFollowing = UserProfileUsersFragment.newInstance(UserProfileUsersFragment.USERS_TYPE_FOLLOWING);

        BaseSupportFragment[] baseSupportFragments = new BaseSupportFragment[]{
                mUserProfilePostsFragmentUpvotes,
                mUserProfilePostsFragmentSubmitted,
                mUserProfilePostsFragmentMade,
                mUserProfileUsersFragmentFollowers,
                mUserProfileUsersFragmentFollowing
        };

        String[] titles = getResources().getStringArray(R.array.activity_user_profile_titles);

        UserProfilePagerAdapter userProfilePagerAdapter = new UserProfilePagerAdapter(getSupportFragmentManager(),
                baseSupportFragments,
                titles);
        viewPagerUserProfile.setAdapter(userProfilePagerAdapter);
        viewPagerUserProfile.setOffscreenPageLimit(4);

        tabLayoutUserProfile.setupWithViewPager(viewPagerUserProfile);
    }

    private void loadLatestDetails(final int userId, final boolean refresh) {
        PredatorAccount.getAuthToken(UserProfileActivity.this,
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.d(TAG, "onNext: token fetched ; Thread: " + Thread.currentThread().getName());
                        mUserProfilePresenter.getLatestData(s, userId, refresh);
                    }
                });
    }

    @Override
    public void offlineLoadingStart() {
        mRefreshMenuEnabled = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onlineLoadingStart() {
        mRefreshing = true;
        invalidateOptionsMenu();
    }

    @Override
    public void showUserDetails(User currentUser) {
        Logger.d(TAG, "showUserDetails: " + currentUser.toString());

        toolbar.setTitle(currentUser.getName());

        String userImageUrl = ImageUtils.getCustomUserThumbnailUrl(currentUser.getImage(),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44));
        imgViewUser.setImageURI(userImageUrl);

        String name = currentUser.getName();
        String headline = currentUser.getHeadline();

        String extraDetails = String.format("@%s", currentUser.getUsername());

        if (!TextUtils.isEmpty(currentUser.getWebsiteUrl())) {
            extraDetails = String.format("@%s \u2022 %s",
                    currentUser.getUsername(),
                    currentUser.getWebsiteUrl());
        }

        txtName.setText(name);
        txtHeadline.setText(headline);
        txtExtraDetails.setText(extraDetails);

        txtHeadline.setVisibility(TextUtils.isEmpty(headline) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void unableToFetchUserDetails() {
        Logger.d(TAG, "unableToFetchUserDetails");
    }

    @Override
    public void showPosts(final UserProfileContract.POST_TYPE postType, final List<Post> posts, final boolean refresh) {
        Logger.d(TAG, "showPosts: type: " + postType + " ; size: " + posts.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePosts(postType, posts, refresh);

            }
        }, DELAY_MS);
    }

    @Override
    public void showUsers(final UserProfileContract.USER_TYPE userType, final List<User> users, final boolean refresh) {
        Logger.d(TAG, "showUsers: type: " + userType + " ; size: " + users.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsers(userType, users, refresh);
            }
        }, DELAY_MS);
    }

    @Override
    public void unableToFetchPosts(final UserProfileContract.POST_TYPE postType) {
        Logger.d(TAG, "unableToFetchPosts: type: " + postType);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePosts(postType, null, false);
            }
        }, DELAY_MS);
    }

    @Override
    public void unableToFetchUsers(final UserProfileContract.USER_TYPE userType) {
        Logger.d(TAG, "unableToFetchUsers: type: " + userType);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsers(userType, null, false);
            }
        }, DELAY_MS);
    }

    @Override
    public void offlineLoadingComplete() {
        mRefreshMenuEnabled = false;
        if (isNetworkAvailable(true)) {
            loadLatestDetails(getIntent().getExtras().getInt(ARG_USERS_TABLE_USER_ID), false);
            invalidateOptionsMenu();
        } else {
            mRefreshMenuEnabled = true;
            invalidateOptionsMenu();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updatePosts(UserProfileContract.POST_TYPE.UPVOTES, null, false);
                    updatePosts(UserProfileContract.POST_TYPE.SUBMITTED, null, false);
                    updatePosts(UserProfileContract.POST_TYPE.MADE, null, false);
                    updateUsers(UserProfileContract.USER_TYPE.FOLLOWERS, null, false);
                    updateUsers(UserProfileContract.USER_TYPE.FOLLOWING, null, false);
                }
            }, DELAY_MS);
        }
    }

    @Override
    public void onlineLoadingComplete() {
        mRefreshMenuEnabled = true;
        mRefreshing = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        }, DELAY_MS);
    }

    @Override
    public void onRefreshComplete() {
        Logger.d(TAG, "onRefreshComplete: done");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showShortToast(R.string.activity_user_profile_updated_details);
            }
        }, DELAY_MS);
    }


    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        mUserProfilePresenter = presenter;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_refresh).setEnabled(!mRefreshing && mRefreshMenuEnabled);
        if (mRefreshing) {
            mMenuItemRefreshActionView = new ProgressBar(this);

            // Make progress bar look at the same place as the original menu item was.
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ScreenUtils.dpToPxInt(getApplicationContext(), 48.0f),
                    ScreenUtils.dpToPxInt(getApplicationContext(), 48.0f));
            mMenuItemRefreshActionView.setLayoutParams(layoutParams);
            mMenuItemRefreshActionView.setPadding(ScreenUtils.dpToPxInt(getApplicationContext(), 12.0f),
                    ScreenUtils.dpToPxInt(getApplicationContext(), 12.0f),
                    ScreenUtils.dpToPxInt(getApplicationContext(), 12.0f),
                    ScreenUtils.dpToPxInt(getApplicationContext(), 12.0f));
        } else {
            mMenuItemRefreshActionView = null;
        }
        menu.findItem(R.id.menu_refresh).setActionView(mMenuItemRefreshActionView);

        return super.onPrepareOptionsMenu(menu);
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
                if (isNetworkAvailable(true)) {
                    showShortToast(R.string.post_details_refreshing);
                    mRefreshing = true;
                    invalidateOptionsMenu();
                    loadLatestDetails(getIntent().getExtras().getInt(ARG_USERS_TABLE_USER_ID), true);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserProfilePresenter.unSubscribe();
    }

    private void updatePosts(UserProfileContract.POST_TYPE postType, List<Post> posts, boolean refresh) {
        switch (postType) {
            case UPVOTES:
                mUserProfilePostsFragmentUpvotes.updateUserPosts(new UserPostsEvent(postType, posts, refresh));
                break;
            case SUBMITTED:
                mUserProfilePostsFragmentSubmitted.updateUserPosts(new UserPostsEvent(postType, posts, refresh));
                break;
            case MADE:
                mUserProfilePostsFragmentMade.updateUserPosts(new UserPostsEvent(postType, posts, refresh));
                break;
        }
    }

    private void updateUsers(UserProfileContract.USER_TYPE userType, List<User> users, boolean refresh) {
        switch (userType) {
            case FOLLOWERS:
                mUserProfileUsersFragmentFollowers.updateUsers(new UserFollowersFollowingEvent(userType, users, refresh));
                break;
            case FOLLOWING:
                mUserProfileUsersFragmentFollowing.updateUsers(new UserFollowersFollowingEvent(userType, users, refresh));
                break;
        }
    }
}
