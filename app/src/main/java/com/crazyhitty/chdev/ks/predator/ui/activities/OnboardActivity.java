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

package com.crazyhitty.chdev.ks.predator.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.ui.adapters.OnboardPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/4/2017 10:48 PM
 * Description: First activity user will interact with, when he/she starts up the app for the first
 * time.
 */

public class OnboardActivity extends BaseAppCompatActivity {
    @BindView(R.id.view_pager_onboard)
    ViewPager viewPagerOnboard;
    @BindView(R.id.view_pager_indicator)
    PageIndicatorView pageIndicatorView;

    private OnboardPagerAdapter mOnboardPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        mOnboardPagerAdapter = new OnboardPagerAdapter();
        viewPagerOnboard.setAdapter(mOnboardPagerAdapter);
        viewPagerOnboard.setOffscreenPageLimit(2);

        pageIndicatorView.setViewPager(viewPagerOnboard);
        pageIndicatorView.setSelectedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        pageIndicatorView.setUnselectedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        pageIndicatorView.setRadius(getResources().getDimension(R.dimen.indicator_radius));
        pageIndicatorView.setAnimationType(AnimationType.WORM);
    }
}
