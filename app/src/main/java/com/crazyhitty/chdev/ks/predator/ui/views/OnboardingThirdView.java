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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.events.OnboardContinueEvent;
import com.crazyhitty.chdev.ks.predator.events.OnboardThirdAnimateEvent;
import com.crazyhitty.chdev.ks.predator.ui.activities.OnboardActivity;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/5/2017 5:21 PM
 * Description: Unavailable
 */

public class OnboardingThirdView extends RelativeLayout {
    private static final int ANIM_ITEM_DURATION_MS = 500;
    private static final int ANIM_DESCRIPTION_DELAY_DURATION_MS = 300;
    private static final int ANIM_BTN_REGULAR_DELAY_DURATION_MS = 600;
    private static final int ANIM_BTN_REGISTERED_DELAY_DURATION_MS = 900;

    @BindView(R.id.text_view_title)
    TextView txtTitle;
    @BindView(R.id.text_view_description)
    TextView txtDescription;
    @BindView(R.id.button_continue_as_regular_user)
    Button btnRegularUser;
    @BindView(R.id.button_continue_as_registered_user)
    Button btnRegisteredUser;

    private boolean mAlreadyAnimated = false;

    public OnboardingThirdView(Context context) {
        super(context);
        initializeViews(context);
    }

    public OnboardingThirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public OnboardingThirdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OnboardingThirdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_onboarding_third, this);

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
     * @param onboardThirdAnimateEvent dummy event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void animateViews(OnboardThirdAnimateEvent onboardThirdAnimateEvent) {
        if (mAlreadyAnimated) {
            return;
        }

        mAlreadyAnimated = true;

        Interpolator interpolator = new DecelerateInterpolator(1.0f);

        float translationBy = ScreenUtils.dpToPx(getContext().getApplicationContext(), 16.0f);

        txtTitle.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationXBy(translationBy)
                .alpha(1.0f)
                .start();

        txtDescription.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationXBy(translationBy)
                .alpha(1.0f)
                .setStartDelay(ANIM_DESCRIPTION_DELAY_DURATION_MS)
                .start();

        btnRegularUser.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationYBy(-translationBy)
                .alpha(1.0f)
                .setStartDelay(ANIM_BTN_REGULAR_DELAY_DURATION_MS)
                .start();

        btnRegisteredUser.animate()
                .setDuration(ANIM_ITEM_DURATION_MS)
                .setInterpolator(interpolator)
                .translationYBy(-translationBy)
                .alpha(1.0f)
                .setStartDelay(ANIM_BTN_REGISTERED_DELAY_DURATION_MS)
                .start();
    }

    @OnClick(R.id.button_continue_as_regular_user)
    public void continueAsRegularUser() {
        EventBus.getDefault().post(new OnboardContinueEvent(OnboardContinueEvent.TYPE.REGULAR));
    }

    @OnClick(R.id.button_continue_as_registered_user)
    public void continueAsRegisteredUser() {
        EventBus.getDefault().post(new OnboardContinueEvent(OnboardContinueEvent.TYPE.REGISTERED));
    }
}
