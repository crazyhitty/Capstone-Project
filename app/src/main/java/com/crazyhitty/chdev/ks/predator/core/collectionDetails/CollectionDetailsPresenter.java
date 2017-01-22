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

package com.crazyhitty.chdev.ks.predator.core.collectionDetails;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.core.collections.CollectionsPresenter;
import com.crazyhitty.chdev.ks.predator.core.posts.PostsPresenter;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CollectionDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/21/2017 1:53 PM
 * Description: Unavailable
 */

public class CollectionDetailsPresenter implements CollectionDetailsContract.Presenter {
    @NonNull
    private CollectionDetailsContract.View mView;

    private CompositeSubscription mCompositeSubscription;

    private Collection mCollection;

    public CollectionDetailsPresenter(@NonNull CollectionDetailsContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getCollectionDetails(final int collectionId) {
        Observable<Collection> collectionsCursorObservable = Observable.create(new Observable.OnSubscribe<Collection>() {
            @Override
            public void call(Subscriber<? super Collection> subscriber) {
                // Retrieve the results from the database.
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.CollectionsEntry.CONTENT_URI_COLLECTIONS,
                                null,
                                PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID + "=" + collectionId,
                                null,
                                null);
                if (cursor != null && cursor.getCount() != 0) {
                    subscriber.onNext(getCollectionFromCursor(cursor));
                    cursor.close();
                } else {
                    subscriber.onError(new CollectionsPresenter.CollectionsUnavailableException());
                }
                subscriber.onCompleted();
            }
        });

        collectionsCursorObservable.subscribeOn(Schedulers.io());
        collectionsCursorObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(collectionsCursorObservable.subscribe(new Observer<Collection>() {
            @Override
            public void onCompleted() {
                // Done.
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
            }

            @Override
            public void onNext(Collection collection) {
                mCollection = collection;
                mView.showCollectionDetails(mCollection);
            }
        }));
    }

    @Override
    public Collection getCurrentCollection() {
        return mCollection;
    }

