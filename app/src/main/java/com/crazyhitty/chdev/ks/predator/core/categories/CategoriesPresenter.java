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

package com.crazyhitty.chdev.ks.predator.core.categories;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Category;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CategoriesData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import static com.crazyhitty.chdev.ks.predator.R.raw.categories;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/15/2017 3:02 PM
 * Description: Unavailable
 */

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private static final String TAG = "CategoriesPresenter";

    @NonNull
    private CategoriesContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    public CategoriesPresenter(@NonNull CategoriesContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void fetchCategories(final Context context, final String token) {
        Observable<List<Category>> categoriesObservable = ProductHuntRestApi.getApi()
                .getCategories(CoreUtils.getAuthToken(token))
                .map(new Function<CategoriesData, List<Category>>() {
                    @Override
                    public List<Category> apply(CategoriesData categoriesData) throws Exception {
                        // Delete existing categories from category table.
                        PredatorDatabase.getInstance()
                                .deleteAllCategories();

                        // Insert new categories into category table.
                        PredatorDatabase.getInstance()
                                .insertCategories(PredatorDbValuesHelper.getBulkContentValuesForCategories(categoriesData.getCategories()));

                        return PredatorDatabase.getInstance()
                                .getCategories();
                    }
                })
                .flatMap(new Function<List<Category>, ObservableSource<List<Category>>>() {
                    @Override
                    public ObservableSource<List<Category>> apply(final List<Category> categories) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<Category>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<Category>> emitter) throws Exception {
                                if (categories != null && categories.size() != 0) {
                                    emitter.onNext(categories);
                                } else {
                                    emitter.onError(new NoCategoriesAvailableException());
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(categoriesObservable.subscribeWith(new DisposableObserver<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                Logger.d(TAG, "onNext: CategoriesSize: " + categories.size());
                mView.showCategories(categories);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                fetchCategoriesOffline(context);
            }

            @Override
            public void onComplete() {
                // Done
            }
        }));
    }

    @Override
    public void fetchCategoriesOffline(final Context context) {
        Observable<List<Category>> categoriesObservable = Observable.create(new ObservableOnSubscribe<List<Category>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Category>> emitter) throws Exception {
                // Add categories.
                // Fetch categories from categories.json located in res/raw.
                InputStream inputStream = context.getResources().openRawResource(categories);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String categoriesJsonStr = "";
                try {
                    String value;
                    while ((value = reader.readLine()) != null) {
                        categoriesJsonStr = categoriesJsonStr.concat(value);
                    }
                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Convert librariesJsonStr to CategoriesData object via gson.
                CategoriesData categoriesData = new Gson().fromJson(categoriesJsonStr, CategoriesData.class);

                // Delete existing categories from category table.
                PredatorDatabase.getInstance()
                        .deleteAllCategories();

                // Insert new categories into category table.
                PredatorDatabase.getInstance()
                        .insertCategories(PredatorDbValuesHelper.getBulkContentValuesForCategories(categoriesData.getCategories()));

                List<Category> categories = PredatorDatabase.getInstance()
                        .getCategories();

                if (categories != null && categories.size() != 0) {
                    emitter.onNext(categories);
                } else {
                    emitter.onError(new NoCategoriesAvailableException());
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(categoriesObservable.subscribeWith(new DisposableObserver<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                Logger.d(TAG, "onNext: CategoriesSize: " + categories.size());
                mView.showCategories(categories);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.categoriesUnavailable();
            }

            @Override
            public void onComplete() {
                // Done
            }
        }));
    }

    @Override
    public void checkIfCategoriesAreAvailable() {
        Observable<Boolean> categoriesAvailableObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                List<Category> categories = PredatorDatabase.getInstance()
                        .getCategories();

                if (categories != null && !categories.isEmpty()) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(categoriesAvailableObservable.subscribeWith(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean status) {
                Logger.d(TAG, "onNext: categoriesAvailable: " + status);
                if (status) {
                    mView.categoriesAvailable();
                } else {
                    mView.categoriesUnavailable();
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
            }

            @Override
            public void onComplete() {
                // Done
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

    private static class NoCategoriesAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No categories available.";
        }
    }
}