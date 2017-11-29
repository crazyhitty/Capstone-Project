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
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.comments.CommentsContract;
import com.crazyhitty.chdev.ks.predator.core.comments.CommentsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.CommentsEvent;
import com.crazyhitty.chdev.ks.predator.events.NetworkEvent;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.CommentsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.views.LoadingView;
import com.crazyhitty.chdev.ks.predator.utils.CommentItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/10/2017 5:02 PM
 * Description: Unavailable
 */

public class CommentsFragment extends BaseSupportFragment implements CommentsContract.View {
    private static final String TAG = "CommentsFragment";

    private static final String ARG_POST_ID = "post_id";

    @BindView(R.id.recycler_view_comments)
    RecyclerView recyclerViewComments;
    @BindView(R.id.linear_layout_loading)
    LinearLayout linearLayoutLoading;
    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;

    private CommentsContract.Presenter mCommentsPresenter;

    private CommentsRecyclerAdapter mCommentsRecyclerAdapter;

    public static CommentsFragment newInstance(int postId) {
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ID, postId);
        CommentsFragment commentsFragment = new CommentsFragment();
        commentsFragment.setArguments(args);
        return commentsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new CommentsPresenter(this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecyclerViewProperties();
        init();
    }

    private void setRecyclerViewProperties() {
        // Create a list type layout manager.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewComments.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        final CommentItemDecorator commentItemDecorator = new CommentItemDecorator(getContext());
        recyclerViewComments.addItemDecoration(commentItemDecorator);

        mCommentsRecyclerAdapter = new CommentsRecyclerAdapter(null,
                null,
                new CommentsRecyclerAdapter.OnCommentsLoadMoreRetryListener() {
            @Override
            public void onLoadMore() {
                if (isNetworkAvailable(true)) {
                    loadMoreComments();
                } else {
                    mCommentsRecyclerAdapter.setNetworkStatus(isNetworkAvailable(false),
                            getString(R.string.item_load_more_posts_error_desc));
                }
            }
        });

        // Add scroll listener that will manage scroll down to load more functionality.
        recyclerViewComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the last item is on screen, if yes then start loading more comments.
                if (mCommentsRecyclerAdapter.getItemCount() != 0 &&
                        layoutManager.findLastVisibleItemPosition() ==
                                mCommentsRecyclerAdapter.getItemCount() - 1 &&
                        isNetworkAvailable(false)) {
                    loadMoreComments();
                }
            }
        });

        recyclerViewComments.setAdapter(mCommentsRecyclerAdapter);
    }

    private void init() {

    }

    public void updateComments(CommentsEvent commentsEvent, String postTitle) {
        /*if (commentsEvent.getComments() != null && commentsEvent.getComments().size() != 0) {
            linearLayoutLoading.setVisibility(View.GONE);
            mCommentsRecyclerAdapter.updateComments(commentsEvent.getComments(), postTitle);
        } else if (mCommentsRecyclerAdapter.getItemCount() == 0) {
            txtMessage.setText(R.string.fragment_comments_unavailable);
            progressBarLoading.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void showLoading() {
        Logger.d(TAG, "show loading");
        linearLayoutLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        Logger.e(TAG, "hide loading");
        linearLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void showComments(@NotNull List<? extends Comment> comments) {
        Logger.d(TAG, "Comments size: " + comments);
    }

    @Override
    public void commentsUnavailable() {

    }

    @Override
    public void setPresenter(CommentsContract.Presenter presenter) {
        mCommentsPresenter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkConnectivityChanged(NetworkEvent networkEvent) {
        if (mCommentsRecyclerAdapter != null && mCommentsRecyclerAdapter.getItemCount() != 0) {
            mCommentsRecyclerAdapter.setNetworkStatus(networkEvent.isConnected(), getString(R.string.item_load_more_posts_error_desc));
        }
    }

    private void loadOnlineComments() {
        PredatorAccount.getAuthToken(getActivity(),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mCommentsPresenter.fetchOnlineComments(s,
                                getArguments().getInt(ARG_POST_ID));
                    }
                });
    }

    private void loadMoreComments() {
        PredatorAccount.getAuthToken(getActivity(),
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getContext().getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mCommentsPresenter.loadMoreOnlineComments(s,
                                getArguments().getInt(ARG_POST_ID));
                    }
                });
    }
}
