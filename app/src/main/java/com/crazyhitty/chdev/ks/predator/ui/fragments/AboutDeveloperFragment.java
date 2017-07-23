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

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.UserFallback;
import com.crazyhitty.chdev.ks.predator.ui.activities.UserProfileActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/13/2017 1:09 PM
 * Description: Unavailable
 */

public class AboutDeveloperFragment extends BaseSupportFragment {
    @BindView(R.id.text_view_extra_desc)
    TextView txtExtraDesc;

    public static AboutDeveloperFragment newInstance() {
        return new AboutDeveloperFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_developer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtExtraDesc.setMovementMethod(LinkMovementMethod.getInstance());
        txtExtraDesc.setText(Html.fromHtml(getString(R.string.activity_about_extra_desc)));
    }

    @OnClick(R.id.card_view_kartik)
    public void onCardViewKartikClick() {
        UserFallback userFallback = new UserFallback();
        userFallback.setName(getString(R.string.activity_about_developer_name_short));
        userFallback.setUsername(getString(R.string.activity_about_developer_username));
        userFallback.setHeadline(getString(R.string.activity_about_developer_headline));
        userFallback.setWebsiteUrl(Constants.About.URL_DEVELOPER_WEBSITE);
        userFallback.setImage(Constants.About.URL_DEVELOPER_IMAGE);

        UserProfileActivity.startActivity(getContext(),
                Constants.About.DEV_KARTIK_PRODUCT_HUNT_USER_ID,
                userFallback);
    }

    @OnClick(R.id.card_view_kajal)
    public void onCardViewKajalClick() {
        CustomTabsHelperFragment.open(getActivity(),
                getCustomTabsIntent(),
                Uri.parse(Constants.About.URL_GITHUB_KAJAL),
                getCustomTabsFallback());
    }
}