    @Override
    public void getOfflinePosts(final int collectionId) {
        Observable<List<Post>> postsDataObservable = Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_COLLECTION_ID + "=" + collectionId + " AND " +
                                        PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION + "=1",
                                null,
                                PredatorContract.PostsEntry.COLUMN_VOTES_COUNT + " DESC");
                if (cursor != null && cursor.getCount() != 0) {
                    subscriber.onNext(getPostsFromCursor(cursor));
                    cursor.close();
                } else {
                    subscriber.onError(new PostsPresenter.NoPostsAvailableException());
                }
                subscriber.onCompleted();
            }
        });
        postsDataObservable.subscribeOn(Schedulers.io());
        postsDataObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postsDataObservable.subscribe(new Observer<List<Post>>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToGetPosts(true, e.getMessage());
            }

            @Override
            public void onNext(List<Post> posts) {
                mView.showPosts(posts);
            }
        }));
    }

    @Override
    public void getPosts(String token, final int collectionId) {
        Observable<List<Post>> postsDataObservable = ProductHuntRestApi.getApi()
                .getCollectionDetails(CoreUtils.getAuthToken(token), collectionId)
                .map(new Func1<CollectionDetailsData, List<Post>>() {
                    @Override
                    public List<Post> call(CollectionDetailsData collectionDetailsData) {
                        // Remove old posts for that collection id
                        MainApplication.getContentResolverInstance()
                                .delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE,
                                        PredatorContract.PostsEntry.COLUMN_COLLECTION_ID + "=" + collectionId + " AND " +
                                                PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION + "=1",
                                        null);

                        if (collectionDetailsData.getCollection().getPosts() == null || collectionDetailsData.getCollection().getPosts().isEmpty()) {
                            return null;
                        }

                        for (PostsData.Posts post : collectionDetailsData.getCollection().getPosts()) {
                            Logger.d(TAG, "post: " + post.getName());
                            MainApplication.getContentResolverInstance()
                                    .insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD,
                                            getContentValuesForPosts(collectionId, post));

                            // Add/update users.
                            MainApplication.getContentResolverInstance()
                                    .insert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                            getContentValuesForHunterUser(post.getId(), post.getUser()));
                            for (PostsData.Posts.Makers maker : post.getMakers()) {
                                MainApplication.getContentResolverInstance()
                                        .insert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getContentValuesForMakerUser(post.getId(), maker));
                            }
                        }
                        Cursor cursor = MainApplication.getContentResolverInstance()
                                .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                        null,
                                        PredatorContract.PostsEntry.COLUMN_COLLECTION_ID + "=" + collectionId + " AND " +
                                                PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION + "=1",
                                        null,
                                        PredatorContract.PostsEntry.COLUMN_VOTES_COUNT + " DESC");
                        List<Post> posts = null;
                        if (cursor != null && cursor.getCount() != 0) {
                            posts = getPostsFromCursor(cursor);
                            cursor.close();
                        }
                        return posts;
                    }
                })
                .flatMap(new Func1<List<Post>, Observable<List<Post>>>() {
                    @Override
                    public Observable<List<Post>> call(final List<Post> posts) {
                        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
                            @Override
                            public void call(Subscriber<? super List<Post>> subscriber) {
                                if (posts != null && posts.size() != 0) {
                                    subscriber.onNext(posts);
                                } else {
                                    subscriber.onError(new NoCollectionPostsAvailableException());
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postsDataObservable.subscribe(new Observer<List<Post>>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToGetPosts(false, e.getMessage());
            }

            @Override
            public void onNext(List<Post> posts) {
                mView.showPosts(posts);
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }

    private ContentValues getContentValuesForPosts(int collectionId, PostsData.Posts post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, post.getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_COLLECTION_ID, collectionId);
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, post.getCategoryId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, post.getDay());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, post.getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, post.getTagline());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, post.getCommentsCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, post.getCreatedAt());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(post.getCreatedAt()));
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, post.getDiscussionUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, post.getRedirectUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, post.getVotesCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, post.getThumbnail().getImageUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, post.getThumbnail().getOriginalImageUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, post.getScreenshotUrl().getValue300px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, post.getScreenshotUrl().getValue850px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, post.getUser().getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, post.getUser().getUsername());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, post.getUser().getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, post.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, post.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION, 1);
        return contentValues;
    }

    private ContentValues getContentValuesForHunterUser(int postId, PostsData.Posts.User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, user.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, user.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, user.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, user.getImageUrl().getValue100px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, user.getImageUrl().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, postId);
        return contentValues;
    }

    private ContentValues getContentValuesForMakerUser(int postId, PostsData.Posts.Makers maker) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, maker.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, maker.getCreatedAt());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, maker.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, maker.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, maker.getImageUrlMaker().getValue48px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, maker.getImageUrlMaker().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, postId);
        return contentValues;
    }

    private List<Post> getPostsFromCursor(Cursor cursor) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Post post = new Post();
            post.setId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_ID));
            post.setPostId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_POST_ID));
            post.setCollectionId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_COLLECTION_ID));
            post.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CATEGORY_ID));
            post.setDay(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY));
            post.setName(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME));
            post.setTagline(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE));
            post.setCommentCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT));
            post.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT));
            post.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS));
            post.setDiscussionUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL));
            post.setRedirectUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL));
            post.setVotesCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_VOTES_COUNT));
            post.setThumbnailImageUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL));
            post.setThumbnailImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL));
            post.setScreenshotUrl300px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX));
            post.setScreenshotUrl850px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX));
            post.setUsername(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_NAME));
            post.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_USERNAME));
            post.setUserId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_USER_ID));
            post.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX));
            post.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));
            post.setInCollection(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION));
            posts.add(post);
        }
        return posts;
    }

    private Collection getCollectionFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        Collection collection = new Collection();
        collection.setId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_ID));
        collection.setCollectionId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID));
        collection.setName(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_NAME));
        collection.setTitle(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_TITLE));
        collection.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_CREATED_AT));
        collection.setUpdatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT));
        collection.setFeaturedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT));
        collection.setSubscriberCount(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT));
        collection.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID));
        collection.setCollectionUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL));
        collection.setPostCounts(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS));
        collection.setBackgroundImageUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL));
        collection.setUsername(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_NAME));
        collection.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME));
        collection.setUserId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_ID));
        collection.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX));
        collection.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));

        return collection;
    }

    public static class NoCollectionPostsAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No posts available for this collection.";
        }
    }
}
