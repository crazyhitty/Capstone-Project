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

import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.ui.dialog.CommentUserPreviewDialog;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/11/2017 11:49 AM
 * Description: Unavailable
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PARENT_COMMENT = 1;
    private static final int VIEW_TYPE_CHILD_COMMENT = 2;
    private static final int VIEW_TYPE_LOAD_MORE = 98;

    private List<Comment> mComments;
    private String mPostTitle;
    private int mLastPosition = -1;
    private boolean mNetworkAvailable;
    private String mErrorMessage;
    private boolean mLoadMoreRequired = false;

    private OnCommentsLoadMoreRetryListener mOnCommentsLoadMoreRetryListener;

    public CommentsRecyclerAdapter(@Nullable List<Comment> comments,
                                   @Nullable String postTitle,
                                   @Nullable OnCommentsLoadMoreRetryListener onCommentsLoadMoreRetryListener) {
        mComments = comments;
        mPostTitle = postTitle;
        mOnCommentsLoadMoreRetryListener = onCommentsLoadMoreRetryListener;
    }

    public void updateComments(@Nullable List<Comment> comments, @Nullable String postTitle) {
        mLastPosition = -1;
        mComments = comments;
        mPostTitle = postTitle;
        notifyDataSetChanged();
    }

    public void updateComments(@Nullable List<Comment> comments,
                               @Nullable String postTitle,
                               boolean loadMoreRequired) {
        mLastPosition = -1;
        mComments = comments;
        mPostTitle = postTitle;
        mLoadMoreRequired = loadMoreRequired;
        notifyDataSetChanged();
    }

    public void setNetworkStatus(boolean status, String message) {
        mNetworkAvailable = status;
        mErrorMessage = message;
        if (!isEmpty() && mLoadMoreRequired) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_PARENT_COMMENT:
            case VIEW_TYPE_CHILD_COMMENT:
                View commentView = layoutInflater.inflate(R.layout.item_post_details_comment, parent, false);
                viewHolder = new CommentViewHolder(commentView);
                break;
            case VIEW_TYPE_LOAD_MORE:
                View viewLoadMore = layoutInflater.inflate(R.layout.item_load_more_posts, parent, false);
                viewHolder = new LoadMoreViewHolder(viewLoadMore);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_PARENT_COMMENT:
            case VIEW_TYPE_CHILD_COMMENT:
                onBindCommentsViewHolder((CommentViewHolder) holder, position);
                break;
            case VIEW_TYPE_LOAD_MORE:
                onBindLoadMoreViewHolder((LoadMoreViewHolder) holder, position);
                break;
        }
        manageAnimation(holder.itemView, position);
    }

    private void onBindCommentsViewHolder(final CommentViewHolder commentViewHolder, int position) {
        commentViewHolder.txtUsername.setText(mComments.get(position).getUsername());
        commentViewHolder.txtUserHeadline.setText(mComments.get(position).getUserHeadline());
        commentViewHolder.txtCommentBody.setText(Html.fromHtml(mComments.get(position).getBody()));

        // Set uer image.
        String userImageUrl = mComments.get(position).getUserImageThumbnailUrl();
        userImageUrl = ImageUtils.getCustomCommentUserImageThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(), 44));
        commentViewHolder.imgViewUser.setImageURI(userImageUrl);

        // Hide headline text if headline is empty.
        commentViewHolder.txtUserHeadline.setVisibility(TextUtils.isEmpty(mComments.get(position).getUserHeadline()) ?
                View.GONE : View.VISIBLE);

        // Set extra details.
        String extraDetails = String.format("%s \u2022 %s",
                commentViewHolder.getQuantityString(R.plurals.item_post_details_comment_votes,
                        mComments.get(position).getVotes(),
                        mComments.get(position).getVotes()),
                commentViewHolder.getString(getStringResourceIdForTimeUnit(mComments.get(position).getTimeUnit()),
                        mComments.get(position).getTimeAgo()));
        commentViewHolder.txtCommentExtraDetails.setText(extraDetails);

        commentViewHolder.imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUserPreviewDialog.show(commentViewHolder.itemView.getContext(),
                        mComments.get(commentViewHolder.getAdapterPosition()),
                        mPostTitle);
            }
        });

        // Set extra internal padding if this was a child comment.
        int paddingLeftPx = ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(),
                54.0f * mComments.get(position).getChildSpaces());
        commentViewHolder.itemView.setPadding(paddingLeftPx,
                0,
                0,
                0);
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
                mOnCommentsLoadMoreRetryListener.onLoadMore();
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
        return mComments != null ? mComments.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && mLoadMoreRequired) {
            return VIEW_TYPE_LOAD_MORE;
        }
        switch (mComments.get(position).getChildSpaces()) {
            case 0:
                return VIEW_TYPE_PARENT_COMMENT;
            case 1:
                return VIEW_TYPE_CHILD_COMMENT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((RootViewHolder) holder).clearAnimation();
    }

    public boolean isEmpty() {
        return mComments == null || mComments.isEmpty();
    }

    public boolean canLoadMore() {
        return mLoadMoreRequired;
    }

    private int getStringResourceIdForTimeUnit(Comment.TIME_UNIT timeUnit) {
        switch (timeUnit){
            case SECOND_AGO:
                return R.string.item_post_details_comment_second_ago;
            case SECOND_AGO_PLURAL:
                return R.string.item_post_details_comment_second_ago_plural;
            case MINUTE_AGO:
                return R.string.item_post_details_comment_minute_ago;
            case MINUTE_AGO_PLURAL:
                return R.string.item_post_details_comment_minute_ago_plural;
            case HOUR_AGO:
                return R.string.item_post_details_comment_hour_ago;
            case HOUR_AGO_PLURAL:
                return R.string.item_post_details_comment_hour_ago_plural;
            case DAY_AGO:
                return R.string.item_post_details_comment_day_ago;
            case DAY_AGO_PLURAL:
                return R.string.item_post_details_comment_day_ago_plural;
            default:
                throw new IllegalArgumentException("Provided TIME_UNIT is not valid.");
        }
    }

    public static class CommentViewHolder extends RootViewHolder {
        @BindView(R.id.image_view_user)
        SimpleDraweeView imgViewUser;
        @BindView(R.id.text_view_user_name)
        TextView txtUsername;
        @BindView(R.id.text_view_user_headline)
        TextView txtUserHeadline;
        @BindView(R.id.text_view_comment_body)
        TextView txtCommentBody;
        @BindView(R.id.text_view_comment_extra_details)
        TextView txtCommentExtraDetails;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            txtCommentBody.setMovementMethod(LinkMovementMethod.getInstance());
        }

        protected void clearAnimation() {
            itemView.clearAnimation();
        }

        public String getQuantityString(@PluralsRes int resId, int quantity, Object... args) {
            return itemView.getResources()
                    .getQuantityString(R.plurals.item_post_details_comment_votes,
                            quantity,
                            args);
        }

        public String getString(@StringRes int resId, Object... args) {
            return itemView.getContext()
                    .getString(resId, args);
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

    public interface OnCommentsLoadMoreRetryListener {
        void onLoadMore();
    }
}
