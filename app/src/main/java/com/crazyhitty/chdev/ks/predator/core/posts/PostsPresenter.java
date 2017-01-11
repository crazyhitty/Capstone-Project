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
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import java.util.HashMap;

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

    private String mLastDate = DateUtils.getPredatorCurrentDate();

    private HashMap<Integer, String> mDateHashMap = new HashMap<>();

    private Cursor mCursor;

    private boolean mLoadMore = false;

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
        Observable<Cursor> postsDataObservable = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                null,
                                null,
                                null);
                if (cursor != null && cursor.getCount() != 0) {
                    dateMatcher(cursor);
                    subscriber.onNext(cursor);
                } else {
                    subscriber.onError(new NoPostsAvailableException());
                }
                subscriber.onCompleted();
            }
        });
        postsDataObservable.subscribeOn(Schedulers.newThread());
        postsDataObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postsDataObservable.subscribe(new Observer<Cursor>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage(), e);
                mView.unableToGetPosts(false, e.getMessage());
            }

            @Override
            public void onNext(Cursor cursor) {
                mCursor = cursor;
                Log.d(TAG, "cursorSize: " + mCursor.getCount());
                mView.showPosts(mCursor, mDateHashMap);
            }
        }));
    }

    @Override
    public void getPosts(final String token,
                         final String categoryName,
                         final boolean latest,
                         final boolean clearPrevious) {
        if (clearPrevious) {
            mLoadMore = false;
            mLastDate = DateUtils.getPredatorCurrentDate();
        }
        Observable<Cursor> postsDataObservable = ProductHuntRestApi.getApi()
                .getPostsCategoryWise(CoreUtils.getAuthToken(token), categoryName, mLastDate)
                .flatMap(new Func1<PostsData, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(final PostsData postsData) {
                        if (clearPrevious) {
                            getContentResolverInstance()
                                    .delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE,
                                            null,
                                            null);
                        }
                        return Observable.create(new Observable.OnSubscribe<Cursor>() {
                            @Override
                            public void call(Subscriber<? super Cursor> subscriber) {
                                if (postsData.getPosts() == null || postsData.getPosts().isEmpty()) {
                                    loadMorePosts(token, categoryName, latest);
                                    return;
                                }

                                for (PostsData.Posts post : postsData.getPosts()) {
                                    MainApplication.getContentResolverInstance()
                                            .insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD,
                                                    getContentValuesBasedOnPosts(post));
                                }
                                Cursor cursor = MainApplication.getContentResolverInstance()
                                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                                null,
                                                null,
                                                null,
                                                null);
                                if (cursor != null && cursor.getCount() != 0) {
                                    dateMatcher(cursor);
                                    subscriber.onNext(cursor);
                                } else {
                                    subscriber.onError(new NoPostsAvailableException());
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postsDataObservable.subscribe(new Observer<Cursor>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage(), e);
                mView.unableToGetPosts(mLoadMore, e.getMessage());
            }

            @Override
            public void onNext(Cursor cursor) {
                mCursor = cursor;
                Log.d(TAG, "cursorSize: " + mCursor.getCount());
                mView.showPosts(mCursor, mDateHashMap);
            }
        }));
    }

    @Override
    public void loadMorePosts(String token, String categoryName, boolean latest) {
        mLastDate = DateUtils.getPredatorPreviousDate(mLastDate);
        mLoadMore = true;
        getPosts(token, categoryName, latest, false);
    }

    /**
     * Matches all the post publish dates and create a hashmap for positions and dates wherever the
     * dates are changed in the cursor.
     *
     * @param cursor Cursor containing database values
     */
    private void dateMatcher(Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            // Match post date with current date
            String date = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY);

            String dateToBeShown = DateUtils.getPredatorPostsDate(date);

            if (!mDateHashMap.containsValue(dateToBeShown)) {
                mDateHashMap.put(i, dateToBeShown);
                mLastDate = date;
            }
        }
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

    public static class NoPostsAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No posts available.";
        }
    }
}
