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
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.events.OnboardSecondAnimateEvent;
import com.crazyhitty.chdev.ks.predator.ui.activities.OnboardActivity;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/5/2017 5:21 PM
 * Description: Unavailable
 */

public class OnboardingSecondView extends ScrollView {
    private static final int ANIM_ITEM_DURATION_MS = 500;
    private static final int ANIM_AVG_DELAY_DURATION_MS = 300;

    @BindView(R.id.linear_layout_onboard)
    LinearLayout linearLayoutOnboard;

    private boolean mAlreadyAnimated = false;

    public OnboardingSecondView(Context context) {
        super(context);
        initializeViews(context);
    }

    public OnboardingSecondView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public OnboardingSecondView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OnboardingSecondView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_onboarding_second, this);

        // bind views
        ButterKnife.bind(this);

        // set layout params
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    /**
     * This method will animate the child views of this viewgroup. This method will generally be
     * invoked from {@link OnboardActivity#initViewPager()}.
     *
     * @param onboardSecondAnimateEvent dummy event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void animateViews(OnboardSecondAnimateEvent onboardSecondAnimateEvent) {
        if (mAlreadyAnimated) {
            return;
        }

        mAlreadyAnimated = true;

        Interpolator interpolator = new DecelerateInterpolator(1.0f);

        int animationDuration = 0;

        for (int i = 0; i < linearLayoutOnboard.getChildCount(); i++) {
            linearLayoutOnboard.getChildAt(i).animate()
                    .setDuration(ANIM_ITEM_DURATION_MS)
                    .setInterpolator(interpolator)
                    .translationXBy(ScreenUtils.dpToPx(getContext().getApplicationContext(), 16.0f))
                    .alpha(1.0f)
                    .setStartDelay(animationDuration)
                    .start();
            animationDuration += ANIM_AVG_DELAY_DURATION_MS;
        }
    }
}
