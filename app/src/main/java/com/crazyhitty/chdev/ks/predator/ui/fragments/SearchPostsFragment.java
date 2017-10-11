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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.PostsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.CollectionPostItemDecorator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/10/17 3:09 PM
 * Description: Unavailable
 */

public class SearchPostsFragment extends BaseSupportFragment {
    private static final String TAG = "SearchPostsFragment";

    @BindView(R.id.recycler_view_posts)
    RecyclerView recyclerViewPosts;
    @BindView(R.id.linear_layout_error)
    LinearLayout linearLayoutError;
    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;

    private PostsRecyclerAdapter mPostsRecyclerAdapter;

    public static SearchPostsFragment newInstance() {
        Bundle args = new Bundle();
        SearchPostsFragment fragment = new SearchPostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);
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
        CollectionPostItemDecorator listItemDecorator = new CollectionPostItemDecorator(getContext().getApplicationContext(), 72);
        recyclerViewPosts.addItemDecoration(listItemDecorator);

        mPostsRecyclerAdapter = new PostsRecyclerAdapter(null,
                PostsRecyclerAdapter.TYPE.LIST);
        recyclerViewPosts.setAdapter(mPostsRecyclerAdapter);

        mPostsRecyclerAdapter.setOnItemClickListener(new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PostDetailsActivity.startActivity(getContext(),
                        mPostsRecyclerAdapter.getPostId(position),
                        PostDetails.fromPost(mPostsRecyclerAdapter.getPost(position)));
            }
        });
    }

    public void updatePosts(List<Post> posts) {
        linearLayoutError.setVisibility(View.GONE);
        mPostsRecyclerAdapter.updateDataset(posts, true);
    }

    public void noPostsAvailable() {
        linearLayoutError.setVisibility(View.VISIBLE);
        mPostsRecyclerAdapter.clear();
    }

    public void networkUnavailable() {
        if (mPostsRecyclerAdapter.isEmpty()) {
            linearLayoutError.setVisibility(View.VISIBLE);
            txtMessage.setText(R.string.fragment_search_posts_network_error);
        } else {
            showShortToast(R.string.not_connected_to_network_err);
        }
    }

    public void searchingStarted() {
        if (mPostsRecyclerAdapter.isEmpty()) {
            linearLayoutError.setVisibility(View.GONE);
            progressBarLoading.setVisibility(View.VISIBLE);
        }
    }

    public void searchingStopped() {
        progressBarLoading.setVisibility(View.GONE);
    }
}
