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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.postDetails.PostDetailsContract;
import com.crazyhitty.chdev.ks.predator.core.postDetails.PostDetailsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.CommentsEvent;
import com.crazyhitty.chdev.ks.predator.events.UsersEvent;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.PostDetailsPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.MediaRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.CommentsFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.UsersFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.MediaItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.predator.utils.StartSnapHelper;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.crazyhitty.chdev.ks.predator.data.Constants.Media.YOUTUBE_PATH;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 1:30 PM
 * Description: Unavailable
 */

public class PostDetailsActivity extends BaseAppCompatActivity implements MediaRecyclerAdapter.OnMediaItemClickListener,
        PostDetailsContract.View {
    public static final String ARG_POST_TABLE_POST_ID = "post_id";
    private static final String TAG = "PostDetailsActivity";
    private static final int DELAY_MS = 600;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_media)
    RecyclerView recyclerViewMedia;
    @BindView(R.id.text_view_post_title)
    TextView txtPostTitle;
    @BindView(R.id.text_view_post_short_desc)
    TextView txtPostShortDesc;
    @BindView(R.id.text_view_extra_details)
    TextView txtPostExtraDetails;
    @BindView(R.id.image_view_post)
    SimpleDraweeView imgViewPost;
    @BindView(R.id.relative_layout_post_details_toolbar)
    RelativeLayout relativeLayoutPostDetailsToolbar;
    @BindView(R.id.view_pager_post_details)
    ViewPager viewPagerPostDetails;
    @BindView(R.id.tab_layout_post_details)
    TabLayout tabLayoutPostDetails;
    /*@BindView(R.id.fab_bookmark)
    FloatingActionButton fabBookmark;*/

    private MediaRecyclerAdapter mMediaRecyclerAdapter;
    private boolean mFirstTimeLoading = true;
    private boolean mRefreshing = false;

    private PostDetailsContract.Presenter mPostDetailsPresenter;

    private View mMenuItemRefreshActionView = null;

    public static void startActivity(Context context, int postId) {
        Intent intent = new Intent(context, PostDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_POST_TABLE_POST_ID, postId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();
        setMediaRecyclerViewProperties();
        initViewPager();

        setPresenter(new PostDetailsPresenter(this));
        mPostDetailsPresenter.subscribe();

        // Get post details.
        mPostDetailsPresenter.getDetails(getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID));

        // Get users.
        mPostDetailsPresenter.getUsers(getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID));

        // Always load offline posts details first.
        getPostDetails(getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID), true);
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

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove jitter when this ViewGroup expands.
        // This will force the parent layout to not animate.
        relativeLayoutPostDetailsToolbar.getLayoutTransition().setAnimateParentHierarchy(false);
    }

    private void initViewPager() {
        PostDetailsPagerAdapter postDetailsPagerAdapter = new PostDetailsPagerAdapter(getSupportFragmentManager(),
                new BaseSupportFragment[]{CommentsFragment.newInstance(), UsersFragment.newInstance()},
                new String[]{getString(R.string.post_details_comments), getString(R.string.post_details_users)});
        viewPagerPostDetails.setAdapter(postDetailsPagerAdapter);

        tabLayoutPostDetails.setupWithViewPager(viewPagerPostDetails);
        changeTabTypeface(tabLayoutPostDetails);
    }

    private void setMediaRecyclerViewProperties() {
        // Set up the recycler view properties.
        // Create a horizontal layout manager for recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewMedia.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        recyclerViewMedia.addItemDecoration(new MediaItemDecorator(getApplicationContext(), 16));

        // Create adapter that will power this recycler view.
        mMediaRecyclerAdapter = new MediaRecyclerAdapter(null, this);
        recyclerViewMedia.setAdapter(mMediaRecyclerAdapter);

        // Add snaphelper that will snap the recycler view items at start.
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewMedia);
    }

    /**
     * Get extra details like media, comments, etc.
     *
     * @param postId      Id of that particular post.
     * @param loadOffline True, if you want to load details from database, otherwise false.
     */
    private void getPostDetails(final int postId, final boolean loadOffline) {
        PredatorAccount.getAuthToken(PostDetailsActivity.this,
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
                        if (loadOffline) {
                            mPostDetailsPresenter.getExtraDetailsOffline(postId);
                        } else {
                            mPostDetailsPresenter.getExtraDetails(s, postId);
                        }
                    }
                });
    }

    @Override
    public void showDetails(PostDetails postDetails) {
        txtPostTitle.setText(postDetails.getTitle());
        txtPostShortDesc.setText(postDetails.getDescription());

        String date = DateUtils.getPredatorPostCompleteDate(postDetails.getDate());

        if (TextUtils.isEmpty(date)) {
            date = postDetails.getDay();
        }

        String extraDetails = String.format("%s \u2022 %s \u2022 %d %s",
                date,
                postDetails.getCategory(),
                postDetails.getVoteCount(),
                getString(R.string.post_details_votes));

        txtPostExtraDetails.setText(extraDetails);

        String postImageUrl = ImageUtils.getCustomPostThumbnailImageUrl(postDetails.getBackdropUrl(),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44.0f),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44.0f));
        imgViewPost.setImageURI(postImageUrl);

        // Set toolbar title.
        getSupportActionBar().setTitle(postDetails.getTitle());
    }

    @Override
    public void showUsers(final List<User> users) {
        Logger.d(TAG, "showUsers: usersCount: " + users.size());

        // Show users after a delay.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsers(users);
            }
        }, DELAY_MS);
    }

    @Override
    public void showMedia(List<Media> media) {
        Logger.d(TAG, "showMedia: mediaCount: " + media.size());

        mMediaRecyclerAdapter.updateMedia(media);
        recyclerViewMedia.post(new Runnable() {
            @Override
            public void run() {
                mFirstTimeLoading = false;
                recyclerViewMedia.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void attachInstallLinks(List<InstallLink> installLinks) {
        Logger.d(TAG, "attachInstallLinks: installLinksCount: " + installLinks.size());
    }

    @Override
    public void showAllUsers(final List<User> users) {
        Logger.d(TAG, "showAllUsers: usersCount: " + users.size());

        // Show users after a delay.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFirstTimeLoading = false;
                updateUsers(users);
            }
        }, DELAY_MS);
    }

    @Override
    public void showComments(final List<Comment> comments) {
        Logger.d(TAG, "showComments: commentsCount: " + comments.size());

        mFirstTimeLoading = false;
        // Show comments after a delay.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFirstTimeLoading = false;
                updateComments(comments);
            }
        }, DELAY_MS);
    }

    @Override
    public void unableToFetchPostDetails(String errorMessage) {
        Logger.e(TAG, "unableToFetchPostDetails: " + errorMessage);
    }

    @Override
    public void unableToFetchUsers(String errorMessage) {
        Logger.e(TAG, "unableToFetchUsers: " + errorMessage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsers(null);
            }
        }, DELAY_MS);
    }

    @Override
    public void unableToFetchMedia(String errorMessage) {
        Logger.e(TAG, "unableToFetchMedia: " + errorMessage);
    }

    @Override
    public void unableToFetchInstallLinks(String errorMessage) {
        Logger.e(TAG, "unableToFetchInstallLinks: " + errorMessage);
    }

    @Override
    public void unableToFetchAllUsers(String errorMessage) {
        Logger.e(TAG, "unableToFetchAllUsers: " + errorMessage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUsers(null);
            }
        }, DELAY_MS);
    }

    @Override
    public void unableToFetchComments(String errorMessage) {
        Logger.e(TAG, "unableToFetchComments: " + errorMessage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateComments(null);
            }
        }, DELAY_MS);
    }

    @Override
    public void noOfflineDataAvailable() {
        Logger.e(TAG, "noOfflineDataAvailable");
        if (isNetworkAvailable(true)) {
            getPostDetails(getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID), false);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateComments(null);
                    updateUsers(null);
                }
            }, DELAY_MS);
        }
    }

    @Override
    public void dismissLoading() {
        Logger.d(TAG, "dismissLoading: Stop refreshing");
        // Only show toast once as this method will be called multiple times.
        if (mRefreshing) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showShortToast(R.string.post_details_updated_details);
                }
            }, DELAY_MS);
        }
        mRefreshing = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onMediaItemClick(int position, Media media) {
        openMedia(media);
    }

    /*@OnClick(R.id.fab_bookmark)
    public void onBookmark() {
        fabBookmark.setSelected(!fabBookmark.isSelected());
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_refresh).setEnabled(!mRefreshing && !mFirstTimeLoading);
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
        getMenuInflater().inflate(R.menu.menu_post_details, menu);
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
                    getPostDetails(getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID), false);
                }
                break;
            case R.id.menu_open_redirect_url:
                mPostDetailsPresenter.openRedirectUrl(this);
                break;
            case R.id.menu_share:
                sharePostDetails();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(PostDetailsContract.Presenter presenter) {
        mPostDetailsPresenter = presenter;
    }

    public void sharePostDetails() {
        if (mPostDetailsPresenter.getPostDetails() == null ||
                TextUtils.isEmpty(mPostDetailsPresenter.getPostDetails().getTitle()) ||
                TextUtils.isEmpty(mPostDetailsPresenter.getPostDetails().getTagline())) {
            Toast.makeText(getApplicationContext(), R.string.post_details_no_redirect_url_available, Toast.LENGTH_SHORT).show();
            return;
        }

        String title = mPostDetailsPresenter.getPostDetails().getTitle();
        String body = mPostDetailsPresenter.getPostDetails().getTagline() + "\n" +
                mPostDetailsPresenter.getPostDetails().getDiscussionUrl();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }

    public void openMedia(Media media) {
        switch (media.getMediaType()) {
            case Constants.Media.IMAGE:
                MediaFullScreenActivity.startActivity(getApplicationContext(),
                        media.getMediaId(),
                        media.getPostId());
                break;
            case Constants.Media.VIDEO:
                Intent videoIntent = new Intent();
                videoIntent.setAction(Intent.ACTION_VIEW);
                videoIntent.setData(Uri.parse(YOUTUBE_PATH.concat(media.getVideoId())));
                startActivity(videoIntent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPostDetailsPresenter.unSubscribe();
    }

    private void updateComments(final List<Comment> comments) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment instanceof CommentsFragment) {
                ((CommentsFragment) fragment).updateComments(new CommentsEvent(comments));
            }
        }
    }

    private void updateUsers(final List<User> users) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment instanceof UsersFragment) {
                ((UsersFragment) fragment).updateUsers(new UsersEvent(users));
            }
        }
    }
}