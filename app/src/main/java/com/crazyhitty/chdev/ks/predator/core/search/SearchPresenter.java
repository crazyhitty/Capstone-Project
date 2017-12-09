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

package com.crazyhitty.chdev.ks.predator.core.search;

import android.text.Spannable;

import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.SearchSpanCreator;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.SearchData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.SearchRequestData;
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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/9/17 10:26 PM
 * Description: Unavailable
 */

public class SearchPresenter implements SearchContract.Presenter {
    private static final String TAG = "SearchPresenter";

    private SearchContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    private int mPostSearchPage = 0;
    private int mCollectionSearchPage = 0;

    private boolean mLoadingMorePosts = false;
    private boolean mLoadingMoreCollections = false;

    private int mSearchSpanColor = SearchSpanCreator.COLOR_NONE;

    public SearchPresenter(SearchContract.View view, int searchSpanColor) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mSearchSpanColor = searchSpanColor;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void search(String keyword) {
        mCompositeDisposable.clear();

        mPostSearchPage = 0;
        mCollectionSearchPage = 0;

        mLoadingMorePosts = false;
        mLoadingMoreCollections = false;

        Observable<SearchDataType> searchDataTypeObservable = ProductHuntRestApi.getSearchApi()
                .search(SearchRequestData.getDefaultRequest(keyword))
                .flatMap(new Function<SearchData, ObservableSource<SearchDataType>>() {
                    @Override
                    public ObservableSource<SearchDataType> apply(final SearchData searchData) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<SearchDataType>() {
                            @Override
                            public void subscribe(ObservableEmitter<SearchDataType> emitter) throws Exception {
                                if (searchData == null) {
                                    emitter.onError(new NullPointerException("SearchData is empty"));
                                    emitter.onComplete();
                                    return;
                                }

                                for (SearchData.Results results : searchData.getResults()) {
                                    switch (results.getIndex()) {
                                        case Constants.Search.POST_PRODUCTION:
                                            List<Post> posts = new ArrayList<>();

                                            for (SearchData.Results.Hits hit : results.getHits()) {
                                                Post post = new Post();
                                                post.setName(hit.getName());
                                                post.setTagline(hit.getTagline());
                                                post.setVotesCount(hit.getVoteCount());
                                                post.setCommentCount(hit.getCommentsCount());
                                                post.setCreatedAt(hit.getCreatedAt());
                                                post.setCreatedAtMillis(hit.getPostedDate());
                                                post.setPostId(hit.getId());
                                                post.setRedirectUrl(hit.getUrl());
                                                post.setThumbnailImageUrl(hit.getThumbnail().getImageUrl());
                                                post.setUserId(hit.getUserId());
                                                post.setUserImageUrlOriginal(hit.getUser().getAvatarUrl());
                                                post.setUsername(hit.getUser().getName());
                                                post.setUsernameAlternative(hit.getUser().getUsername());
                                                post.setNameSpannable(getSpannableText(hit.getName(),
                                                        hit.getHighlightResult().getName().getMatchedWords()));
                                                post.setTaglineSpannable(getSpannableText(hit.getTagline(),
                                                        hit.getHighlightResult().getTagline().getMatchedWords()));
                                                posts.add(post);
                                            }

                                            SearchDataType searchDataPosts = new SearchDataType();
                                            searchDataPosts.setPosts(posts);
                                            searchDataPosts.setType(SearchDataType.TYPE.POST);
                                            emitter.onNext(searchDataPosts);
                                            break;
                                        case Constants.Search.COLLECTION_PRODUCTION:
                                            List<Collection> collections = new ArrayList<>();

                                            for (SearchData.Results.Hits hit : results.getHits()) {
                                                Collection collection = new Collection();
                                                collection.setCollectionId(hit.getId());
                                                collection.setName(hit.getName());
                                                collection.setTitle(hit.getTitle());
                                                collection.setBackgroundImageUrl(hit.getBackgroundImageBannerUrl());
                                                collection.setCollectionUrl(hit.getUrl());
                                                collection.setCategoryId(hit.getCategoryId());
                                                collection.setPostCounts(hit.getPostsCount());
                                                collection.setUserId(hit.getUserId());
                                                collection.setUsername(hit.getUser().getName());
                                                collection.setUsernameAlternative(hit.getUser().getUsername());
                                                collection.setUserImageUrl100px(hit.getUser().getAvatarUrl());
                                                collection.setNameSpannable(getSpannableText(hit.getName(),
                                                        hit.getHighlightResult().getName().getMatchedWords()));
                                                /*collection.setTitleSpannable(getSpannableText(hit.getTitle(),
                                                        hit.getHighlightResult().getTitle().getMatchedWords()));*/

                                                collections.add(collection);
                                            }

                                            SearchDataType searchDataCollections = new SearchDataType();
                                            searchDataCollections.setCollections(collections);
                                            searchDataCollections.setType(SearchDataType.TYPE.COLLECTION);
                                            emitter.onNext(searchDataCollections);
                                            break;
                                    }
                                }

                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(searchDataTypeObservable.subscribeWith(new DisposableObserver<SearchDataType>() {
            @Override
            public void onNext(SearchDataType searchDataType) {
                switch (searchDataType.getType()) {
                    case POST:
                        if (searchDataType.getPosts() != null && !searchDataType.getPosts().isEmpty()) {
                            mView.showPostResults(searchDataType.getPosts(), false);
                        } else {
                            mView.noPostsAvailable(false);
                        }
                        break;
                    case COLLECTION:
                        if (searchDataType.getCollections() != null && !searchDataType.getCollections().isEmpty()) {
                            mView.showCollectionResults(searchDataType.getCollections(), false);
                        } else {
                            mView.noCollectionsAvailable(false);
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, e.getMessage(), e);
                mView.noPostsAvailable(false);
                mView.noCollectionsAvailable(false);
            }

            @Override
            public void onComplete() {
                // Done.
            }
        }));
    }

    @Override
    public void loadMorePosts(String keyword) {
        mLoadingMorePosts = true;

        Observable<SearchDataType> searchDataTypeObservable = ProductHuntRestApi.getSearchApi()
                .search(SearchRequestData.getPostRequest(keyword, mPostSearchPage + 1))
                .flatMap(new Function<SearchData, ObservableSource<SearchDataType>>() {
                    @Override
                    public ObservableSource<SearchDataType> apply(final SearchData searchData) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<SearchDataType>() {
                            @Override
                            public void subscribe(ObservableEmitter<SearchDataType> emitter) throws Exception {
                                if (searchData == null) {
                                    emitter.onError(new NullPointerException("SearchData is empty"));
                                    emitter.onComplete();
                                    return;
                                }

                                for (SearchData.Results results : searchData.getResults()) {
                                    switch (results.getIndex()) {
                                        case Constants.Search.POST_PRODUCTION:
                                            List<Post> posts = new ArrayList<>();

                                            for (SearchData.Results.Hits hit : results.getHits()) {
                                                Post post = new Post();
                                                post.setName(hit.getName());
                                                post.setTagline(hit.getTagline());
                                                post.setVotesCount(hit.getVoteCount());
                                                post.setCommentCount(hit.getCommentsCount());
                                                post.setCreatedAt(hit.getCreatedAt());
                                                post.setCreatedAtMillis(hit.getPostedDate());
                                                post.setPostId(hit.getId());
                                                post.setRedirectUrl(hit.getUrl());
                                                post.setThumbnailImageUrl(hit.getThumbnail().getImageUrl());
                                                post.setUserId(hit.getUserId());
                                                post.setUserImageUrlOriginal(hit.getUser().getAvatarUrl());
                                                post.setUsername(hit.getUser().getName());
                                                post.setUsernameAlternative(hit.getUser().getUsername());
                                                post.setNameSpannable(getSpannableText(hit.getName(),
                                                        hit.getHighlightResult().getName().getMatchedWords()));
                                                post.setTaglineSpannable(getSpannableText(hit.getTagline(),
                                                        hit.getHighlightResult().getTagline().getMatchedWords()));
                                                posts.add(post);
                                            }

                                            SearchDataType searchDataPosts = new SearchDataType();
                                            searchDataPosts.setPosts(posts);
                                            searchDataPosts.setType(SearchDataType.TYPE.POST);
                                            emitter.onNext(searchDataPosts);
                                            break;
                                    }
                                }

                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(searchDataTypeObservable.subscribeWith(new DisposableObserver<SearchDataType>() {
            @Override
            public void onNext(SearchDataType searchDataType) {
                switch (searchDataType.getType()) {
                    case POST:
                        mPostSearchPage++;
                        if (searchDataType.getPosts() != null && !searchDataType.getPosts().isEmpty()) {
                            mView.showPostResults(searchDataType.getPosts(), true);
                        } else {
                            mView.noPostsAvailable(true);
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, e.getMessage(), e);
                mView.noPostsAvailable(true);
            }

            @Override
            public void onComplete() {
                // Done.
                mLoadingMorePosts = false;
            }
        }));
    }

    @Override
    public void loadMoreCollections(String keyword) {
        mLoadingMoreCollections = true;

        Observable<SearchDataType> searchDataTypeObservable = ProductHuntRestApi.getSearchApi()
                .search(SearchRequestData.getCollectionRequest(keyword, mCollectionSearchPage + 1))
                .flatMap(new Function<SearchData, ObservableSource<SearchDataType>>() {
                    @Override
                    public ObservableSource<SearchDataType> apply(final SearchData searchData) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<SearchDataType>() {
                            @Override
                            public void subscribe(ObservableEmitter<SearchDataType> emitter) throws Exception {
                                if (searchData == null) {
                                    emitter.onError(new NullPointerException("SearchData is empty"));
                                    emitter.onComplete();
                                    return;
                                }

                                for (SearchData.Results results : searchData.getResults()) {
                                    switch (results.getIndex()) {
                                        case Constants.Search.COLLECTION_PRODUCTION:
                                            List<Collection> collections = new ArrayList<>();

                                            for (SearchData.Results.Hits hit : results.getHits()) {
                                                Collection collection = new Collection();
                                                collection.setCollectionId(hit.getId());
                                                collection.setName(hit.getName());
                                                collection.setTitle(hit.getTitle());
                                                collection.setBackgroundImageUrl(hit.getBackgroundImageBannerUrl());
                                                collection.setCollectionUrl(hit.getUrl());
                                                collection.setCategoryId(hit.getCategoryId());
                                                collection.setPostCounts(hit.getPostsCount());
                                                collection.setUserId(hit.getUserId());
                                                collection.setUsername(hit.getUser().getName());
                                                collection.setUsernameAlternative(hit.getUser().getUsername());
                                                collection.setUserImageUrl100px(hit.getUser().getAvatarUrl());
                                                collection.setNameSpannable(getSpannableText(hit.getName(),
                                                        hit.getHighlightResult().getName().getMatchedWords()));
                                                /*collection.setTitleSpannable(getSpannableText(hit.getTitle(),
                                                        hit.getHighlightResult().getTitle().getMatchedWords()));*/
                                                collections.add(collection);
                                            }

                                            SearchDataType searchDataCollections = new SearchDataType();
                                            searchDataCollections.setCollections(collections);
                                            searchDataCollections.setType(SearchDataType.TYPE.COLLECTION);
                                            emitter.onNext(searchDataCollections);
                                            break;
                                    }
                                }

                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(searchDataTypeObservable.subscribeWith(new DisposableObserver<SearchDataType>() {
            @Override
            public void onNext(SearchDataType searchDataType) {
                switch (searchDataType.getType()) {
                    case COLLECTION:
                        mCollectionSearchPage++;
                        if (searchDataType.getCollections() != null && !searchDataType.getCollections().isEmpty()) {
                            mView.showCollectionResults(searchDataType.getCollections(), true);
                        } else {
                            mView.noCollectionsAvailable(true);
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, e.getMessage(), e);
                mView.noCollectionsAvailable(true);
            }

            @Override
            public void onComplete() {
                // Done.
                mLoadingMoreCollections = false;
            }
        }));
    }

    @Override
    public void cancelOngoingRequest() {
        mCompositeDisposable.clear();
    }

    @Override
    public boolean isLoadingMorePosts() {
        return mLoadingMorePosts;
    }

    @Override
    public boolean isLoadingMoreCollections() {
        return mLoadingMoreCollections;
    }

    private Spannable getSpannableText(String text, List<String> selections) {
        return SearchSpanCreator.getInstance()
                .create(mSearchSpanColor, text, selections);
    }

    private static class SearchDataType {
        private List<Post> posts;
        private List<Collection> collections;
        private TYPE type;

        public SearchDataType() {

        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public List<Collection> getCollections() {
            return collections;
        }

        public void setCollections(List<Collection> collections) {
            this.collections = collections;
        }

        public TYPE getType() {
            return type;
        }

        public void setType(TYPE type) {
            this.type = type;
        }

        enum TYPE {
            POST,
            COLLECTION
        }
    }
}
