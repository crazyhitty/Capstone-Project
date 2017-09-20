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

package com.crazyhitty.chdev.ks.predator.data;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Bundle;

import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsContract;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsPresenter;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.notifications.PostNotification;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/24/2017 11:59 AM
 * Description: Unavailable
 */

public class PredatorSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "PredatorSyncAdapter";

    private final AccountManager mAccountManager;

    private PostsContract.Presenter mPostsPresenter;

    private PostsContract.View mPostsView = new PostsContract.View() {
        @Override
        public void showPosts(List<Post> posts, HashMap<Integer, String> dateHashMap) {
            Logger.d(TAG, "showPosts: posts synced with size: " + (posts != null ? posts.size() : 0));
            // Update widgets.
            mPostsPresenter.updateWidgets(getContext());
            // Show notifications, if enabled.
            if (PredatorSharedPreferences.areNotificationsEnabled(getContext())) {
                mPostsPresenter.getNotification();
            }
        }

        @Override
        public void unableToGetPosts(boolean onLoadMore, boolean wasLoadingOfflinePosts, String errorMessage) {
            Logger.e(TAG, "unableToGetPosts: error: " + errorMessage);
        }

        @Override
        public void postsCleared() {
            Logger.d(TAG, "postsCleared: called");
        }

        @Override
        public void unableToClearPosts(String message) {
            Logger.e(TAG, "unableToClearPosts: " + message);
        }

        @Override
        public void showNotification(Post post) {
            Logger.d(TAG, "showNotification: called");
            mPostsPresenter.notificationShownForPost(post.getPostId());
            new PostNotification(getContext()).show(post);
        }

        @Override
        public void unableToShowNotification() {
            Logger.e(TAG, "unableToShowNotification: called");
        }

        @Override
        public void setPresenter(PostsContract.Presenter presenter) {
            // Do nothing here.
        }
    };

    public PredatorSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);

        // Initialize the presenters.
        mPostsPresenter = new PostsPresenter(mPostsView);

        mPostsPresenter.subscribe();
    }

    public static void initializePeriodicSync(Context context) {
        // Enable Sync
        ContentResolver.setIsSyncable(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Constants.Sync.ON);

        ContentResolver.setSyncAutomatically(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                true);

        // Set periodic sync interval
        Logger.d(TAG, "initializePeriodicSync: interval(seconds): " + PredatorSharedPreferences.getSyncIntervalInSeconds(context));
        ContentResolver.addPeriodicSync(
                PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Bundle.EMPTY,
                PredatorSharedPreferences.getSyncIntervalInSeconds(context));
    }

    public static void initializePeriodicSync(Context context, long syncIntervalInSeconds) {
        // Enable Sync
        ContentResolver.setIsSyncable(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Constants.Sync.ON);

        // Set periodic sync interval
        Logger.d(TAG, "initializePeriodicSync: interval(seconds): " + syncIntervalInSeconds);
        ContentResolver.addPeriodicSync(
                PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Bundle.EMPTY,
                syncIntervalInSeconds);
    }

    public static void removePeriodicSync(Context context) {
        // Disable Sync
        ContentResolver.setIsSyncable(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Constants.Sync.OFF);

        Logger.d(TAG, "removePeriodicSync: true");
        ContentResolver.removePeriodicSync(
                PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Bundle.EMPTY);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Logger.d(TAG, "onPerformSync: account: " + account.name);
        try {
            // Check if internet is available. If not then don't perform any sync.
            if (!NetworkConnectionUtil.isNetworkAvailable(getContext())) {
                return;
            }

            // Get the auth token for the current account.
            String authToken = mAccountManager.blockingGetAuthToken(account,
                    PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()),
                    true);

            // Fetch latest posts.
            mPostsPresenter.getPosts(authToken, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();

        if (mPostsPresenter != null) {
            mPostsPresenter.unSubscribe();
        }
    }
}
