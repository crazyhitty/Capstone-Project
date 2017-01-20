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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.collections.CollectionsContract;
import com.crazyhitty.chdev.ks.predator.core.collections.CollectionsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.NetworkEvent;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.ui.adapters.CollectionsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.views.LoadingView;
import com.crazyhitty.chdev.ks.predator.utils.CollectionItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/19/2017 2:53 PM
 * Description: Unavailable
 */

public class CollectionFragment extends BaseSupportFragment implements CollectionsContract.View, CollectionsRecyclerAdapter.OnCollectionsLoadMoreRetryListener {
    private static final String TAG = "CollectionFragment";

    @BindView(R.id.swipe_refresh_layout_collections)
    SwipeRefreshLayout swipeRefreshLayoutCollections;
    @BindView(R.id.recycler_view_collections)
    RecyclerView recyclerViewCollections;
    @BindView(R.id.loading_view)
    LoadingView loadingView;

    private CollectionsContract.Presenter mCollectionsPresenter;

    private CollectionsRecyclerAdapter mCollectionsRecyclerAdapter;

    private boolean mIsLoading = false;
    private int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setPresenter(new CollectionsPresenter(this));
        mCollectionsPresenter.subscribe();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLoadingView();
        initSwipeRefreshLayout();
        setCollectionsRecyclerViewProperties();

        // Always load offline collections first.
        mCollectionsPresenter.getOfflineCollections();
    }

    private void initLoadingView() {
        // Stop loading initially.
        loadingView.stopLoading();
        // Set loading type.
        loadingView.setLoadingType(LoadingView.TYPE.LATEST_COLLECTIONS);
        // Add retry listener on loading view.
        loadingView.setOnRetryClickListener(new LoadingView.OnRetryClickListener() {
            @Override
            public void onRetry() {
                if (isNetworkAvailable(true)) {
                    // Get latest posts if internet is available and no offline collections are
                    // available currently.
                    loadingView.startLoading(LoadingView.TYPE.LATEST_COLLECTIONS);
                    getLatestCollections();
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        // Disable swipe refresh layout initially.
        swipeRefreshLayoutCollections.setEnabled(false);
        swipeRefreshLayoutCollections.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(true)) {
                    getLatestCollections();
                } else {
                    swipeRefreshLayoutCollections.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayoutCollections.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    private void setCollectionsRecyclerViewProperties() {
        // Create a layout manager for recycler view.
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerViewCollections.setLayoutManager(layoutManager);

        // Set item decorations.
        recyclerViewCollections.addItemDecoration(new CollectionItemDecorator(getContext(), 8));

        // Add scroll listener that will manage scroll down to load more functionality.
        recyclerViewCollections.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the last item is on screen, if yes then start loading more posts.
                // Source: https://github.com/kajal-mittal/Flix/blob/master/app/src/main/java/com/kmdev/flix/ui/fragments/ItemListFragment.java#L172
                mVisibleItemCount = layoutManager.getChildCount();
                mTotalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    mPastVisibleItems = firstVisibleItems[0];
                }

                if (((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount) &&
                        !mIsLoading &&
                        isNetworkAvailable(false)) {
                    mIsLoading = true;
                    loadMoreCollections();
                }
            }
        });

        // Create adapter that will power this recycler view.
        mCollectionsRecyclerAdapter = new CollectionsRecyclerAdapter(null, this);

        recyclerViewCollections.setAdapter(mCollectionsRecyclerAdapter);

        mCollectionsRecyclerAdapter.setOnItemClickListener(new CollectionsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showShortToast(R.string.not_yet_implemented);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCollectionsPresenter.unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkConnectivityChanged(NetworkEvent networkEvent) {
        if (mCollectionsRecyclerAdapter != null && mCollectionsRecyclerAdapter.getItemCount() != 0) {
            mCollectionsRecyclerAdapter.setNetworkStatus(networkEvent.isConnected(), getString(R.string.item_load_more_collection_error_desc));
        } else if (networkEvent.isConnected() && loadingView.getCurrentState() == LoadingView.STATE_SHOWN.ERROR) {
            // Get latest posts if internet is available and no offline posts are available currently.
            loadingView.startLoading(LoadingView.TYPE.LATEST_POSTS);
            getLatestCollections();
        }
    }

    @Override
    public void showCollections(List<Collection> collections) {
        if (mIsLoading) {
            mIsLoading = false;
            mCollectionsRecyclerAdapter.updateDataset(collections, false);
        } else {
            // Hide loading view
            loadingView.setComplete(getString(R.string.posts_successfully_loaded_posts));

            // Enable swipe refresh layout
            swipeRefreshLayoutCollections.setEnabled(true);

            mCollectionsRecyclerAdapter.updateDataset(collections, true);
        }

        // Dismiss swipe refresh layout animation if it is going on.
        if (swipeRefreshLayoutCollections.isRefreshing()) {
            swipeRefreshLayoutCollections.setRefreshing(false);
        }

        mCollectionsRecyclerAdapter.setNetworkStatus(isNetworkAvailable(false), getString(R.string.item_load_more_posts_error_desc));
    }

    @Override
    public void unableToFetchCollections(boolean onLoadMore, boolean wasLoadingOfflinePosts, String errorMessage) {
        if (mCollectionsRecyclerAdapter != null && mCollectionsRecyclerAdapter.getItemCount() != 0) {
            // If the adapter contains items already.
            showLongToast(errorMessage);
            if (onLoadMore) {
                // If the error occurs while loading more collections.
                mCollectionsRecyclerAdapter.setNetworkStatus(false, errorMessage);
            }
        } else if (wasLoadingOfflinePosts && isNetworkAvailable(false)) {
            // Get latest posts if internet is available and no offline collections are available currently.
            loadingView.startLoading(LoadingView.TYPE.LATEST_COLLECTIONS);
            getLatestCollections();
        } else {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setError(errorMessage);
        }
    }

    @Override
    public void setPresenter(CollectionsContract.Presenter presenter) {
        mCollectionsPresenter = presenter;
    }

    private void getLatestCollections() {
        PredatorAccount.getAuthToken(getActivity(),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        mCollectionsPresenter.getLatestCollections(s, true);
                    }
                });
    }

    private void loadMoreCollections() {
        PredatorAccount.getAuthToken(getActivity(),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        mCollectionsPresenter.loadMoreCollections(s);
                    }
                });
    }

    @Override
    public void onLoadMore() {
        if (isNetworkAvailable(true)) {
            loadMoreCollections();
        }
        mCollectionsRecyclerAdapter.setNetworkStatus(isNetworkAvailable(false), getString(R.string.item_load_more_collection_error_desc));
    }
}
