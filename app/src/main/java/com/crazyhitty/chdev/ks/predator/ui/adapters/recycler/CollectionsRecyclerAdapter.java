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

package com.crazyhitty.chdev.ks.predator.ui.adapters.recycler;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.utils.MaterialColorPalette;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/20/2017 9:44 AM
 * Description: Unavailable
 */

public class CollectionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_COLLECTION = 1;
    public static final int VIEW_TYPE_LOAD_MORE = 2;
    private static final String TAG = "CollectionsRecyclerAdapter";
    private List<Collection> mCollections;
    private OnCollectionsLoadMoreRetryListener mOnCollectionsLoadMoreRetryListener;
    private OnItemClickListener mOnItemClickListener;

    private boolean mNetworkAvailable;
    private String mErrorMessage;
    private int mLastPosition = -1;
    private boolean mLoadMoreNotRequired = false;
    private HashMap<Integer, Integer> mColorHashMap = new HashMap<>();

    public CollectionsRecyclerAdapter() {
        mLoadMoreNotRequired = true;
    }

    public CollectionsRecyclerAdapter(List<Collection> collections,
                                      OnCollectionsLoadMoreRetryListener onCollectionsLoadMoreRetryListener) {
        mCollections = collections;
        mOnCollectionsLoadMoreRetryListener = onCollectionsLoadMoreRetryListener;
        mLoadMoreNotRequired = false;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setNetworkStatus(boolean status, String message) {
        mNetworkAvailable = status;
        mErrorMessage = message;
        notifyItemChanged(getItemCount() - 1);
    }

    public void updateDataset(List<Collection> collections, boolean forceReplace) {
        mCollections = collections;
        if (forceReplace) {
            mLastPosition = -1;
            notifyDataSetChanged();
        } else {
            int oldCount = mCollections.size();
            notifyItemRangeInserted(oldCount, mCollections.size() - oldCount);
        }
    }

    public void clear() {
        mCollections = null;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_COLLECTION:
                View collectionView = layoutInflater.inflate(R.layout.item_collection, parent, false);
                viewHolder = new CollectionViewHolder(collectionView);
                break;
            case VIEW_TYPE_LOAD_MORE:
                View loadMoreView = layoutInflater.inflate(R.layout.item_load_more_collection, parent, false);
                viewHolder = new LoadMoreViewHolder(loadMoreView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_COLLECTION:
                onBindCollectionViewHolder((CollectionViewHolder) holder, position);
                break;
            case VIEW_TYPE_LOAD_MORE:
                onBindLoadMoreViewHolder((LoadMoreViewHolder) holder, position);
                break;
        }
        manageAnimation(holder.itemView, position);
    }

    private void onBindCollectionViewHolder(final CollectionViewHolder collectionViewHolder, int position) {
        String title = mCollections.get(position).getName();
        String description = mCollections.get(position).getTitle();

        collectionViewHolder.txtTitle.setText(title);

        if (TextUtils.isEmpty(description)) {
            description = collectionViewHolder.itemView.getResources().getString(R.string.item_collection_no_desc_available);
        }
        collectionViewHolder.txtDescription.setText(description);

        if (!mColorHashMap.containsKey(position)) {
            if (PredatorSharedPreferences.getCurrentTheme(collectionViewHolder.itemView.getContext()) == PredatorSharedPreferences.THEME_TYPE.LIGHT) {
                mColorHashMap.put(position, MaterialColorPalette.getRandomColor("100"));
            } else {
                mColorHashMap.put(position, ContextCompat.getColor(collectionViewHolder.itemView.getContext(), R.color.color_primary_inverse));
            }
        }
        ((CardView) collectionViewHolder.itemView).setCardBackgroundColor(mColorHashMap.get(position));

        collectionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(collectionViewHolder.getAdapterPosition());
                }
            }
        });
    }

    private void onBindLoadMoreViewHolder(LoadMoreViewHolder loadMoreViewHolder, int position) {
        // All elements except progress bar will be visible if network is available, and vice versa.

        // Set span size to full width for this view
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) loadMoreViewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);

        loadMoreViewHolder.txtErrorTitle.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);
        loadMoreViewHolder.txtErrorDesc.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);
        loadMoreViewHolder.btnRetry.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);

        loadMoreViewHolder.progressBarLoading.setVisibility(mNetworkAvailable ? View.VISIBLE : View.GONE);

        loadMoreViewHolder.txtErrorDesc.setText(mErrorMessage);

        loadMoreViewHolder.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCollectionsLoadMoreRetryListener != null) {
                    mOnCollectionsLoadMoreRetryListener.onLoadMore();
                }
            }
        });
    }

    private void manageAnimation(View view, int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                    R.anim.anim_bottom_top_fade_in);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (mLoadMoreNotRequired) {
            return mCollections != null ?
                    mCollections.size() : 0;
        } else {
            // Add extra item, that will be shown in case of load more scenario.
            return mCollections != null ?
                    mCollections.size() + 1 : 0;
        }
    }

    public boolean isEmpty() {
        return mCollections == null || mCollections.isEmpty();
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique id associated with the item at available position.
     */
    public int getId(int position) {
        return mCollections.get(position).getId();
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique collection id associated with the item at available position.
     */
    public int getCollectionId(int position) {
        return mCollections.get(position).getCollectionId();
    }

    @Override
    public int getItemViewType(int position) {
        // If last position, then show "load more" view to the user.
        if (position == getItemCount() - 1 && !mLoadMoreNotRequired) {
            return VIEW_TYPE_LOAD_MORE;
        } else {
            return VIEW_TYPE_COLLECTION;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((RootViewHolder) holder).clearAnimation();
    }

    public interface OnCollectionsLoadMoreRetryListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class CollectionViewHolder extends RootViewHolder {
        @BindView(R.id.text_view_title)
        TextView txtTitle;
        @BindView(R.id.text_view_description)
        TextView txtDescription;

        public CollectionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LoadMoreViewHolder extends RootViewHolder {
        @BindView(R.id.text_view_error_title)
        TextView txtErrorTitle;
        @BindView(R.id.text_view_error_desc)
        TextView txtErrorDesc;
        @BindView(R.id.button_retry)
        Button btnRetry;
        @BindView(R.id.progress_bar_loading)
        ProgressBar progressBarLoading;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static class RootViewHolder extends RecyclerView.ViewHolder {
        public RootViewHolder(View itemView) {
            super(itemView);
        }

        protected void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
