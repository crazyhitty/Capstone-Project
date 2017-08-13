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

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/9/2017 10:06 AM
 * Description: Unavailable
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PostsRecyclerAdapter";

    private static final int VIEW_TYPE_LIST = 1;
    private static final int VIEW_TYPE_CARD = 2;
    private static final int VIEW_TYPE_SMALL_CARDS = 3;
    private static final int VIEW_TYPE_LARGE_CARDS = 4;
    private static final int VIEW_TYPE_LOAD_MORE = 98;

    private List<Post> mPosts;
    private TYPE mType;
    private OnPostsLoadMoreRetryListener mOnPostsLoadMoreRetryListener;
    private OnItemClickListener mOnItemClickListener;
    private HashMap<Integer, String> mDateHashMap = new HashMap<>();

    private int mLastPosition = -1;
    private boolean mNetworkAvailable;
    private String mErrorMessage;
    private boolean mLoadMoreNotRequired = false;

    /**
     * Initialize using this constructor if load more and dates functionalities are not required.
     *
     * @param posts List containing posts
     * @param type  Type of data to be displayed
     */
    public PostsRecyclerAdapter(List<Post> posts, TYPE type) {
        mPosts = posts;
        mType = type;
        mLoadMoreNotRequired = true;
    }

    /**
     * Constructor used to create a PostRecyclerAdapter with already defined dates. General use case
     * is when user want to see offline posts which doesn't support date wise pagination.
     *
     * @param posts                        List containing posts
     * @param type                         Type of data to be displayed
     * @param dateHashMap                  Hashmap containing where to show appropriate dates
     * @param onPostsLoadMoreRetryListener listener that will notify when to load more posts
     */
    public PostsRecyclerAdapter(List<Post> posts,
                                TYPE type,
                                HashMap<Integer, String> dateHashMap,
                                OnPostsLoadMoreRetryListener onPostsLoadMoreRetryListener) {
        mPosts = posts;
        mType = type;
        mDateHashMap = dateHashMap;
        mOnPostsLoadMoreRetryListener = onPostsLoadMoreRetryListener;
        mLoadMoreNotRequired = false;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setType(TYPE type) {
        mType = type;
    }

    /**
     * Update current dataset.
     *
     * @param posts
     * @param dateHashMap
     * @param forceReplace
     */
    public void updateDataset(List<Post> posts, HashMap<Integer, String> dateHashMap, boolean forceReplace) {
        mDateHashMap = dateHashMap;
        mPosts = posts;
        if (forceReplace) {
            mLastPosition = -1;
            notifyDataSetChanged();
        } else {
            int oldCount = mPosts.size();
            notifyItemRangeInserted(oldCount, mPosts.size() - oldCount);
        }
    }

    /**
     * Update current dataset.
     *
     * @param posts
     * @param forceReplace
     */
    public void updateDataset(List<Post> posts, boolean forceReplace) {
        mPosts = posts;
        if (forceReplace) {
            mLastPosition = -1;
        }
        notifyDataSetChanged();
    }

    public void setNetworkStatus(boolean status, String message) {
        mNetworkAvailable = status;
        mErrorMessage = message;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_LIST:
                View viewListType = layoutInflater.inflate(R.layout.item_list_post, parent, false);
                viewHolder = new ListItemViewHolder(viewListType);
                break;
            case VIEW_TYPE_CARD:
                View viewGridType = layoutInflater.inflate(R.layout.item_grid_post, parent, false);
                viewHolder = new GridItemViewHolder(viewGridType);
                break;
            case VIEW_TYPE_LOAD_MORE:
                View viewLoadMore = layoutInflater.inflate(R.layout.item_load_more_posts, parent, false);
                viewHolder = new LoadMoreViewHolder(viewLoadMore);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_LIST:
                onBindListItemViewHolder((ListItemViewHolder) holder, position);
                break;
            case VIEW_TYPE_CARD:
                onBindGridItemViewHolder((GridItemViewHolder) holder, position);
                break;
            case VIEW_TYPE_LOAD_MORE:
                onBindLoadMoreViewHolder((LoadMoreViewHolder) holder, position);
                break;
        }
        manageAnimation(holder.itemView, position);
    }

    private void onBindListItemViewHolder(final ListItemViewHolder listItemViewHolder, int position) {
        String title = mPosts.get(position).getName();
        String shortDesc = mPosts.get(position).getTagline();

        String postImageUrl = mPosts.get(position).getThumbnailImageUrl();
        postImageUrl = ImageUtils.getCustomPostThumbnailImageUrl(postImageUrl,
                ScreenUtils.dpToPxInt(listItemViewHolder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(listItemViewHolder.itemView.getContext(), 44));

        String date = mDateHashMap.get(position);
        boolean showDate = (date != null);

        listItemViewHolder.txtPostTitle.setText(title);
        listItemViewHolder.txtShortDesc.setText(shortDesc);
        listItemViewHolder.txtDate.setText(date);
        listItemViewHolder.txtDate.setVisibility(showDate ? View.VISIBLE : View.GONE);
        listItemViewHolder.imageViewPost.setImageURI(postImageUrl);

        listItemViewHolder.relativeLayoutPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(listItemViewHolder.getAdapterPosition());
                }
            }
        });
    }

    private void onBindGridItemViewHolder(final GridItemViewHolder gridItemViewHolder, int position) {
        String title = mPosts.get(position).getName();
        String shortDesc = mPosts.get(position).getTagline();

        String postImageUrl = mPosts.get(position).getThumbnailImageUrl();
        postImageUrl = ImageUtils.getCustomPostThumbnailImageUrl(postImageUrl,
                ScreenUtils.dpToPxInt(gridItemViewHolder.itemView.getContext(), 500),
                ScreenUtils.dpToPxInt(gridItemViewHolder.itemView.getContext(), 100));

        String date = mDateHashMap.get(position);
        boolean showDate = (date != null);

        gridItemViewHolder.txtPostTitle.setText(title);
        gridItemViewHolder.txtShortDesc.setText(shortDesc);
        gridItemViewHolder.txtDate.setText(date);
        gridItemViewHolder.txtDate.setVisibility(showDate ? View.VISIBLE : View.GONE);
        gridItemViewHolder.imageViewPost.setImageURI(postImageUrl);

        gridItemViewHolder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(gridItemViewHolder.getAdapterPosition());
                }
            }
        });
    }

    private void onBindLoadMoreViewHolder(LoadMoreViewHolder loadMoreViewHolder, int position) {
        // All elements except progress bar will be visible if network is available, and vice versa.

        loadMoreViewHolder.imgViewError.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);
        loadMoreViewHolder.txtErrorTitle.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);
        loadMoreViewHolder.txtErrorDesc.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);
        loadMoreViewHolder.btnRetry.setVisibility(mNetworkAvailable ? View.GONE : View.VISIBLE);

        loadMoreViewHolder.progressBarLoading.setVisibility(mNetworkAvailable ? View.VISIBLE : View.GONE);

        loadMoreViewHolder.txtErrorDesc.setText(mErrorMessage);

        loadMoreViewHolder.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPostsLoadMoreRetryListener.onLoadMore();
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
            return mPosts != null ?
                    mPosts.size() : 0;
        } else {
            // Add extra item, that will be shown in case of load more scenario.
            return mPosts != null ?
                    mPosts.size() + 1 : 0;
        }
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique id associated with the item at available position.
     */
    public int getId(int position) {
        return mPosts.get(position).getId();
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique post id associated with the item at available position.
     */
    public int getPostId(int position) {
        return mPosts.get(position).getPostId();
    }

    @Override
    public int getItemViewType(int position) {
        // If last position, then show "load more" view to the user.
        if (position == getItemCount() - 1 && !mLoadMoreNotRequired) {
            return VIEW_TYPE_LOAD_MORE;
        }
        switch (mType) {
            case LIST:
                return VIEW_TYPE_LIST;
            case CARD:
                return VIEW_TYPE_CARD;
            case SMALL_CARDS:
                return VIEW_TYPE_SMALL_CARDS;
            case LARGE_CARDS:
                return VIEW_TYPE_LARGE_CARDS;
            default:
                return VIEW_TYPE_LIST;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((RootViewHolder) holder).clearAnimation();
    }

    public enum TYPE {
        LIST,
        CARD,
        SMALL_CARDS,
        LARGE_CARDS
    }

    public interface OnPostsLoadMoreRetryListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ListItemViewHolder extends RootViewHolder {
        @BindView(R.id.text_view_date)
        TextView txtDate;
        @BindView(R.id.image_view_post)
        SimpleDraweeView imageViewPost;
        @BindView(R.id.text_view_post_title)
        TextView txtPostTitle;
        @BindView(R.id.text_view_post_short_desc)
        TextView txtShortDesc;
        @BindView(R.id.checkbox_bookmark)
        CheckBox checkBoxBookmark;
        @BindView(R.id.relative_layout_post)
        RelativeLayout relativeLayoutPost;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class GridItemViewHolder extends RootViewHolder {
        @BindView(R.id.text_view_date)
        TextView txtDate;
        @BindView(R.id.image_view_post)
        SimpleDraweeView imageViewPost;
        @BindView(R.id.text_view_post_title)
        TextView txtPostTitle;
        @BindView(R.id.text_view_post_short_desc)
        TextView txtShortDesc;
        @BindView(R.id.card_view_post)
        CardView cardViewPost;

        public GridItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LoadMoreViewHolder extends RootViewHolder {
        @BindView(R.id.image_view_error_icon)
        SimpleDraweeView imgViewError;
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
