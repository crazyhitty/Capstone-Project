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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/10/2017 3:01 PM
 * Description: Unavailable
 */

public class LoadingView extends RelativeLayout {
    private static final int ANIM_MESSAGE_DURATION_MS = 500;
    private static final int ANIM_PROGRESS_DURATION_MS = 500;
    private static final int ANIM_IMAGE_DURATION_MS = 500;
    private static final int ANIM_BUTTON_DURATION_MS = 500;
    private static final int ANIM_MESSAGE_DELAY_APPEARING_MS = 500;
    private static final int ANIM_PROGRESS_DELAY_APPEARING_MS = 750;
    private static final int ANIM_IMAGE_DELAY_APPEARING_MS = 500;
    private static final int ANIM_BUTTON_DELAY_APPEARING_MS = 750;
    private static final int ANIM_MESSAGE_DELAY_DISAPPEARING_MS = 500;
    private static final int ANIM_PROGRESS_DELAY_DISAPPEARING_MS = 500;
    private static final int ANIM_IMAGE_DELAY_DISAPPEARING_MS = 500;
    private static final int ANIM_BUTTON_DELAY_DISAPPEARING_MS = 500;

    @BindView(R.id.image_view_loading)
    SimpleDraweeView imgViewLoading;
    @BindView(R.id.text_view_message)
    TextView txtMessage;
    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBarLoading;
    @BindView(R.id.button_retry)
    Button btnRetry;

    private STATE_SHOWN mStateShown = STATE_SHOWN.LOADING;
    private OnRetryClickListener mOnRetryClickListener;
    private AnimationStateChangeListener mAnimationStateChangeListener;

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
        switch (PredatorSharedPreferences.getCurrentTheme(getContext())) {
            case LIGHT:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_primary));
                break;
            case DARK:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_black_dark));
                break;
            case AMOLED:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_black_dark));
                break;
        }

        // Set animation.
        manageAnimation(true);
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        mOnRetryClickListener = onRetryClickListener;
    }

    public void setAnimationStateChangeListener(AnimationStateChangeListener animationStateChangeListener) {
        mAnimationStateChangeListener = animationStateChangeListener;
    }

    public void startLoading(final TYPE type) {
        int animDelay = 0;
        if (mStateShown == STATE_SHOWN.ERROR) {
            animDelay = 700;
            manageAnimation(false);
        }

        mStateShown = STATE_SHOWN.LOADING;

        setVisibility(View.VISIBLE);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarLoading.setVisibility(View.VISIBLE);
                progressBarLoading.setAlpha(0.0f);

                imgViewLoading.setVisibility(View.GONE);
                imgViewLoading.setAlpha(0.0f);

                btnRetry.setVisibility(View.GONE);
                btnRetry.setAlpha(0.0f);

                txtMessage.setAlpha(0.0f);
                setLoadingType(type);

                txtMessage.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                progressBarLoading.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));

                manageAnimation(true);
            }
        }, animDelay);
    }

    public void stopLoading() {
        setVisibility(View.GONE);
    }

    public void setError(final String errorMessage) {
        manageAnimation(false);

        // Set new properties of views and animate them until the previous animation is finished.
        postDelayed(new Runnable() {
            @Override
            public void run() {
                txtMessage.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                progressBarLoading.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                btnRetry.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));

                txtMessage.setAlpha(0.0f);

                progressBarLoading.setVisibility(View.GONE);
                progressBarLoading.setAlpha(0.0f);

                imgViewLoading.setVisibility(View.VISIBLE);
                imgViewLoading.setAlpha(0.0f);

                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setAlpha(0.0f);

                txtMessage.setText(errorMessage);
                manageAnimation(true);
            }
        }, 700);

        mStateShown = STATE_SHOWN.ERROR;
    }

    public void setComplete(final String message) {
        manageAnimation(false);

        // Set new properties of views and animate them until the previous animation is finished.
        postDelayed(new Runnable() {
            @Override
            public void run() {
                txtMessage.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                progressBarLoading.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                btnRetry.setTranslationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), 32.0f));
                GenericDraweeHierarchy hierarchy = imgViewLoading.getHierarchy();

                boolean isLightTheme = PredatorSharedPreferences.getCurrentTheme(getContext()) == PredatorSharedPreferences.THEME_TYPE.LIGHT;
                hierarchy.setPlaceholderImage(isLightTheme ? R.drawable.ic_done : R.drawable.ic_done_inverse);

                imgViewLoading.setHierarchy(hierarchy);

                txtMessage.setAlpha(0.0f);

                progressBarLoading.setVisibility(View.GONE);
                progressBarLoading.setAlpha(0.0f);

                imgViewLoading.setVisibility(View.VISIBLE);
                imgViewLoading.setAlpha(0.0f);

                btnRetry.setVisibility(View.GONE);
                btnRetry.setAlpha(0.0f);

                txtMessage.setText(message);
                manageAnimation(true);

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        mStateShown = STATE_SHOWN.COMPLETE;
                        if (mAnimationStateChangeListener != null) {
                            mAnimationStateChangeListener.onStateChanged(mStateShown);
                        }
                    }
                }, 2000);
            }
        }, 700);
    }

    private void manageAnimation(boolean isAppearing) {
        float alpha = 1.0f;
        float translationY = 0.0f;
        int messageDelay = ANIM_MESSAGE_DELAY_APPEARING_MS;
        int progressBarDelay = ANIM_PROGRESS_DELAY_APPEARING_MS;
        int imageDelay = ANIM_IMAGE_DELAY_APPEARING_MS;
        int buttonDelay = ANIM_BUTTON_DELAY_APPEARING_MS;

        if (!isAppearing) {
            alpha = 0.0f;
            translationY = -32.0f;
            messageDelay = ANIM_MESSAGE_DELAY_DISAPPEARING_MS;
            progressBarDelay = ANIM_PROGRESS_DELAY_DISAPPEARING_MS;
            imageDelay = ANIM_IMAGE_DELAY_DISAPPEARING_MS;
            buttonDelay = ANIM_BUTTON_DELAY_DISAPPEARING_MS;
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

        imgViewLoading.animate()
                .setDuration(ANIM_IMAGE_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(alpha)
                .setStartDelay(imageDelay)
                .translationY(ScreenUtils.dpToPx(getContext().getApplicationContext(), translationY))
                .start();

        btnRetry.animate()
                .setDuration(ANIM_IMAGE_DURATION_MS)
                .setInterpolator(interpolator)
                .alpha(alpha)
                .setStartDelay(buttonDelay)
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
            case LATEST_COLLECTIONS:
                String latestCollections = getResources().getString(R.string.loading_view_latest_collections);
                txtMessage.setText(String.format(loadingText, latestCollections));
                break;
            case COLLECTION_POSTS:
                String collectionsPosts = getResources().getString(R.string.loading_view_collections_posts);
                txtMessage.setText(String.format(loadingText, collectionsPosts));
                break;
        }
    }

    public STATE_SHOWN getCurrentState() {
        return mStateShown;
    }

    @OnClick(R.id.button_retry)
    public void onRetry() {
        if (mOnRetryClickListener != null) {
            mOnRetryClickListener.onRetry();
        }
    }

    public enum TYPE {
        LATEST_POSTS,
        LATEST_COLLECTIONS,
        COLLECTION_POSTS
    }

    public enum STATE_SHOWN {
        LOADING,
        ERROR,
        COMPLETE
    }

    public interface OnRetryClickListener {
        void onRetry();
    }

    public interface AnimationStateChangeListener {
        void onStateChanged(STATE_SHOWN stateShown);
    }
}
