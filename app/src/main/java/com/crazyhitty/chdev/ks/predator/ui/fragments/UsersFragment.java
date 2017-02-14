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
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.events.UsersEvent;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.UsersRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.UserItemDecorator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/10/2017 5:02 PM
 * Description: Unavailable
 */

public class UsersFragment extends BaseSupportFragment {
    @BindView(R.id.recycler_view_users)
    RecyclerView recyclerViewUsers;
    @BindView(R.id.linear_layout_loading)
    LinearLayout linearLayoutLoading;
    @BindView(R.id.text_view_message)
    TextView txtMessage;

    private UsersRecyclerAdapter mUsersRecyclerAdapter;

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
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
        recyclerViewUsers.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        final UserItemDecorator userItemDecorator = new UserItemDecorator(getContext());
        recyclerViewUsers.addItemDecoration(userItemDecorator);

        mUsersRecyclerAdapter = new UsersRecyclerAdapter(null);
        recyclerViewUsers.setAdapter(mUsersRecyclerAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUsers(UsersEvent usersEvent) {
        if (usersEvent.getUsers() != null && usersEvent.getUsers().size() != 0) {
            linearLayoutLoading.setVisibility(View.GONE);
            mUsersRecyclerAdapter.updateUsers(usersEvent.getUsers());
        } else if (mUsersRecyclerAdapter.getItemCount() == 0) {
            txtMessage.setText(R.string.fragment_comments_unavailable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
