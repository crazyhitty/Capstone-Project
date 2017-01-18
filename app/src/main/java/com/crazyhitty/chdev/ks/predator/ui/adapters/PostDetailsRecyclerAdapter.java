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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.MediaItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.PostUserItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/18/2017 11:10 AM
 * Description: Unavailable
 */

public class PostDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_USERS = 2;
    private static final int VIEW_TYPE_MEDIA = 3;
    private static final int VIEW_TYPE_COMMENT = 4;
    private static final int VIEW_TYPE_LOADING = 5;

    private List<String> mTitles;
    private List<User> mUsers;
    private List<Media> mMedia;
    private List<Comment> mComments;
    private int mLastPosition = -1;
    private boolean mUnavailable;

    public PostDetailsRecyclerAdapter() {
    }

    public PostDetailsRecyclerAdapter(List<String> titles,
                                      List<User> users,
                                      List<Media> media,
                                      List<Comment> comments) {
        mTitles = titles;
        mUsers = users;
        mMedia = media;
        mComments = comments;
    }

    public void updateTitles(List<String> titles) {
        mTitles = titles;
        notifyDataSetChanged();
    }

    public void updateUsers(List<User> users) {
        mUsers = users;
        notifyItemChanged(1);
    }

    public void updateMedia(List<Media> media) {
        mMedia = media;
        notifyItemChanged(3);
    }

    public void updateComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public boolean isUnavailable() {
        return mUnavailable;
    }

    public void setUnavailable(boolean isUnavailable) {
        mUnavailable = isUnavailable;
        if (mUsers == null || mUsers.size() == 0) {
            notifyItemChanged(1);
        }
        if (mMedia == null || mMedia.size() == 0) {
            notifyItemChanged(3);
        }
        if (mComments == null || mComments.size() == 0) {
            notifyItemChanged(5);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                View titleView = layoutInflater.inflate(R.layout.item_post_details_title, parent, false);
                viewHolder = new TitleViewHolder(titleView);
                break;
            case VIEW_TYPE_USERS:
                View usersView = layoutInflater.inflate(R.layout.item_post_details_users, parent, false);
                viewHolder = new UsersViewHolder(usersView);
                break;
            case VIEW_TYPE_MEDIA:
                View mediaView = layoutInflater.inflate(R.layout.item_post_details_media, parent, false);
                viewHolder = new MediaViewHolder(mediaView);
                break;
            case VIEW_TYPE_COMMENT:
                View commentView = layoutInflater.inflate(R.layout.item_post_details_comment, parent, false);
                viewHolder = new CommentViewHolder(commentView);
                break;
            case VIEW_TYPE_LOADING:
                View loadingView = layoutInflater.inflate(R.layout.item_post_details_loading, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_TITLE:
                onBindTitleViewHolder((TitleViewHolder) holder, position);
                break;
            case VIEW_TYPE_USERS:
                onBindUsersViewHolder((UsersViewHolder) holder, position);
                break;
            case VIEW_TYPE_MEDIA:
                onBindMediaViewHolder((MediaViewHolder) holder, position);
                break;
            case VIEW_TYPE_COMMENT:
                onBindCommentViewHolder((CommentViewHolder) holder, position);
                break;
            case VIEW_TYPE_LOADING:
                onBindLoadingViewHolder((LoadingViewHolder) holder, position);
                break;
        }
    }

    private void onBindTitleViewHolder(TitleViewHolder titleViewHolder, int position) {
        String title = mTitles.get(position / 2);
        titleViewHolder.txtTitle.setText(title);
    }

    private void onBindUsersViewHolder(UsersViewHolder usersViewHolder, int position) {
        usersViewHolder.postUsersRecyclerAdapter.updateUsers(mUsers);
    }

    private void onBindMediaViewHolder(MediaViewHolder mediaViewHolder, int position) {
        mediaViewHolder.mediaRecyclerAdapter.updateMedia(mMedia);
    }

    private void onBindCommentViewHolder(CommentViewHolder commentViewHolder, int position) {
        Comment comment = mComments.get(position - 5);

        commentViewHolder.txtUsername.setText(comment.getUsername());
        commentViewHolder.txtUserHeadline.setText(comment.getUserHeadline());
        commentViewHolder.txtCommentBody.setText(Html.fromHtml(comment.getBody()));

        // Set uer image.
        String userImageUrl = comment.getUserImageThumbnailUrl();
        userImageUrl = ImageUtils.getCustomCommentUserImageThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(), 44));
        commentViewHolder.imgViewUser.setImageURI(userImageUrl);

        // Hide headline text if headline is empty.
        commentViewHolder.txtUserHeadline.setVisibility(TextUtils.isEmpty(comment.getUserHeadline()) ?
                View.GONE : View.VISIBLE);

        // Set extra internal padding if this was a child comment.
        int paddingLeftPx = ScreenUtils.dpToPxInt(commentViewHolder.itemView.getContext(),
                54.0f * comment.getChildSpaces());
        commentViewHolder.itemView.setPadding(paddingLeftPx,
                0,
                0,
                0);

        manageAnimation(commentViewHolder.itemView, position);
    }

    private void onBindLoadingViewHolder(LoadingViewHolder loadingViewHolder, int position) {
        int messageRes = R.string.post_details_loading_please_wait;
        int progressVisibility = View.VISIBLE;

        if (mUnavailable) {
            messageRes = R.string.post_details_unavailable;
            progressVisibility = View.GONE;
        }

        loadingViewHolder.txtMessage.setText(messageRes);
        loadingViewHolder.progressBarLoading.setVisibility(progressVisibility);
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
        int titleViewTypeCount = 3;
        int userViewTypeCount = 1;
        int mediaViewTypeCount = 1;
        int commentViewTypeCount = mComments != null ? mComments.size() : 1;
        return titleViewTypeCount +
                userViewTypeCount +
                mediaViewTypeCount +
                commentViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_TITLE;
            case 1:
                return mUsers != null ? VIEW_TYPE_USERS : VIEW_TYPE_LOADING;
            case 2:
                return VIEW_TYPE_TITLE;
            case 3:
                return mMedia != null ? VIEW_TYPE_MEDIA : VIEW_TYPE_LOADING;
            case 4:
                return VIEW_TYPE_TITLE;
            default:
                return mComments != null ? VIEW_TYPE_COMMENT : VIEW_TYPE_LOADING;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).clearAnimation();
        }
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_title)
        TextView txtTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_view_users)
        RecyclerView recyclerViewUsers;

        PostUsersRecyclerAdapter postUsersRecyclerAdapter;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Set up the recycler view properties.
            // Create a horizontal layout manager for recycler view.
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            recyclerViewUsers.setLayoutManager(layoutManager);

            // Add appropriate decorations to the recycler view items.
            recyclerViewUsers.addItemDecoration(new PostUserItemDecorator(itemView.getContext(), 16));

            // Create adapter that will power this recycler view.
            postUsersRecyclerAdapter = new PostUsersRecyclerAdapter(null);
            recyclerViewUsers.setAdapter(postUsersRecyclerAdapter);
        }
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_view_media)
        RecyclerView recyclerViewMedia;

        MediaRecyclerAdapter mediaRecyclerAdapter;

        public MediaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Set up the recycler view properties.
            // Create a horizontal layout manager for recycler view.
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            recyclerViewMedia.setLayoutManager(layoutManager);

            // Add appropriate decorations to the recycler view items.
            recyclerViewMedia.addItemDecoration(new MediaItemDecorator(itemView.getContext(), 16));

            // Create adapter that will power this recycler view.
            mediaRecyclerAdapter = new MediaRecyclerAdapter(null);
            recyclerViewMedia.setAdapter(mediaRecyclerAdapter);
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

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_message)
        TextView txtMessage;
        @BindView(R.id.progress_bar_loading)
        ProgressBar progressBarLoading;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
