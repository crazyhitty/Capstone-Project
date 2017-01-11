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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/10/2017 3:01 PM
 * Description: Unavailable
 */

public class LoadingView extends RelativeLayout {
    private static final int ANIM_MESSAGE_DURATION_MS = 500;
    private static final int ANIM_PROGRESS_DURATION_MS = 500;
    private static final int ANIM_MESSAGE_DELAY_APPEARING_MS = 500;
    private static final int ANIM_PROGRESS_DELAY_APPEARING_MS = 750;
    private static final int ANIM_MESSAGE_DELAY_DISAPPEARING_MS = 500;
    private static final int ANIM_PROGRESS_DELAY_DISAPPEARING_MS = 500;

    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;

    public LoadingView(Context context) {
        super(context);
        initializeViews(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_loading, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        // Set layout properties.
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        // Set animation.
        manageAnimation(true, false);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                setError("Something happened!");
            }
        }, 3000);
    }

    public void startLoading() {
        setVisibility(View.VISIBLE);
        progressBarLoading.setVisibility(View.VISIBLE);
        manageAnimation(true, false);
    }

    public void stopLoading() {
        setVisibility(View.GONE);
    }

    private void manageAnimation(boolean isAppearing, boolean isError) {
        float alpha = 1.0f;
        float translationY = 0.0f;
        int messageDelay = ANIM_MESSAGE_DELAY_APPEARING_MS;
        int progressBarDelay = ANIM_PROGRESS_DELAY_APPEARING_MS;

        if (!isAppearing) {
            alpha = 0.0f;
            translationY = -32.0f;
            messageDelay = ANIM_MESSAGE_DELAY_DISAPPEARING_MS;
            progressBarDelay = ANIM_PROGRESS_DELAY_DISAPPEARING_MS;
        }

        Interpolator interpolator = new DecelerateInterpolator();

        txtMessage.animate()
                .setDuration(ANIM_MESSAGE_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(alpha)
                .setStartDelay(messageDelay)
                .translationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), translationY))
                .start();

        progressBarLoading.animate()
                .setDuration(ANIM_PROGRESS_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(alpha)
                .setStartDelay(progressBarDelay)
                .translationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), translationY))
                .start();
    }

    public void setLoadingType(TYPE type) {
        String loadingText = getResources().getString(R.string.loading_view_message);
        switch (type) {
            case LATEST_POSTS:
                String latestPosts = getResources().getString(R.string.loading_view_latest_posts);
                txtMessage.setText(String.format(loadingText, latestPosts));
                break;
        }
    }

    public void setError(final String errorMessage) {
        manageAnimation(false, false);

        // Set new translationY for both txtMessage and progressBarLoading.
        txtMessage.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 64.0f));
        progressBarLoading.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 64.0f));

        // Change text after the animation is complete and run the .
        postDelayed(new Runnable() {
            @Override
            public void run() {
                txtMessage.setText(errorMessage);
                manageAnimation(true, true);
            }
        }, 600);
    }

    public enum TYPE {
        LATEST_POSTS
    }
}
