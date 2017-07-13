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
import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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

    private CompositeDisposable mCompositeDisposable;

    private Collection mCollection;

    public CollectionDetailsPresenter(@NonNull CollectionDetailsContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getCollectionDetails(final int collectionId) {
        Observable<Collection> collectionsCursorObservable = Observable.create(new ObservableOnSubscribe<Collection>() {
            @Override
            public void subscribe(ObservableEmitter<Collection> emitter) throws Exception {
                // Retrieve the results from the database.
                Collection collection = PredatorDatabase.getInstance()
                        .getCollectionDetails(collectionId);
                if (collection != null) {
                    emitter.onNext(collection);
                } else {
                    emitter.onError(new CollectionsPresenter.CollectionsUnavailableException());
                }
                emitter.onComplete();
            }
        });

        collectionsCursorObservable.subscribeOn(Schedulers.io());
        collectionsCursorObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(collectionsCursorObservable.subscribeWith(new DisposableObserver<Collection>() {
            @Override
            public void onComplete() {
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
        Observable<List<Post>> postsDataObservable = Observable.create(new ObservableOnSubscribe<List<Post>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Post>> emitter) throws Exception {
                List<Post> posts = PredatorDatabase.getInstance()
                        .getPostsForCollection(collectionId);
                if (posts != null && !posts.isEmpty()) {
                    emitter.onNext(posts);
                } else {
                    emitter.onError(new PostsPresenter.NoPostsAvailableException());
                }
                emitter.onComplete();
            }
        });
        postsDataObservable.subscribeOn(Schedulers.io());
        postsDataObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postsDataObservable.subscribeWith(new DisposableObserver<List<Post>>() {
            @Override
            public void onComplete() {
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
                .map(new Function<CollectionDetailsData, List<Post>>() {
                    @Override
                    public List<Post> apply(CollectionDetailsData collectionDetailsData) throws Exception {
                        // Remove old posts for that collection id
                        PredatorDatabase.getInstance()
                                .deletePostsForCollection(collectionId);

                        if (collectionDetailsData.getCollection().getPosts() == null || collectionDetailsData.getCollection().getPosts().isEmpty()) {
                            return null;
                        }

                        for (PostsData.Posts post : collectionDetailsData.getCollection().getPosts()) {
                            Logger.d(TAG, "post: " + post.getName());
                            PredatorDatabase.getInstance()
                                    .insertPost(PredatorDbValuesHelper.getContentValuesForPost(collectionId, post));

                            // Add/update users.
                            PredatorDatabase.getInstance()
                                    .insertUser(PredatorDbValuesHelper.getContentValuesForHunterUser(post.getId(), post.getUser()));
                            for (PostsData.Posts.Makers maker : post.getMakers()) {
                                PredatorDatabase.getInstance()
                                        .insertUser(PredatorDbValuesHelper.getContentValuesForMakerUser(post.getId(), maker));
                            }
                        }

                        return PredatorDatabase.getInstance()
                                .getPostsForCollection(collectionId);
                    }
                })
                .flatMap(new Function<List<Post>, ObservableSource<List<Post>>>() {
                    @Override
                    public ObservableSource<List<Post>> apply(final List<Post> posts) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<Post>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<Post>> emitter) throws Exception {
                                if (posts != null && posts.size() != 0) {
                                    emitter.onNext(posts);
                                } else {
                                    emitter.onError(new NoCollectionPostsAvailableException());
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postsDataObservable.subscribeWith(new DisposableObserver<List<Post>>() {
            @Override
            public void onComplete() {
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
        mCompositeDisposable.clear();
    }

    public static class NoCollectionPostsAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No posts available for this collection.";
        }
    }
}
