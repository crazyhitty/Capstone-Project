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

package com.crazyhitty.chdev.ks.predator.core.posts;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.crazyhitty.chdev.ks.predator.MainApplication.getContentResolverInstance;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/2/2017 10:13 PM
 * Description: Unavailable
 */

public class PostsPresenter implements PostsContract.Presenter {
    private static final String TAG = "PostsPresenter";

    private static final int PER_PAGE_VALUE = 20;

    private int mPage = 1;

    private Cursor mCursor;

    @NonNull
    private PostsContract.View mView;

    private CompositeSubscription mCompositeSubscription;

    public PostsPresenter(@NonNull PostsContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void getOfflinePosts(boolean latest) {

    }

    @Override
    public void getPosts(boolean latest) {
        Observable<Boolean> postsDataObservable = ProductHuntRestApi.getApi()
                .getPosts(mPage, PER_PAGE_VALUE)
                .flatMapIterable(new Func1<PostsData, Iterable<PostsData.Posts>>() {
                    @Override
                    public Iterable<PostsData.Posts> call(PostsData postsData) {
                        // Clear the posts from db, only those which are not bookmarked.
                        getContentResolverInstance()
                                .delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE,
                                        null,
                                        null);
                        return postsData.getPosts();
                    }
                })
                .flatMap(new Func1<PostsData.Posts, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final PostsData.Posts post) {
                        return Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                Log.d(TAG, "call: woah");
                                // Save posts in db

                                MainApplication.getContentResolverInstance()
                                        .insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD,
                                                getContentValuesBasedOnPosts(post));

                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postsDataObservable.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                // Done
                mCursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                null,
                                null,
                                null);
                Log.d(TAG, "cursorSize: " + mCursor.getCount());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage(), e);
                mView.unableToGetPosts(e.getMessage());
            }

            @Override
            public void onNext(Boolean bool) {

            }
        }));
    }

    @Override
    public void loadMorePosts() {

    }

    private ContentValues getContentValuesBasedOnPosts(PostsData.Posts post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, post.getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, post.getCategoryId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, post.getDay());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, post.getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, post.getTagline());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, post.getCommentsCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, post.getCreatedAt());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, post.getDiscussionUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, post.getRedirectUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, post.getVotesCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, post.getThumbnail().getImageUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, post.getScreenshotUrl().getValue300px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, post.getScreenshotUrl().getValue850px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, post.getUser().getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, post.getUser().getUsername());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, post.getUser().getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_48PX, post.getUser().getImageUrl().getValue48px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, post.getUser().getImageUrl().getOriginal());
        return contentValues;
    }
}
