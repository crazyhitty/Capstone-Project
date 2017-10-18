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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.ui.activities.CollectionDetailsActivity;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.CollectionsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.CollectionItemDecorator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/10/17 3:09 PM
 * Description: Unavailable
 */

public class SearchCollectionsFragment extends BaseSupportFragment {
    private static final String TAG = "SearchCollectionsFragment";

    @BindView(R.id.recycler_view_collections)
    RecyclerView recyclerViewCollections;
    @BindView(R.id.linear_layout_error)
    LinearLayout linearLayoutError;
    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;

    private CollectionsRecyclerAdapter mCollectionsRecyclerAdapter;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    private int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;

    public static SearchCollectionsFragment newInstance() {
        Bundle args = new Bundle();
        SearchCollectionsFragment fragment = new SearchCollectionsFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_collections, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCollectionsRecyclerViewProperties();
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
                        linearLayoutError.getVisibility() == View.GONE &&
                        progressBarLoading.getVisibility() == View.GONE &&
                        isNetworkAvailable(false) &&
                        mCollectionsRecyclerAdapter.canLoadMore()) {
                    mOnFragmentInteractionListener.loadMoreCollections();
                }
            }
        });

        // Create adapter that will power this recycler view.
        mCollectionsRecyclerAdapter = new CollectionsRecyclerAdapter(null, new CollectionsRecyclerAdapter.OnCollectionsLoadMoreRetryListener() {
            @Override
            public void onLoadMore() {
                if (isNetworkAvailable(true)) {
                    mOnFragmentInteractionListener.loadMoreCollections();
                }
            }
        });

        recyclerViewCollections.setAdapter(mCollectionsRecyclerAdapter);

        mCollectionsRecyclerAdapter.setOnItemClickListener(new CollectionsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CollectionDetailsActivity.startActivity(getContext(),
                        mCollectionsRecyclerAdapter.getCollectionId(position),
                        mCollectionsRecyclerAdapter.getCollection(position));
            }
        });

        mCollectionsRecyclerAdapter.setNetworkStatus(isNetworkAvailable(false), getString(R.string.item_load_more_posts_error_desc));
    }

    public void searchInit() {
        linearLayoutError.setVisibility(View.VISIBLE);
        txtMessage.setText(R.string.fragment_search_collections_search_something);
        mCollectionsRecyclerAdapter.clear();
    }

    public void updateCollections(List<Collection> collections, boolean loadMore) {
        linearLayoutError.setVisibility(View.GONE);
        mCollectionsRecyclerAdapter.setLoadMore(true);
        if (loadMore) {
            mCollectionsRecyclerAdapter.addDataset(collections);
        } else {
            mCollectionsRecyclerAdapter.updateDataset(collections, true);
        }
    }

    public void noCollectionsAvailable(boolean loadMore) {
        if (loadMore) {
            mCollectionsRecyclerAdapter.setLoadMore(false);
            mCollectionsRecyclerAdapter.removeLoadingView();
            showLongToast(R.string.fragment_search_collections_max_limit_error);
        } else {
            linearLayoutError.setVisibility(View.VISIBLE);
            txtMessage.setText(R.string.fragment_search_collections_unavailable);
            mCollectionsRecyclerAdapter.clear();
        }
    }

    public void onNetworkConnectivityChanged(boolean status) {
        mCollectionsRecyclerAdapter.setNetworkStatus(status, getString(R.string.item_load_more_collection_error_desc));
        if (!status && mCollectionsRecyclerAdapter.isEmpty()) {
            linearLayoutError.setVisibility(View.VISIBLE);
            txtMessage.setText(R.string.fragment_search_collections_network_error);
        } else if (mCollectionsRecyclerAdapter.isEmpty()) {
            linearLayoutError.setVisibility(View.VISIBLE);
            txtMessage.setText(R.string.fragment_search_collections_search_something);
        }
    }

    public void searchingStarted() {
        mCollectionsRecyclerAdapter.clear();
        linearLayoutError.setVisibility(View.GONE);
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    public void searchingStopped() {
        progressBarLoading.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        void loadMoreCollections();
    }
}
