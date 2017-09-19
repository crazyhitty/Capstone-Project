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

package com.crazyhitty.chdev.ks.predator.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsContract;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsPresenter;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/26/2017 4:45 PM
 * Description: Unavailable
 */

public class PredatorPostsWidgetFactory implements RemoteViewsService.RemoteViewsFactory,
        PostsContract.View {
    private static final String TAG = "PredatorPostsWidgetFactory";

    private WeakReference<Context> mContextWeakReference;
    private int mAppWidgetId;

    private PostsContract.Presenter mPostsPresenter;

    private List<Post> mPosts;

    public PredatorPostsWidgetFactory(Context context, Intent intent) {
        mContextWeakReference = new WeakReference<Context>(context);
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Logger.d(TAG, "PredatorPostsWidgetFactory: constructor initialized");
    }

    @Override
    public void onCreate() {
        Logger.d(TAG, "onCreate: true");
        setPresenter(new PostsPresenter(this));
        mPostsPresenter.subscribe();
        mPostsPresenter.getOfflinePosts();
    }

    @Override
    public void onDataSetChanged() {
        Logger.d(TAG, "onDataSetChanged: true");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPostsPresenter.getOfflinePosts();
            }
        });
    }

    @Override
    public void onDestroy() {
        mPostsPresenter.unSubscribe();
    }

    @Override
    public int getCount() {
        Logger.d(TAG, "getCount: " + (mPosts != null ? mPosts.size() : 0));
        return mPosts != null ? mPosts.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= mPosts.size()) {
            return null;
        }
        RemoteViews rowRemoteViews = new RemoteViews(mContextWeakReference.get().getPackageName(),
                R.layout.item_widget_post);
        rowRemoteViews.setTextViewText(R.id.text_view_post_title, mPosts.get(position).getName());
        rowRemoteViews.setTextViewText(R.id.text_view_post_short_desc, mPosts.get(position).getTagline());

        Intent intent = new Intent();
        intent.putExtra(PostDetailsActivity.ARG_POST_TABLE_POST_ID, mPosts.get(position).getPostId());
        Bundle extras = new Bundle();
        extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        intent.putExtras(extras);

        rowRemoteViews.setOnClickFillInIntent(R.id.linear_layout_widget_post, intent);
        return rowRemoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void showPosts(List<Post> posts, HashMap<Integer, String> dateHashMap) {
        Logger.d(TAG, "showPosts: post size: " + posts.size());
        mPosts = posts;
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
        // No use.
    }

    @Override
    public void unableToShowNotification() {
        // No use.
    }

    @Override
    public void setPresenter(PostsContract.Presenter presenter) {
        mPostsPresenter = presenter;
    }
}
