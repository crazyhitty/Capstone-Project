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

package com.crazyhitty.chdev.ks.predator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.events.UserPostsEvent;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.PostsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.UserProfilePostItemDecorator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/16/2017 4:55 PM
 * Description: Unavailable
 */

public class UserProfilePostsFragment extends BaseSupportFragment {
    public static final String POSTS_TYPE_UPVOTES = "UPVOTES";
    public static final String POSTS_TYPE_SUBMITTED = "SUBMITTED";
    public static final String POSTS_TYPE_MADE = "MADE";
    private static final String TAG = "UserProfilePostsFragment";
    private static final String ARG_POSTS_TYPE = "posts_type";
    @BindView(R.id.recycler_view_posts)
    RecyclerView recyclerViewPosts;
    @BindView(R.id.linear_layout_loading)
    LinearLayout linearLayoutLoading;
    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;

    private PostsRecyclerAdapter mPostsRecyclerAdapter;

    public static UserProfilePostsFragment newInstance(String postType) {
        Bundle args = new Bundle();
        args.putString(ARG_POSTS_TYPE, postType);
        UserProfilePostsFragment fragment = new UserProfilePostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_posts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecyclerViewProperties();
    }

    private void setRecyclerViewProperties() {
        // Create a list type layout manager.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        final UserProfilePostItemDecorator userProfilePostItemDecorator = new UserProfilePostItemDecorator(getContext(), 72);
        recyclerViewPosts.addItemDecoration(userProfilePostItemDecorator);

        mPostsRecyclerAdapter = new PostsRecyclerAdapter(null, PostsRecyclerAdapter.TYPE.LIST);
        recyclerViewPosts.setAdapter(mPostsRecyclerAdapter);

        mPostsRecyclerAdapter.setOnItemClickListener(new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PostDetailsActivity.startActivity(getContext(), mPostsRecyclerAdapter.getPostId(position));
            }
        });
    }

    public void updateUserPosts(UserPostsEvent userPostsEvent) {
        if (TextUtils.equals(getArguments().getString(ARG_POSTS_TYPE),
                userPostsEvent.getPostType().toString())) {
            if (userPostsEvent.getPosts() != null && userPostsEvent.getPosts().size() != 0) {
                linearLayoutLoading.setVisibility(View.GONE);
                mPostsRecyclerAdapter.updateDataset(userPostsEvent.getPosts(), userPostsEvent.isForceReplace());
            } else if (mPostsRecyclerAdapter.getItemCount() == 0) {
                txtMessage.setText(R.string.fragment_user_profile_posts_unavailable);
                progressBarLoading.setVisibility(View.GONE);
            }
        }
    }
}
