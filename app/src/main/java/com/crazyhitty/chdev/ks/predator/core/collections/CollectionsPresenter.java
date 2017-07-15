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

import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CollectionsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

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
    private CompositeDisposable mCompositeDisposable;

    public CollectionsPresenter(@NonNull CollectionsContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getOfflineCollections() {
        Observable<List<Collection>> collectionsCursorObservable = Observable.create(new ObservableOnSubscribe<List<Collection>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Collection>> emitter) throws Exception {
                // Retrieve the results from the database.
                List<Collection> collections = PredatorDatabase.getInstance()
                        .getCollections();
                if (collections != null && !collections.isEmpty()) {
                    emitter.onNext(collections);
                } else {
                    emitter.onError(new CollectionsUnavailableException());
                }
                emitter.onComplete();
            }
        });

        collectionsCursorObservable.subscribeOn(Schedulers.io());
        collectionsCursorObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(collectionsCursorObservable.subscribeWith(new DisposableObserver<List<Collection>>() {
            @Override
            public void onComplete() {
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
                .map(new Function<CollectionsData, List<Collection>>() {
                    @Override
                    public List<Collection> apply(CollectionsData collectionsData) throws Exception {
                        if (clearPrevious) {
                            // Clear previous collections from database.
                            PredatorDatabase.getInstance()
                                    .deleteAllCollections();

                            // Clear posts currently available for collections from database.
                            PredatorDatabase.getInstance()
                                    .deletePostsForCollections();
                        }

                        // Add content to the database.
                        PredatorDatabase.getInstance()
                                .insertCollections(PredatorDbValuesHelper.getBulkContentValuesForCollections(collectionsData.getCollections()));

                        // Retrieve the results from the database.
                        return PredatorDatabase.getInstance()
                                .getCollections();
                    }
                })
                .flatMap(new Function<List<Collection>, ObservableSource<List<Collection>>>() {
                    @Override
                    public ObservableSource<List<Collection>> apply(final List<Collection> collections) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<Collection>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<Collection>> emitter) throws Exception {
                                if (collections != null && collections.size() != 0) {
                                    emitter.onNext(collections);
                                } else {
                                    emitter.onError(new CollectionsUnavailableException());
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(collectionsCursorObservable.subscribeWith(new DisposableObserver<List<Collection>>() {
            @Override
            public void onComplete() {
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
        mCompositeDisposable.clear();
    }

    public static class CollectionsUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No collections available currently.";
        }
    }
}
