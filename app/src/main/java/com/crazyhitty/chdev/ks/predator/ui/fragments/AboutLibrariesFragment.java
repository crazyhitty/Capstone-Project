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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.about.AboutLibrariesContract;
import com.crazyhitty.chdev.ks.predator.core.about.AboutLibrariesPresenter;
import com.crazyhitty.chdev.ks.predator.models.AboutLibrary;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.AboutLibrariesRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.UserItemDecorator;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/13/2017 1:09 PM
 * Description: Unavailable
 */

public class AboutLibrariesFragment extends BaseSupportFragment implements AboutLibrariesContract.View,
        AboutLibrariesRecyclerAdapter.OnLibraryItemClickListener {
    private static final String TAG = "AboutLibrariesFragment";
    private final CustomTabsIntent mCustomTabsIntent = new CustomTabsIntent.Builder()
            .enableUrlBarHiding()
            .setShowTitle(true)
            .build();
    private final CustomTabsActivityHelper.CustomTabsFallback mCustomTabsFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, R.string.no_application_available_to_open_this_url, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            };
    @BindView(R.id.recycler_view_libraries)
    RecyclerView recyclerViewLibraries;
    private AboutLibrariesRecyclerAdapter mAboutLibrariesRecyclerAdapter;
    private AboutLibrariesContract.Presenter mAboutLibrariesPresenter;

    public static AboutLibrariesFragment newInstance() {
        return new AboutLibrariesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new AboutLibrariesPresenter(this));
        mAboutLibrariesPresenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_libraries, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecyclerViewProperties();
        mAboutLibrariesPresenter.fetchLibraries(getContext());
    }

    private void setRecyclerViewProperties() {
        // Create a list type layout manager.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewLibraries.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        final UserItemDecorator userItemDecorator = new UserItemDecorator(getContext());
        recyclerViewLibraries.addItemDecoration(userItemDecorator);

        mAboutLibrariesRecyclerAdapter = new AboutLibrariesRecyclerAdapter(null, this);
        recyclerViewLibraries.setAdapter(mAboutLibrariesRecyclerAdapter);
    }

    @Override
    public void showLibrariesData(List<AboutLibrary> aboutLibraries) {
        mAboutLibrariesRecyclerAdapter.updateLibraries(aboutLibraries);
    }

    @Override
    public void unableToShowLibrariesData(String errorMessage) {
        Logger.d(TAG, "unableToShowLibrariesData: " + errorMessage);
    }

    @Override
    public void setPresenter(AboutLibrariesContract.Presenter presenter) {
        mAboutLibrariesPresenter = presenter;
    }

    @Override
    public void onLibraryItemClick(int position, String redirectUrl) {
        CustomTabsHelperFragment.open(getActivity(),
                mCustomTabsIntent,
                Uri.parse(redirectUrl),
                mCustomTabsFallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAboutLibrariesPresenter.unSubscribe();
    }
}
