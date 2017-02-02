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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.mediaDetails.MediaDetailsContract;
import com.crazyhitty.chdev.ks.predator.core.mediaDetails.MediaDetailsPresenter;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.MediaPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/23/2017 1:56 PM
 * Description: Unavailable
 */

public class MediaFullScreenActivity extends BaseAppCompatActivity implements MediaDetailsContract.View, MediaPagerAdapter.OnImageClickListener {
    private static final String TAG = "MediaFullScreenActivity";
    private static final String ARG_MEDIA_TABLE_MEDIA_ID = "media_id";
    private static final String ARG_MEDIA_TABLE_POST_ID = "post_id";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    @BindView(R.id.coordinatar_layout_full_screen)
    CoordinatorLayout coordinatorLayoutFullScreen;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            coordinatorLayoutFullScreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    @BindView(R.id.relative_layout_footer)
    RelativeLayout relativeLayoutFooter;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            relativeLayoutFooter.setVisibility(View.VISIBLE);
        }
    };
    @BindView(R.id.view_pager_media)
    ViewPager viewPagerMedia;
    @BindView(R.id.image_button_close)
    ImageButton imgBtnClose;
    @BindView(R.id.image_button_previous)
    ImageButton imgBtnPrevious;
    @BindView(R.id.image_button_next)
    ImageButton imgBtnNext;
    @BindView(R.id.text_view_current_position)
    TextView txtCurrentPosition;
    private MediaPagerAdapter mMediaPagerAdapter;
    private MediaDetailsContract.Presenter mMediaDetailsPresenter;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public static void startActivity(Context context, int mediaId, int postId) {
        Intent intent = new Intent(context, MediaFullScreenActivity.class);
        intent.putExtra(ARG_MEDIA_TABLE_MEDIA_ID, mediaId);
        intent.putExtra(ARG_MEDIA_TABLE_POST_ID, postId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_media_full_screen);
        ButterKnife.bind(this);
        setPresenter(new MediaDetailsPresenter(this));
        mMediaDetailsPresenter.subscribe();
        prepareUiElements();

        // Fetch media details.
        mMediaDetailsPresenter.getMedia(getIntent().getExtras().getInt(ARG_MEDIA_TABLE_POST_ID),
                getIntent().getExtras().getInt(ARG_MEDIA_TABLE_MEDIA_ID));
    }

    private void applyTheme() {
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_black_alpha));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaDetailsPresenter.unSubscribe();
    }

    private void prepareUiElements() {
        // Don't hide UI elements while the user is in the process of interacting with them.
        relativeLayoutFooter.setOnTouchListener(mDelayHideTouchListener);
        for (int i = 0; i < relativeLayoutFooter.getChildCount(); i++) {
            relativeLayoutFooter.getChildAt(i).setOnTouchListener(mDelayHideTouchListener);
        }

        // Auto hide the elements instantly.
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, 0);

        Logger.d(TAG, "prepareUiElements: navBarHeight: " + ScreenUtils.getNavigationBarHeight(getApplicationContext()));

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) relativeLayoutFooter.getLayoutParams();
        layoutParams.setMargins(0,
                0,
                0,
                ScreenUtils.getNavigationBarHeight(getApplicationContext()));
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        relativeLayoutFooter.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        coordinatorLayoutFullScreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void showMedia(final List<Media> media, final int defaultPosition) {
        mMediaPagerAdapter = new MediaPagerAdapter(media, this);
        viewPagerMedia.setAdapter(mMediaPagerAdapter);
        viewPagerMedia.setCurrentItem(defaultPosition);

        String currentPosition = String.format(Locale.US, "%d / %d", defaultPosition + 1, media.size());
        txtCurrentPosition.setText(currentPosition);

        viewPagerMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String currentPosition = String.format(Locale.US, "%d / %d", position + 1, media.size());
                txtCurrentPosition.setText(currentPosition);
                manageNextPreviousButtons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        manageNextPreviousButtons();
    }

    @Override
    public void unableToFetchMedia(String errorMessage) {
        Logger.d(TAG, "unableToFetchMedia: " + errorMessage);
    }

    @Override
    public void setPresenter(MediaDetailsContract.Presenter presenter) {
        mMediaDetailsPresenter = presenter;
    }

    @Override
    public void onImageClicked() {
        toggle();
    }

    @OnClick(R.id.image_button_close)
    public void onClose() {
        onBackPressed();
    }

    @OnClick(R.id.image_button_previous)
    public void onPrevious() {
        if (viewPagerMedia.getCurrentItem() > 0) {
            viewPagerMedia.setCurrentItem(viewPagerMedia.getCurrentItem() - 1);
        }
    }

    @OnClick(R.id.image_button_next)
    public void onNext() {
        if (viewPagerMedia.getCurrentItem() < mMediaPagerAdapter.getCount() - 1) {
            viewPagerMedia.setCurrentItem(viewPagerMedia.getCurrentItem() + 1);
        }
    }

    private void manageNextPreviousButtons() {
        if (viewPagerMedia.getCurrentItem() == 0) {
            // enable next button
            imgBtnNext.setEnabled(true);
            // disable back button
            imgBtnPrevious.setEnabled(false);

            imgBtnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
            imgBtnPrevious.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.text_color_secondary));
        } else if (viewPagerMedia.getCurrentItem() == mMediaPagerAdapter.getCount() - 1) {
            // disable next button
            imgBtnNext.setEnabled(false);
            // enable back button
            imgBtnPrevious.setEnabled(true);

            imgBtnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.text_color_secondary));
            imgBtnPrevious.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
        } else {
            // enable next button
            imgBtnNext.setEnabled(true);
            // enable back button
            imgBtnPrevious.setEnabled(true);

            imgBtnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
            imgBtnPrevious.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
        }
    }
}
