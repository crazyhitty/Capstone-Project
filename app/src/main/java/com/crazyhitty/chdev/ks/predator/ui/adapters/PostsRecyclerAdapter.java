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

package com.crazyhitty.chdev.ks.predator.ui.adapters;

import android.database.Cursor;
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
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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
    private static final int VIEW_TYPE_SMALL_CARDS = 2;
    private static final int VIEW_TYPE_LARGE_CARDS = 3;
    private static final int VIEW_TYPE_LOAD_MORE = 98;

    private TYPE mType;
    private WeakReference<Cursor> mCursorWeakReference;
    private OnPostsLoadMoreRetryListener mOnPostsLoadMoreRetryListener;
    private OnItemClickListener mOnItemClickListener;
    private int mLastPosition = -1;
    private HashMap<Integer, String> mDateHashMap = new HashMap<>();
    private boolean mNetworkAvailable;
    private String mErrorMessage;

    /**
     * Constructor used to create a PostRecyclerAdapter with already defined dates. General use case
     * is when user want to see offline posts which doesn't support date wise pagination.
     *
     * @param cursor                       Cursor containing database values
     * @param type                         Type of data to be displayed
     * @param dateHashMap                  Hashmap containing where to show appropriate dates
     * @param onPostsLoadMoreRetryListener listener that will notify when to load more posts
     */
    public PostsRecyclerAdapter(Cursor cursor,
                                TYPE type,
                                HashMap<Integer, String> dateHashMap,
                                OnPostsLoadMoreRetryListener onPostsLoadMoreRetryListener) {
        mCursorWeakReference = new WeakReference<Cursor>(cursor);
        mType = type;
        mDateHashMap = dateHashMap;
        mOnPostsLoadMoreRetryListener = onPostsLoadMoreRetryListener;
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
     * @param cursor
     * @param dateHashMap
     * @param forceReplace
     */
    public void updateCursor(Cursor cursor, HashMap<Integer, String> dateHashMap, boolean forceReplace) {
        int oldCursorSize = mCursorWeakReference.get() != null ?
                mCursorWeakReference.get().getCount() : 0;
        int newCursorSize = cursor.getCount();
        mCursorWeakReference = new WeakReference<Cursor>(cursor);
        mDateHashMap = dateHashMap;
        if (forceReplace) {
            mLastPosition = -1;
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(oldCursorSize, newCursorSize - oldCursorSize);
        }
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
            case VIEW_TYPE_LOAD_MORE:
                View viewLoadMore = layoutInflater.inflate(R.layout.item_load_more, parent, false);
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
            case VIEW_TYPE_LOAD_MORE:
                onBindLoadMoreViewHolder((LoadMoreViewHolder) holder, position);
                break;
        }
        manageAnimation(holder.itemView, position);
    }

    private void onBindListItemViewHolder(final ListItemViewHolder listItemViewHolder, int position) {
        mCursorWeakReference.get().moveToPosition(position);

        String title = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_NAME);
        String shortDesc = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_TAGLINE);

        String postImageUrl = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL);
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
        // Add extra item, that will be shown in case of load more scenario.
        return mCursorWeakReference.get() != null ?
                mCursorWeakReference.get().getCount() + 1 : 0;
    }

    /**
     * @return Returns the cursor size.
     */
    public int getCount() {
        return mCursorWeakReference.get() != null ?
                mCursorWeakReference.get().getCount() : 0;
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique id associated with the item at available position.
     */
    public int getId(int position) {
        mCursorWeakReference.get().moveToPosition(position);
        return CursorUtils.getInt(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_ID);
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique post id associated with the item at available position.
     */
    public int getPostId(int position) {
        mCursorWeakReference.get().moveToPosition(position);
        return CursorUtils.getInt(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_POST_ID);
    }

    @Override
    public int getItemViewType(int position) {
        // If last position, then show "load more" view to the user.
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE;
        }
        switch (mType) {
            case LIST:
                return VIEW_TYPE_LIST;
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
