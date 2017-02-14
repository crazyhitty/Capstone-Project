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

package com.crazyhitty.chdev.ks.predator.core.about;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.AboutLibrary;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
 * Created:     1/25/2017 11:54 AM
 * Description: Unavailable
 */

public class AboutLibrariesPresenter implements AboutLibrariesContract.Presenter {
    private static final String TAG = "AboutLibrariesPresenter";

    @NonNull
    private AboutLibrariesContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public AboutLibrariesPresenter(@NonNull AboutLibrariesContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void fetchLibraries(final Context context) {
        Observable<List<AboutLibrary>> aboutObservable = Observable.create(new ObservableOnSubscribe<List<AboutLibrary>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AboutLibrary>> emitter) throws Exception {
                List<AboutLibrary> aboutLibraries = new ArrayList<AboutLibrary>();

                // Add libraries.
                // Fetch libraries from libraries.json located in res/raw.
                InputStream inputStream = context.getResources().openRawResource(R.raw.libraries);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String librariesJsonStr = "";
                try {
                    String value;
                    while ((value = reader.readLine()) != null) {
                        librariesJsonStr = librariesJsonStr.concat(value);
                    }
                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Convert librariesJsonStr to jsonObject for further parsing.
                try {
                    JSONObject jsonObjectLibraries = new JSONObject(librariesJsonStr);
                    JSONArray jsonArrayLibraries = jsonObjectLibraries.getJSONArray(Constants.Library.LIBRARIES);
                    for (int i = 0; i < jsonArrayLibraries.length(); i++) {
                        JSONObject jsonObjectLibrary = jsonArrayLibraries.getJSONObject(i);

                        String libraryTitle = jsonObjectLibrary.getString(Constants.Library.LIBRARY_TITLE);
                        String libraryCreator = jsonObjectLibrary.getString(Constants.Library.LIBRARY_CREATOR);
                        String libraryLicenseType = jsonObjectLibrary.getString(Constants.Library.LIBRARY_LICENSE_TYPE);
                        String libraryRedirectUrl = jsonObjectLibrary.getString(Constants.Library.LIBRARY_REDIRECT_URL);

                        AboutLibrary library = new AboutLibrary();
                        library.setTitle(libraryTitle);
                        library.setCreator(libraryCreator);
                        library.setLicenseType(libraryLicenseType);
                        library.setRedirectUrl(libraryRedirectUrl);

                        aboutLibraries.add(library);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                emitter.onNext(aboutLibraries);
                emitter.onComplete();
            }
        });
        aboutObservable.subscribeOn(Schedulers.io());
        aboutObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(aboutObservable.subscribeWith(new DisposableObserver<List<AboutLibrary>>() {
            @Override
            public void onNext(List<AboutLibrary> aboutLibraries) {
                mView.showLibrariesData(aboutLibraries);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToShowLibrariesData(e.getMessage());
            }

            @Override
            public void onComplete() {
                // Done.
            }
        }));
    }
}
