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

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.postDetails.PostDetailsContract;
import com.crazyhitty.chdev.ks.predator.core.postDetails.PostDetailsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.NetworkEvent;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.PostDetailsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 5:32 PM
 * Description: Unavailable
 */

public class PostDetailsFragment extends BaseSupportFragment implements PostDetailsContract.View {
    private static final String TAG = "PostDetailsFragment";
    private static final String ARG_POST_TABLE_POST_ID = "post_id";

    @BindView(R.id.swipe_refresh_layout_post_details)
    SwipeRefreshLayout swipeRefreshLayoutPostDetails;
    @BindView(R.id.recycler_view_post_details)
    RecyclerView recyclerViewPostDetails;

    private PostDetailsContract.Presenter mPostDetailsPresenter;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    private PostDetailsRecyclerAdapter mPostDetailsRecyclerAdapter;

    public static PostDetailsFragment newInstance(int postId) {
        Bundle args = new Bundle();
        args.putInt(ARG_POST_TABLE_POST_ID, postId);
        PostDetailsFragment fragment = new PostDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setPresenter(new PostDetailsPresenter(this));
        mPostDetailsPresenter.subscribe();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setPostDetailsRecyclerViewProperties();

        // Get post details.
        mPostDetailsPresenter.getDetails(getArguments().getInt(ARG_POST_TABLE_POST_ID));

        // Get users.
        mPostDetailsPresenter.getUsers(getArguments().getInt(ARG_POST_TABLE_POST_ID));

        // Always load offline posts details first.
        getPostDetails(getArguments().getInt(ARG_POST_TABLE_POST_ID), true);

        initSwipeRefreshLayout();
    }

    private void setPostDetailsRecyclerViewProperties() {
        // Create a layout manager for recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPostDetails.setLayoutManager(layoutManager);

        // Create adapter that will power this recycler view.
        mPostDetailsRecyclerAdapter = new PostDetailsRecyclerAdapter();

        mPostDetailsRecyclerAdapter.updateTitles(Arrays.asList(getString(R.string.post_details_users),
                getString(R.string.post_details_comments)));

        recyclerViewPostDetails.setAdapter(mPostDetailsRecyclerAdapter);
    }

    /**
     * Get extra details like media, comments, etc.
     *
     * @param postId      Id of that particular post.
     * @param loadOffline True, if you want to load details from database, otherwise false.
     */
    private void getPostDetails(final int postId, final boolean loadOffline) {
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
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        mPostDetailsRecyclerAdapter.setCommentsUnavailable(false);
                        mPostDetailsRecyclerAdapter.setUsersUnavailable(false);
                        if (loadOffline) {
                            mPostDetailsPresenter.getExtraDetailsOffline(postId);
                        } else {
                            mPostDetailsPresenter.getExtraDetails(s, postId);
                        }
                    }
                });
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayoutPostDetails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(true)) {
                    getPostDetails(getArguments().getInt(ARG_POST_TABLE_POST_ID), false);
                } else {
                    swipeRefreshLayoutPostDetails.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayoutPostDetails.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPostDetailsPresenter.unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_post_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_redirect_url:
                mPostDetailsPresenter.openRedirectUrl(getActivity());
                break;
            case R.id.menu_share:
                mPostDetailsPresenter.sharePostDetails(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDetails(Cursor cursor) {
        String title = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME);
        String description = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String day = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY);
        String date = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT);
        String backdropUrl = CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL);

        // Show post details on activity too.
        mOnFragmentInteractionListener.showPostDetails(new PostDetails(title, description, day, date, backdropUrl));
    }

    @Override
    public void showUsers(List<User> users) {
        mPostDetailsRecyclerAdapter.updateUsers(users);
    }

    @Override
    public void showMedia(List<Media> media) {
        Logger.d(TAG, "showMedia: media: " + media.size());
        mOnFragmentInteractionListener.showMedia(media);
    }

    @Override
    public void attachInstallLinks(List<InstallLink> installLinks) {
        Logger.d(TAG, "attachInstallLinks: installLinks: " + installLinks.size());
    }

    @Override
    public void showAllUsers(List<User> users) {
        Logger.d(TAG, "showAllUsers: users: " + users.size());
        mPostDetailsRecyclerAdapter.updateUsers(users);
    }

    @Override
    public void showComments(final List<Comment> comments) {
        Logger.d(TAG, "showComments: comments: " + comments.size());
        mPostDetailsRecyclerAdapter.updateComments(comments);
    }

    @Override
    public void unableToFetchPostDetails(String errorMessage) {
        Logger.d(TAG, "unableToFetchPostDetails: " + errorMessage);
    }

    @Override
    public void unableToFetchUsers(String errorMessage) {
        Logger.d(TAG, "unableToFetchUsers: " + errorMessage);
    }

    @Override
    public void unableToFetchMedia(String errorMessage) {
        Logger.d(TAG, "unableToFetchMedia: " + errorMessage);
    }

    @Override
    public void unableToFetchInstallLinks(String errorMessage) {
        Logger.d(TAG, "unableToFetchInstallLinks: " + errorMessage);
    }

    @Override
    public void unableToFetchAllUsers(String errorMessage) {
        Logger.d(TAG, "unableToFetchAllUsers: " + errorMessage);
        mPostDetailsRecyclerAdapter.setUsersUnavailable(true);
    }

    @Override
    public void unableToFetchComments(String errorMessage) {
        Logger.d(TAG, "unableToFetchComments: " + errorMessage);
        mPostDetailsRecyclerAdapter.setCommentsUnavailable(true);
    }

    @Override
    public void noOfflineDataAvailable() {
        swipeRefreshLayoutPostDetails.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutPostDetails.setRefreshing(false);
            }
        });
        Logger.d(TAG, "noOfflineDataAvailable");
        if (isNetworkAvailable(true)) {
            getPostDetails(getArguments().getInt(ARG_POST_TABLE_POST_ID), false);
        } else {
            mPostDetailsRecyclerAdapter.setCommentsUnavailable(false);
            mPostDetailsRecyclerAdapter.setUsersUnavailable(false);
        }
    }

    @Override
    public void dismissLoading() {
        Logger.d(TAG, "dismissLoading");
        swipeRefreshLayoutPostDetails.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutPostDetails.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(PostDetailsContract.Presenter presenter) {
        mPostDetailsPresenter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkConnectivityChanged(NetworkEvent networkEvent) {
        if (networkEvent.isConnected() && mPostDetailsRecyclerAdapter.isUnavailable()) {
            getPostDetails(getArguments().getInt(ARG_POST_TABLE_POST_ID), false);
        }
    }

    public void openMedia(Media media) {
        mPostDetailsPresenter.openMedia(getContext(), media);
    }

    public interface OnFragmentInteractionListener {
        void showPostDetails(PostDetails postDetails);

        void showMedia(List<Media> media);
    }
}
