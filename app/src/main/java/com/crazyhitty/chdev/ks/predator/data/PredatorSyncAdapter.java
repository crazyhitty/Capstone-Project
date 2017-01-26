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

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.collections.CollectionsContract;
import com.crazyhitty.chdev.ks.predator.core.collections.CollectionsPresenter;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsContract;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsPresenter;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
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
    private CollectionsContract.Presenter mCollectionsPresenter;

    private PostsContract.View mPostsView = new PostsContract.View() {
        @Override
        public void showPosts(List<Post> posts, HashMap<Integer, String> dateHashMap) {
            Logger.d(TAG, "showPosts: postsSize: " + posts.size());
        }

        @Override
        public void unableToGetPosts(boolean onLoadMore, boolean wasLoadingOfflinePosts, String errorMessage) {
            Logger.d(TAG, "unableToGetPosts: error: " + errorMessage);
        }

        @Override
        public void setPresenter(PostsContract.Presenter presenter) {
            // Do nothing here.
        }
    };

    private CollectionsContract.View mCollectionView = new CollectionsContract.View() {
        @Override
        public void showCollections(List<Collection> collections) {
            Logger.d(TAG, "showCollections: collectionsSize: " + collections.size());
        }

        @Override
        public void unableToFetchCollections(boolean onLoadMore, boolean wasLoadingOfflinePosts, String errorMessage) {
            Logger.d(TAG, "unableToFetchCollections: error: " + errorMessage);
        }

        @Override
        public void setPresenter(CollectionsContract.Presenter presenter) {
            // Do nothing here.
        }
    };

    public PredatorSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);

        // Initialize the presenters.
        mPostsPresenter = new PostsPresenter(mPostsView);
        mCollectionsPresenter = new CollectionsPresenter(mCollectionView);

        mPostsPresenter.subscribe();
        mCollectionsPresenter.subscribe();
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    public static void initializePeriodicSync(Context context) {
        // Enable Sync
        ContentResolver.setIsSyncable(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Constants.Sync.ON);

        // Set periodic sync interval
        Logger.d(TAG, "initializePeriodicSync: interval(millis): " + PredatorSharedPreferences.getSyncIntervalInMillis(context));
        ContentResolver.addPeriodicSync(
                PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Bundle.EMPTY,
                PredatorSharedPreferences.getSyncIntervalInMillis(context));
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    public static void initializePeriodicSync(Context context, long syncIntervalInMillis) {
        // Enable Sync
        ContentResolver.setIsSyncable(PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Constants.Sync.ON);

        // Set periodic sync interval
        Logger.d(TAG, "initializePeriodicSync: interval(millis): " + syncIntervalInMillis);
        ContentResolver.addPeriodicSync(
                PredatorAccount.getAccount(context),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                Bundle.EMPTY,
                syncIntervalInMillis);
    }

    @SuppressWarnings("MissingPermission")
    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
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
            mPostsPresenter.getPosts(authToken, Constants.Posts.CATEGORY_TECH, true, true);

            // Fetch featured collections.
            mCollectionsPresenter.getLatestCollections(authToken, true);
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

        if (mCollectionsPresenter != null) {
            mCollectionsPresenter.unSubscribe();
        }
    }
}
