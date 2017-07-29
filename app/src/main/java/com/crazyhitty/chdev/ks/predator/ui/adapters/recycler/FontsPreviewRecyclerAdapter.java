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

public class FontsPreviewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "FontsPreviewRecyclerAdapter";

    private static final int VIEW_TYPE_LIST = 1;
    private static final int VIEW_TYPE_SMALL_CARDS = 2;
    private static final int VIEW_TYPE_LARGE_CARDS = 3;

    private List<Post> mPosts;
    private TYPE mType;
    private HashMap<Integer, String> mDateHashMap = new HashMap<>();

    private int mLastPosition = -1;

    public FontsPreviewRecyclerAdapter(List<Post> posts,
                                       TYPE type,
                                       HashMap<Integer, String> dateHashMap) {
        mPosts = posts;
        mType = type;
        mDateHashMap = dateHashMap;
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
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_LIST:
                onBindListItemViewHolder((ListItemViewHolder) holder, position);
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

        int checkboxSelectVisibility = position == 0 ? View.VISIBLE : View.GONE;
        listItemViewHolder.checkBoxSelect.setVisibility(checkboxSelectVisibility);
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
        return mPosts != null ?
                mPosts.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
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

    public static class ListItemViewHolder extends RootViewHolder {
        @BindView(R.id.checkbox_select)
        CheckBox checkBoxSelect;
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

            relativeLayoutPost.setClickable(false);
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
