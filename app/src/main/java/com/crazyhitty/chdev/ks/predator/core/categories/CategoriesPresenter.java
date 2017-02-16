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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Category;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CategoriesData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public void fetchCategories(final Context context, final String token, boolean loadOffline) {
        Observable<List<Category>> categoriesObservable;

        if (loadOffline) {
            categoriesObservable = Observable.create(new ObservableOnSubscribe<List<Category>>() {
                @Override
                public void subscribe(ObservableEmitter<List<Category>> emitter) throws Exception {
                    // Add categories.
                    // Fetch categories from categories.json located in res/raw.
                    InputStream inputStream = context.getResources().openRawResource(R.raw.categories);
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
                    MainApplication.getContentResolverInstance()
                            .delete(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY_DELETE, null, null);

                    // Insert new categories into category table.
                    MainApplication.getContentResolverInstance()
                            .bulkInsert(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY_ADD,
                                    getBulkContentValuesForCategories(categoriesData.getCategories()));

                    Cursor cursor = MainApplication.getContentResolverInstance()
                            .query(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY,
                                    null,
                                    null,
                                    null,
                                    null);

                    List<Category> categories = new ArrayList<Category>();
                    if (cursor != null && cursor.getCount() != 0) {
                        categories = getCategoriesFromCursor(cursor);
                        cursor.close();
                    }

                    if (categories != null && categories.size() != 0) {
                        emitter.onNext(categories);
                    } else {
                        emitter.onError(new NoCategoriesAvailableException());
                    }
                    emitter.onComplete();
                }
            });
        } else {
            categoriesObservable = ProductHuntRestApi.getApi()
                    .getCategories(CoreUtils.getAuthToken(token))
                    .map(new Function<CategoriesData, List<Category>>() {
                        @Override
                        public List<Category> apply(CategoriesData categoriesData) throws Exception {
                            // Delete existing categories from category table.
                            MainApplication.getContentResolverInstance()
                                    .delete(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY_DELETE, null, null);

                            // Insert new categories into category table.
                            MainApplication.getContentResolverInstance()
                                    .bulkInsert(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY_ADD,
                                            getBulkContentValuesForCategories(categoriesData.getCategories()));

                            Cursor cursor = MainApplication.getContentResolverInstance()
                                    .query(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY,
                                            null,
                                            null,
                                            null,
                                            null);

                            List<Category> categories = new ArrayList<Category>();
                            if (cursor != null && cursor.getCount() != 0) {
                                categories = getCategoriesFromCursor(cursor);
                                cursor.close();
                            }

                            return categories;
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
                    });
        }

        categoriesObservable.subscribeOn(Schedulers.io());
        categoriesObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(categoriesObservable.subscribeWith(new DisposableObserver<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                Logger.d(TAG, "onNext: CategoriesSize: " + categories.size());
                mView.showCategories(categories);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                fetchCategories(context, token, true);
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

    private ContentValues[] getBulkContentValuesForCategories(List<CategoriesData.Categories> categories) {
        ContentValues[] contentValuesArr = new ContentValues[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            CategoriesData.Categories category = categories.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID, category.getId());
            contentValues.put(PredatorContract.CategoryEntry.COLUMN_SLUG, category.getSlug());
            contentValues.put(PredatorContract.CategoryEntry.COLUMN_NAME, category.getName());
            contentValues.put(PredatorContract.CategoryEntry.COLUMN_COLOR, category.getColor());
            contentValues.put(PredatorContract.CategoryEntry.COLUMN_ITEM_NAME, category.getItemName());

            contentValuesArr[i] = contentValues;
        }

        return contentValuesArr;
    }

    private List<Category> getCategoriesFromCursor(Cursor cursor) {
        List<Category> categories = new ArrayList<>();
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            Category category = new Category();

            category.setId(CursorUtils.getInt(cursor, PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID));
            category.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID));
            category.setSlug(CursorUtils.getString(cursor, PredatorContract.CategoryEntry.COLUMN_SLUG));
            category.setName(CursorUtils.getString(cursor, PredatorContract.CategoryEntry.COLUMN_NAME));
            category.setColor(CursorUtils.getString(cursor, PredatorContract.CategoryEntry.COLUMN_COLOR));
            category.setItemName(CursorUtils.getString(cursor, PredatorContract.CategoryEntry.COLUMN_ITEM_NAME));

            categories.add(category);
        }

        return categories;
    }

    private static class NoCategoriesAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No categories available.";
        }
    }
}