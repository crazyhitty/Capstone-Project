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

package com.crazyhitty.chdev.ks.predator.core.fontSelection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.AboutLibrary;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.json.JSONArray;
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
 * Created:     7/23/17 10:18 PM
 * Description: Unavailable
 */

public class FontSelectionPresenter implements FontSelectionContract.Presenter {
    private static final String TAG = "FontSelectionPresenter";

    @NonNull
    private FontSelectionContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public FontSelectionPresenter(@NonNull FontSelectionContract.View view) {
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
    public void getDummyPosts(final Context context) {
        Observable<List<Post>> postObservable = Observable.create(new ObservableOnSubscribe<List<Post>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Post>> emitter) throws Exception {
                List<Post> posts = new ArrayList<>();

                // Add dummy posts.
                // Fetch dummy posts from dummy_posts.json located in res/raw.
                InputStream inputStream = context.getResources().openRawResource(R.raw.dummy_posts);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String dummyPostsJsonStr = "";
                try {
                    String value;
                    while ((value = reader.readLine()) != null) {
                        dummyPostsJsonStr = dummyPostsJsonStr.concat(value);
                    }
                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Convert librariesJsonStr to jsonObject for further parsing.
                JSONObject jsonObjectDummyPosts = new JSONObject(dummyPostsJsonStr);
                JSONArray jsonArrayDummyPosts = jsonObjectDummyPosts.getJSONArray(Constants.Posts.POSTS);
                for (int i = 0; i < jsonArrayDummyPosts.length(); i++) {
                    JSONObject jsonObjectDummyPost = jsonArrayDummyPosts.getJSONObject(i);

                    String postName = jsonObjectDummyPost.getString(Constants.Posts.NAME);
                    String postTagline = jsonObjectDummyPost.getString(Constants.Posts.TAGLINE);

                    Post post = new Post();
                    post.setName(postName);
                    post.setTagline(postTagline);

                    posts.add(post);
                }

                emitter.onNext(posts);
                emitter.onComplete();
            }
        });

        postObservable.subscribeOn(Schedulers.io());
        postObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postObservable.subscribeWith(new DisposableObserver<List<Post>>() {
            @Override
            public void onNext(List<Post> value) {
                mView.showDummyPosts(value);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.dummyPostsUnavailable();
            }

            @Override
            public void onComplete() {
                // Done.
            }
        }));
    }
}
