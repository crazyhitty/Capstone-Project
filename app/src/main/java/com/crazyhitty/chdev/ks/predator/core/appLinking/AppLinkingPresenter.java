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

package com.crazyhitty.chdev.ks.predator.core.appLinking;

import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     8/6/17 7:32 PM
 * Description: Unavailable
 */

public class AppLinkingPresenter implements AppLinkingContract.Presenter {
    private static final String TAG = "AppLinkingPresenter";

    @NonNull
    private AppLinkingContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public AppLinkingPresenter(@NonNull AppLinkingContract.View view) {
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
    public void fetchPostDetailsFromSlug(String token, String slug) {
        Observable<Post> postObservable = ProductHuntRestApi.getApi()
                .searchPostBySlug(CoreUtils.getAuthToken(token), slug)
                .map(new Function<PostsData, Post>() {
                    @Override
                    public Post apply(PostsData postsData) throws Exception {
                        if (postsData.getPosts() == null || postsData.getPosts().isEmpty()) {
                            return null;
                        }

                        for (PostsData.Posts post : postsData.getPosts()) {
                            Logger.d(TAG, "post: " + post.getName());
                            PredatorDatabase.getInstance()
                                    .insertPost(PredatorDbValuesHelper.getContentValuesForPostOnlyAppLink(post));

                            // Add/update users.
                            PredatorDatabase.getInstance()
                                    .insertUser(PredatorDbValuesHelper.getContentValuesForHunterUser(post.getId(), post.getUser()));
                            for (PostsData.Posts.Makers maker : post.getMakers()) {
                                PredatorDatabase.getInstance()
                                        .insertUser(PredatorDbValuesHelper.getContentValuesForMakerUser(post.getId(), maker));
                            }
                        }

                        Post post = PredatorDatabase.getInstance()
                                .getPost(postsData.getPosts().get(0).getId());
                        Logger.d(TAG, "apply: post: " + post);
                        return post;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postObservable.subscribeWith(new DisposableObserver<Post>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToFetchPostDetails();
            }

            @Override
            public void onNext(Post post) {
                if (post != null) {
                    mView.showPostDetails(post.getPostId());
                } else {
                    mView.unableToFetchPostDetails();
                }
            }
        }));
    }
}
