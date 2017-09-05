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

package com.crazyhitty.chdev.ks.predator.core.posts;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.widget.PredatorPostsWidgetProvider;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Created:     1/2/2017 10:13 PM
 * Description: Unavailable
 */

public class PostsPresenter implements PostsContract.Presenter {
    private static final String TAG = "PostsPresenter";

    private String mLastDate = DateUtils.getPredatorCurrentDate();

    private HashMap<Integer, String> mDateHashMap = new HashMap<>();

    private boolean mLoadMore = false;

    @NonNull
    private PostsContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    public PostsPresenter(@NonNull PostsContract.View view) {
        this.mView = view;
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
    public void getOfflinePosts() {
        Observable<List<Post>> postsDataObservable = Observable.create(new ObservableOnSubscribe<List<Post>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Post>> emitter) throws Exception {
                List<Post> posts = PredatorDatabase.getInstance()
                        .getPosts();
                if (posts != null && !posts.isEmpty()) {
                    dateMatcher(posts);
                    emitter.onNext(posts);
                } else {
                    emitter.onError(new NoPostsAvailableException());
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
                mView.unableToGetPosts(false, true, e.getMessage());
            }

            @Override
            public void onNext(List<Post> posts) {
                mView.showPosts(posts, mDateHashMap);
            }
        }));
    }

    @Override
    public void getPosts(final String token, boolean today) {
        Logger.d(TAG, "getPosts: called");

        if (today) {
            mLoadMore = false;
            mLastDate = DateUtils.getPredatorCurrentDate();
            mDateHashMap = new HashMap<>();
        }

        Observable<List<Post>> postsDataObservable = ProductHuntRestApi.getApi()
                .getPostsCategoryWise(CoreUtils.getAuthToken(token), Constants.Posts.CATEGORY_ALL, mLastDate)
                .map(new Function<PostsData, List<Post>>() {
                    @Override
                    public List<Post> apply(PostsData postsData) throws Exception {
                        if (postsData.getPosts() == null || postsData.getPosts().isEmpty()) {
                            return new ArrayList<Post>();
                        }

                        for (PostsData.Posts post : postsData.getPosts()) {
                            Logger.d(TAG, "post: " + post.getName());
                            PredatorDatabase.getInstance()
                                    .insertPost(PredatorDbValuesHelper.getContentValuesForPost(post));

                            // Add/update users.
                            PredatorDatabase.getInstance()
                                    .insertUser(PredatorDbValuesHelper.getContentValuesForHunterUser(post.getId(), post.getUser()));
                            for (PostsData.Posts.Makers maker : post.getMakers()) {
                                PredatorDatabase.getInstance()
                                        .insertUser(PredatorDbValuesHelper.getContentValuesForMakerUser(post.getId(), maker));
                            }
                        }

                        List<Post> posts = PredatorDatabase.getInstance()
                                .getPosts();
                        if (posts != null && !posts.isEmpty()) {
                            dateMatcher(posts);
                        }
                        Logger.d(TAG, "apply: posts: " + posts);
                        return posts;
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
                                    loadMorePosts(token);
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
                mView.unableToGetPosts(mLoadMore, false, e.getMessage());
            }

            @Override
            public void onNext(List<Post> posts) {
                mView.showPosts(posts, mDateHashMap);
            }
        }));
    }

    @Override
    public void loadMorePosts(String token) {
        mLastDate = DateUtils.getPredatorPostPreviousDate(mLastDate);
        mLoadMore = true;
        getPosts(token, false);
    }

    @Override
    public void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, PredatorPostsWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_posts);
    }

    @Override
    public void clear() {
        Logger.d(TAG, "clear: called");
        Observable<Void> clearPostsObservable = Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
                PredatorDatabase.getInstance()
                        .deleteAllPosts();
                PredatorDatabase.getInstance()
                        .deleteAllUsers();
                PredatorDatabase.getInstance()
                        .deleteAllComments();
                PredatorDatabase.getInstance()
                        .deleteAllMedia();
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(clearPostsObservable.subscribeWith(new DisposableObserver<Void>() {
            @Override
            public void onComplete() {
                // Done
                mView.postsCleared();
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToClearPosts(e.getMessage());
            }

            @Override
            public void onNext(Void aVoid) {

            }
        }));
    }

    /**
     * Matches all the post publish dates and create a hashmap for positions and dates wherever the
     * dates are changed in the cursor.
     *
     * @param posts List containing posts
     */
    private void dateMatcher(List<Post> posts) {
        for (int i = 0; i < posts.size(); i++) {
            // Match post date with current date
            String date = posts.get(i).getDay();

            String dateToBeShown = DateUtils.getPredatorPostDate(date);

            if (!mDateHashMap.containsValue(dateToBeShown)) {
                mDateHashMap.put(i, dateToBeShown);
                mLastDate = date;
            }
        }
    }

    public static class NoPostsAvailableException extends Throwable {
        @Override
        public String getMessage() {
            return "No posts available.";
        }
    }
}
