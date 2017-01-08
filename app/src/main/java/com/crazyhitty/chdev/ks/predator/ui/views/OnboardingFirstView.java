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

package com.crazyhitty.chdev.ks.predator.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/5/2017 12:09 PM
 * Description: Unavailable
 */

public class OnboardingFirstView extends RelativeLayout {
    private static final int ANIM_START_DELAY_DURATION_MS = 600;
    private static final int ANIM_ITEM_DURATION_MS = 500;
    private static final int ANIM_DESCRIPTION_DELAY_DURATION_MS = 300;
    private static final int ANIM_IMAGE_DELAY_DURATION_MS = 200;

    @BindView(R.id.image_view_icon)
    ImageView imgViewIcon;
    @BindView(R.id.text_view_title)
    TextView txtTitle;
    @BindView(R.id.text_view_description)
    TextView txtDescription;

    public OnboardingFirstView(Context context) {
        super(context);
        initializeViews(context);
    }

    public OnboardingFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public OnboardingFirstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OnboardingFirstView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_onboarding_first, this);

        // bind views
        ButterKnife.bind(this);

        // set layout params
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animateViews();
            }
        }, ANIM_START_DELAY_DURATION_MS);

        // center out the image, so that it overlaps the window background icon.

        int topPadding = 0;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // for below kitkat devices, only add status bar height to top padding
            topPadding = ScreenUtils.getStatusBarHeight(getContext().getApplicationContext());
        } else {
            // for below kitkat devices, add both status bar height and navigation bar height to
            // top padding
            topPadding = ScreenUtils.getStatusBarHeight(getContext().getApplicationContext()) +
                    ScreenUtils.getNavigationBarHeight(getContext().getApplicationContext());
        }
        imgViewIcon.setPadding(0,
                topPadding,
                0,
                0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void animateViews() {
        Interpolator interpolator = new DecelerateInterpolator(0.8f);

        txtTitle.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(1.0f)
                .translationXBy(ScreenUtils.dpToPx(getContext().getApplicationContext(), 16.0f))
                .start();

        txtDescription.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationXBy(ScreenUtils.dpToPx(getContext().getApplicationContext(), 16.0f))
                .alpha(1.0f)
                .setStartDelay(ANIM_DESCRIPTION_DELAY_DURATION_MS)
                .start();

        imgViewIcon.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(new DecelerateInterpolator(1.2f))
                .translationYBy(75)
                .setStartDelay(ANIM_IMAGE_DELAY_DURATION_MS)
                .start();
    }
}