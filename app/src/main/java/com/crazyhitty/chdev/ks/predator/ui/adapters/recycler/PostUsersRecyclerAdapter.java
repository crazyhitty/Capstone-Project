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
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/14/2017 8:58 PM
 * Description: Unavailable
 */

public class PostUsersRecyclerAdapter extends RecyclerView.Adapter<PostUsersRecyclerAdapter.UsersViewHolder> {
    private List<User> mUsers;

    public PostUsersRecyclerAdapter(List<User> users) {
        mUsers = users;
    }

    public void updateUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_post_user, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        String name = mUsers.get(position).getName();

        String userImageUrl = mUsers.get(position).getThumbnail();
        userImageUrl = ImageUtils.getCustomUserThumbnailUrl(userImageUrl,
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44),
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 44));

        holder.txtUserName.setText(name);
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
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_user_name)
        TextView txtUserName;
        @BindView(R.id.text_view_user_type)
        TextView txtUserType;
        @BindView(R.id.image_view_user)
        SimpleDraweeView imgViewUser;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
