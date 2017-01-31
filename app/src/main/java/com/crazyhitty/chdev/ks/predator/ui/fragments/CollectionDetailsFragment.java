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

package com.crazyhitty.chdev.ks.predator.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.collectionDetails.CollectionDetailsContract;
import com.crazyhitty.chdev.ks.predator.core.collectionDetails.CollectionDetailsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.NetworkEvent;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.PostsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.views.LoadingView;
import com.crazyhitty.chdev.ks.predator.utils.CollectionPostItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/21/2017 1:27 PM
 * Description: Unavailable
 */

public class CollectionDetailsFragment extends BaseSupportFragment implements CollectionDetailsContract.View {
    private static final String TAG = "CollectionDetailsFragment";
    private static final String ARG_COLLECTION_TABLE_ID = "id";
    private static final String ARG_COLLECTION_TABLE_COLLECTION_ID = "collection_id";

    @BindView(R.id.recycler_view_posts)
    RecyclerView recyclerViewPosts;
    @BindView(R.id.swipe_refresh_layout_posts)
    SwipeRefreshLayout swipeRefreshLayoutPosts;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private CollectionDetailsContract.Presenter mCollectionDetailsPresenter;

    private PostsRecyclerAdapter mPostsRecyclerAdapter;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    public static CollectionDetailsFragment newInstance(int id, int collectionId) {
        Bundle args = new Bundle();
        args.putInt(ARG_COLLECTION_TABLE_ID, id);
        args.putInt(ARG_COLLECTION_TABLE_COLLECTION_ID, collectionId);
        CollectionDetailsFragment fragment = new CollectionDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new CollectionDetailsPresenter(this));
        mCollectionDetailsPresenter.subscribe();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLoadingView();
        initSwipeRefreshLayout();
        setRecyclerViewProperties();

        // load collection details.
        mCollectionDetailsPresenter.getCollectionDetails(getArguments().getInt(ARG_COLLECTION_TABLE_COLLECTION_ID));

        // Always load offline posts first.
        mCollectionDetailsPresenter.getOfflinePosts(getArguments().getInt(ARG_COLLECTION_TABLE_COLLECTION_ID));
    }

    private void initLoadingView() {
        // Stop loading initially.
        loadingView.stopLoading();
        // Set loading type.
        loadingView.setLoadingType(LoadingView.TYPE.COLLECTION_POSTS);
        // Add retry listener on loading view.
        loadingView.setOnRetryClickListener(new LoadingView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                if (isNetworkAvailable(true)) {
                    // Get latest posts if internet is available and no offline posts are available currently.
                    loadingView.startLoading(LoadingView.TYPE.COLLECTION_POSTS);
                    getLatestPosts();
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        // Disable swipe refresh layout initially.
        swipeRefreshLayoutPosts.setEnabled(false);
        swipeRefreshLayoutPosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(true)) {
                    getLatestPosts();
                } else {
                    swipeRefreshLayoutPosts.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayoutPosts.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    private void setRecyclerViewProperties() {
        // Create a list type layout manager.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        CollectionPostItemDecorator listItemDecorator = new CollectionPostItemDecorator(getContext().getApplicationContext(), 72);
        recyclerViewPosts.addItemDecoration(listItemDecorator);

        mPostsRecyclerAdapter = new PostsRecyclerAdapter(null,
                PostsRecyclerAdapter.TYPE.LIST);
        recyclerViewPosts.setAdapter(mPostsRecyclerAdapter);

        mPostsRecyclerAdapter.setOnItemClickListener(new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PostDetailsActivity.startActivity(getContext(),
                        mPostsRecyclerAdapter.getPostId(position));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkConnectivityChanged(NetworkEvent networkEvent) {
        if (networkEvent.isConnected() &&
                loadingView.getCurrentState() == LoadingView.STATE_SHOWN.ERROR &&
                mPostsRecyclerAdapter == null ||
                mPostsRecyclerAdapter.getItemCount() == 0) {
            // Get latest posts if internet is available and no offline posts are available currently.
            Logger.d(TAG, "onNetworkConnectivityChanged: called");
            loadingView.startLoading(LoadingView.TYPE.COLLECTION_POSTS);
            getLatestPosts();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCollectionDetailsPresenter.unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }

    @Override
    public void showCollectionDetails(Collection collection) {
        mOnFragmentInteractionListener.setToolbarTitle(collection.getName());
    }

    @Override
    public void showPosts(List<Post> posts) {
        mOnFragmentInteractionListener.setAppBarContents(mCollectionDetailsPresenter.getCurrentCollection());
        mOnFragmentInteractionListener.expandAppBar();

        // Hide loading view
        loadingView.setComplete(getString(R.string.posts_successfully_loaded_posts));

        // Enable swipe refresh layout
        swipeRefreshLayoutPosts.setEnabled(true);

        setListTypeAdapter(posts);

        // Dismiss swipe refresh layout animation if it is going on.
        if (swipeRefreshLayoutPosts.isRefreshing()) {
            swipeRefreshLayoutPosts.setRefreshing(false);
        }
    }

    @Override
    public void unableToGetPosts(boolean wasLoadingOfflinePosts, String errorMessage) {
        swipeRefreshLayoutPosts.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutPosts.setRefreshing(false);
            }
        });
        if (wasLoadingOfflinePosts && isNetworkAvailable(false)) {
            // Get latest posts if internet is available and no offline posts are available currently.
            loadingView.startLoading(LoadingView.TYPE.COLLECTION_POSTS);
            getLatestPosts();
        } else {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setError(errorMessage);
        }
    }

    @Override
    public void setPresenter(CollectionDetailsContract.Presenter presenter) {
        mCollectionDetailsPresenter = presenter;
    }

    private void getLatestPosts() {
        PredatorAccount.getAuthToken(getActivity(),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        mCollectionDetailsPresenter.getPosts(s, getArguments().getInt(ARG_COLLECTION_TABLE_COLLECTION_ID));
                    }
                });
    }

    private void setListTypeAdapter(List<Post> posts) {
        mPostsRecyclerAdapter.updateDataset(posts, true);
    }

    public interface OnFragmentInteractionListener {
        void setToolbarTitle(String title);

        void setAppBarContents(Collection collection);

        void expandAppBar();
    }
}
