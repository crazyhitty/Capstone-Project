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

package com.crazyhitty.chdev.ks.predator.core.collections;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CollectionsData;
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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/20/2017 9:25 AM
 * Description: Unavailable
 */

public class CollectionsPresenter implements CollectionsContract.Presenter {
    private static final String TAG = "CollectionsPresenter";
    private static final int PER_PAGE_COUNT = 50;
    private static int sPage = 1;
    @NonNull
    private CollectionsContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public CollectionsPresenter(@NonNull CollectionsContract.View view) {
        mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getOfflineCollections() {
        Observable<List<Collection>> collectionsCursorObservable = Observable.create(new Observable.OnSubscribe<List<Collection>>() {
            @Override
            public void call(Subscriber<? super List<Collection>> subscriber) {
                // Retrieve the results from the database.
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.CollectionsEntry.CONTENT_URI_COLLECTIONS,
                                null,
                                null,
                                null,
                                null);
                if (cursor != null && cursor.getCount() != 0) {
                    subscriber.onNext(getCollectionsFromCursor(cursor));
                    cursor.close();
                } else {
                    subscriber.onError(new CollectionsUnavailableException());
                }
                subscriber.onCompleted();
            }
        });

        collectionsCursorObservable.subscribeOn(Schedulers.io());
        collectionsCursorObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(collectionsCursorObservable.subscribe(new Observer<List<Collection>>() {
            @Override
            public void onCompleted() {
                // Done.
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToFetchCollections(false, true, e.getMessage());
            }

            @Override
            public void onNext(List<Collection> collections) {
                mView.showCollections(collections);
            }
        }));
    }

    @Override
    public void getLatestCollections(final String token, final boolean clearPrevious) {
        Observable<List<Collection>> collectionsCursorObservable = ProductHuntRestApi.getApi()
                .getCollections(CoreUtils.getAuthToken(token), sPage, PER_PAGE_COUNT, true)
                .map(new Func1<CollectionsData, List<Collection>>() {
                    @Override
                    public List<Collection> call(CollectionsData collectionsData) {
                        if (clearPrevious) {
                            // Clear previous collections from database.
                            MainApplication.getContentResolverInstance()
                                    .delete(PredatorContract.CollectionsEntry.CONTENT_URI_COLLECTIONS_DELETE,
                                            null,
                                            null);

                            // Clear posts currently available for collections from database.
                            MainApplication.getContentResolverInstance()
                                    .delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE,
                                            PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION + "=1",
                                            null);
                        }

                        // Add content to the database.
                        MainApplication.getContentResolverInstance()
                                .bulkInsert(PredatorContract.CollectionsEntry.CONTENT_URI_COLLECTIONS_ADD,
                                        getBulkContentValuesForCollections(collectionsData.getCollections()));

                        // Retrieve the results from the database.
                        Cursor cursor = MainApplication.getContentResolverInstance()
                                .query(PredatorContract.CollectionsEntry.CONTENT_URI_COLLECTIONS,
                                        null,
                                        null,
                                        null,
                                        null);

                        List<Collection> collections = null;
                        if (cursor != null && cursor.getCount() != 0) {
                            collections = getCollectionsFromCursor(cursor);
                            cursor.close();
                        }
                        return collections;
                    }
                })
                .flatMap(new Func1<List<Collection>, Observable<List<Collection>>>() {
                    @Override
                    public Observable<List<Collection>> call(final List<Collection> collections) {
                        return Observable.create(new Observable.OnSubscribe<List<Collection>>() {
                            @Override
                            public void call(Subscriber<? super List<Collection>> subscriber) {
                                if (collections != null && collections.size() != 0) {
                                    subscriber.onNext(collections);
                                } else {
                                    subscriber.onError(new CollectionsUnavailableException());
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(collectionsCursorObservable.subscribe(new Observer<List<Collection>>() {
            @Override
            public void onCompleted() {
                // Done.
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToFetchCollections(false, false, e.getMessage());
            }

            @Override
            public void onNext(List<Collection> collections) {
                mView.showCollections(collections);
            }
        }));
    }

    @Override
    public void loadMoreCollections(String token) {
        sPage++;
        Logger.d(TAG, "loadMoreCollections: page: " + sPage);
        getLatestCollections(token, false);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }

    private ContentValues[] getBulkContentValuesForCollections(List<CollectionsData.Collections> collections) {
        ContentValues[] contentValuesArr = new ContentValues[collections.size()];

        for (int i = 0; i < collections.size(); i++) {
            CollectionsData.Collections collection = collections.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID, collection.getId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_NAME, collection.getName());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_TITLE, collection.getTitle());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_CREATED_AT, collection.getCreatedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT, collection.getUpdatedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT, collection.getFeaturedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT, collection.getSubscriberCount());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID, collection.getCategoryId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL, collection.getCollectionUrl());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS, collection.getPostsCount());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL, collection.getBackgroundImageUrl());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_NAME, collection.getUser().getName());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME, collection.getUser().getUsername());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_ID, collection.getUser().getId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX, collection.getUser().getImageUrl().getValue100px());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, collection.getUser().getImageUrl().getOriginal());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    private List<Collection> getCollectionsFromCursor(Cursor cursor) {
        List<Collection> collections = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

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
            collections.add(collection);
        }
        return collections;
    }

    public static class CollectionsUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No collections available currently.";
        }
    }
}
