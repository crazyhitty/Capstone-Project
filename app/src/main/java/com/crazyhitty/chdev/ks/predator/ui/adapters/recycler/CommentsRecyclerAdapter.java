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
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Comment;
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

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder> {
    public static final int VIEW_TYPE_PARENT_COMMENT = 1;
    public static final int VIEW_TYPE_CHILD_COMMENT = 2;

    private List<Comment> mComments;
    private int mLastPosition = -1;

    public CommentsRecyclerAdapter(@Nullable List<Comment> comments) {
        mComments = comments;
    }

    public void updateComments(@Nullable List<Comment> comments) {
        mLastPosition = -1;
        mComments = comments;
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_post_details_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.txtUsername.setText(mComments.get(position).getUsername());
        holder.txtUserHeadline.setText(mComments.get(position).getUserHeadline());
        holder.txtCommentBody.setText(Html.fromHtml(mComments.get(position).getBody()));

        // Set uer image.
        String userImageUrl = mComments.get(position).getUserImageThumbnailUrl();
        userImageUrl = ImageUtils.getCustomCommentUserImageThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44));
        holder.imgViewUser.setImageURI(userImageUrl);

        // Hide headline text if headline is empty.
        holder.txtUserHeadline.setVisibility(TextUtils.isEmpty(mComments.get(position).getUserHeadline()) ?
                View.GONE : View.VISIBLE);

        // Set extra details.
        String extraDetails = String.format("%s \u2022 %s",
                holder.getQuantityString(R.plurals.item_post_details_comment_votes,
                        mComments.get(position).getVotes(),
                        mComments.get(position).getVotes()),
                holder.getString(getStringResourceIdForTimeUnit(mComments.get(position).getTimeUnit()),
                        mComments.get(position).getTimeAgo()));
        holder.txtCommentExtraDetails.setText(extraDetails);

        // Set extra internal padding if this was a child comment.
        int paddingLeftPx = ScreenUtils.dpToPxInt(holder.itemView.getContext(),
                54.0f * mComments.get(position).getChildSpaces());
        holder.itemView.setPadding(paddingLeftPx,
                0,
                0,
                0);

        manageAnimation(holder.itemView, position);
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
        switch (mComments.get(position).getChildSpaces()) {
            case 0:
                return VIEW_TYPE_PARENT_COMMENT;
            case 1:
                return VIEW_TYPE_CHILD_COMMENT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onViewDetachedFromWindow(CommentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
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

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
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
}
