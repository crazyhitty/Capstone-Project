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

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class OnboardingFirstView extends LinearLayout {
    private static final int ANIM_START_DELAY_DURATION_MS = 600;
    private static final int ANIM_LAYOUT_TRANSITION_DURATION_MS = 500;
    private static final int ANIM_ITEM_DURATION_MS = 500;
    private static final int ANIM_DESCRIPTION_DELAY_DURATION_MS = 300;

    @BindView(R.id.image_view_icon)
    ImageView imageViewIcon;
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
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        // set layout properties
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        int paddingPx = ScreenUtils.dpToPxInt(getContext().getApplicationContext(), getResources().getDimension(R.dimen.padding_small));
        setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        // set layout transitions
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setInterpolator(LayoutTransition.CHANGING, new DecelerateInterpolator(0.2f));
        layoutTransition.setDuration(LayoutTransition.CHANGING, ANIM_LAYOUT_TRANSITION_DURATION_MS);
        setLayoutTransition(layoutTransition);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTitle.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.VISIBLE);
                animateViews();
            }
        }, ANIM_START_DELAY_DURATION_MS);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void animateViews() {
        Interpolator interpolator = new DecelerateInterpolator(0.6f);

        txtTitle.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(1.0f)
                .translationYBy(25)
                .start();

        txtDescription.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationYBy(25)
                .alpha(1.0f)
                .setStartDelay(ANIM_DESCRIPTION_DELAY_DURATION_MS)
                .start();
    }
}