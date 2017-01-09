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
import android.widget.CheckBox;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.RecyclerViewItemAnimations;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;

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
    private static final int VIEW_TYPE_DATE_CHANGE = 99;

    private boolean mAnimationStatus = true;
    private TYPE mType;
    private WeakReference<Cursor> mCursorWeakReference;

    /**
     * Constructor used to create a PostRecyclerAdapter.
     *
     * @param cursor Cursor containing database values
     * @param type   Type of data to be displayed
     */
    public PostsRecyclerAdapter(Cursor cursor, TYPE type) {
        mCursorWeakReference = new WeakReference<Cursor>(cursor);
        mType = type;
    }

    public void setType(TYPE type) {
        mType = type;
    }

    public void setAnimationStatus(boolean animationStatus) {
        mAnimationStatus = animationStatus;
    }

    public void updateCursor(Cursor cursor) {
        int oldCursorSize = mCursorWeakReference.get().getCount();
        int newCursorSize = cursor.getCount();
        mCursorWeakReference = new WeakReference<Cursor>(cursor);
        /*notifyItemRangeInserted(oldCursorSize, newCursorSize-oldCursorSize);*/
        for (int i = 0; i < (newCursorSize - oldCursorSize); i++) {
            notifyItemInserted(getItemCount() - 2);
        }
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
    }

    private void onBindListItemViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        mCursorWeakReference.get().moveToPosition(position);

        String title = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_NAME);
        String shortDesc = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String postImageUrl = CursorUtils.getString(mCursorWeakReference.get(),
                PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL);

        listItemViewHolder.txtPostTitle.setText(title);
        listItemViewHolder.txtShortDesc.setText(shortDesc);
        listItemViewHolder.imageViewPost.setImageURI(postImageUrl);

        if (mAnimationStatus) {
            RecyclerViewItemAnimations.setListTranslateYAnim(listItemViewHolder.itemView,
                    50);
        }
    }

    private void onBindLoadMoreViewHolder(LoadMoreViewHolder loadMoreViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        // Add extra item, that will be shown in case of load more scenario.
        return mCursorWeakReference.get().getCount() + 1;
    }

    /**
     * @return Returns the cursor size.
     */
    public int getCount() {
        return mCursorWeakReference.get().getCount();
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

    public enum TYPE {
        LIST,
        SMALL_CARDS,
        LARGE_CARDS
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_post)
        SimpleDraweeView imageViewPost;
        @BindView(R.id.text_view_post_title)
        TextView txtPostTitle;
        @BindView(R.id.text_view_post_short_desc)
        TextView txtShortDesc;
        @BindView(R.id.checkbox_bookmark)
        CheckBox checkBoxBookmark;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }
}
