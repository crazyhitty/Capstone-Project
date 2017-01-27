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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.about.AboutContract;
import com.crazyhitty.chdev.ks.predator.core.about.AboutPresenter;
import com.crazyhitty.chdev.ks.predator.models.About;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.AboutRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/2017 1:47 AM
 * Description: Unavailable
 */

public class AboutFragment extends BaseSupportFragment implements AboutContract.View, AboutRecyclerAdapter.OnLibraryItemClickListener {
    @BindView(R.id.recycler_view_about)
    RecyclerView recyclerViewAbout;

    private AboutContract.Presenter mAboutPresenter;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setPresenter(new AboutPresenter(this));
        mAboutPresenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAboutRecyclerViewProperties();
        mAboutPresenter.fetchAboutData(getContext());
    }

    private void setAboutRecyclerViewProperties() {
        // Create a layout manager for recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewAbout.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAboutPresenter.unSubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_about, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_feedback:
                mAboutPresenter.openFeedback(getActivity());
                break;
            case R.id.menu_changelog:
                mAboutPresenter.openChangelog(getFragmentManager());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showAboutData(List<About> about) {
        AboutRecyclerAdapter aboutRecyclerAdapter = new AboutRecyclerAdapter(about, this);
        recyclerViewAbout.setAdapter(aboutRecyclerAdapter);
    }

    @Override
    public void unableToShowAboutData(String errorMessage) {
        showLongToast(errorMessage);
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mAboutPresenter = presenter;
    }

    @Override
    public void onLibraryItemClick(int position, String redirectUrl) {
        mAboutPresenter.openLibraryRedirectUrl(getActivity(), redirectUrl);
    }

    public void openGithub() {
        mAboutPresenter.openGithub(getActivity());
    }

    public void openGooglePlus() {
        mAboutPresenter.openGooglePlus(getContext());
    }

    public void openMail() {
        mAboutPresenter.openMail(getContext());
    }
}
