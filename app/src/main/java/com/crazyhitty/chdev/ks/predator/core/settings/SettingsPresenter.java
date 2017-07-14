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

package com.crazyhitty.chdev.ks.predator.core.settings;

import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/16/2017 9:53 AM
 * Description: Unavailable
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private static final String TAG = "SettingsPresenter";

    @NonNull
    private SettingsContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    public SettingsPresenter(@NonNull SettingsContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void clearCache() {
        Observable<Boolean> clearCacheObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                // Delete all values from all tables except CATEGORY TABLE AS IT MUST STAY AS IT IS.
                // Delete all values from posts_table.
                PredatorDatabase.getInstance()
                        .deleteAllPosts();
                // Delete all values from users_table.
                PredatorDatabase.getInstance()
                        .deleteAllUsers();
                // Delete all values from comments_table.
                PredatorDatabase.getInstance()
                        .deleteAllComments();
                // Delete all values from install_links_table.
                PredatorDatabase.getInstance()
                        .deleteAllInstallLinks();
                // Delete all values from media_table.
                PredatorDatabase.getInstance()
                        .deleteAllMedia();
                // Delete all values from collections_table.
                PredatorDatabase.getInstance()
                        .deleteAllCollections();

                // Clear fresco cache.
                Fresco.getImagePipeline().clearCaches();

                emitter.onNext(true);
                emitter.onComplete();
            }
        });

        clearCacheObservable.subscribeOn(Schedulers.io());
        clearCacheObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(clearCacheObservable.subscribeWith(new DisposableObserver<Boolean>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToWipeCache();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Logger.d(TAG, "onNext: cache cleared");
                mView.cacheCleared();
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
