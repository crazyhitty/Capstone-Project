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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.models.UserFallback;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/11/2017 4:12 PM
 * Description: Unavailable
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {
    private List<User> mUsers;
    private OnUserItemClickListener mOnUserItemClickListener;
    private int mLastPosition = -1;

    public UsersRecyclerAdapter(@Nullable List<User> users, @NonNull OnUserItemClickListener onUserItemClickListener) {
        mUsers = users;
        mOnUserItemClickListener = onUserItemClickListener;
    }

    public void updateUsers(@Nullable List<User> users) {
        mLastPosition = -1;
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_post_details_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        String name = mUsers.get(position).getName();
        String username = holder.itemView
                .getResources()
                .getString(R.string.item_post_user_name_alternative, mUsers.get(position).getUsername());

        String userImageUrl = mUsers.get(position).getThumbnail();
        userImageUrl = ImageUtils.getCustomUserThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44));

        holder.txtUserName.setText(name);
        holder.txtUserNameAlternative.setText(username);
        holder.imgViewUser.setImageURI(userImageUrl);

        int userTypeRes = 0;

        switch (mUsers.get(position).getType()) {
            case BOTH:
                userTypeRes = R.string.item_post_user_both;
                break;
            case HUNTER:
                userTypeRes = R.string.item_post_user_hunter;
                break;
            case MAKER:
                userTypeRes = R.string.item_post_user_maker;
                break;
            case UPVOTER:
                userTypeRes = R.string.item_post_user_voted;
        }

        holder.txtUserType.setText(userTypeRes);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnUserItemClickListener != null) {
                    mOnUserItemClickListener.onUserClick(holder.getAdapterPosition());
                }
            }
        });

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
        return mUsers != null ? mUsers.size() : 0;
    }

    /**
     * @param position Current position of the element.
     * @return Returns the unique user id associated with the item at available position.
     */
    public int getUserId(int position) {
        return mUsers.get(position).getUserId();
    }

    /**
     * @param position Current position of the element
     * @return Returns the unique user fallback associated with the item at available position.
     */
    public UserFallback getUserFallback(int position) {
        UserFallback userFallback = new UserFallback();
        userFallback.setName(mUsers.get(position).getName());
        userFallback.setUsername(mUsers.get(position).getUsername());
        userFallback.setHeadline(mUsers.get(position).getHeadline());
        userFallback.setThumbnail(mUsers.get(position).getThumbnail());
        userFallback.setWebsiteUrl(mUsers.get(position).getWebsiteUrl());
        userFallback.setImage(mUsers.get(position).getImage());
        return userFallback;
    }

    @Override
    public void onViewDetachedFromWindow(UserViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    public interface OnUserItemClickListener {
        void onUserClick(int position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_name)
        TextView txtUserName;
        @BindView(R.id.text_view_user_type)
        TextView txtUserType;
        @BindView(R.id.text_view_username)
        TextView txtUserNameAlternative;
        @BindView(R.id.image_view_user)
        SimpleDraweeView imgViewUser;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
