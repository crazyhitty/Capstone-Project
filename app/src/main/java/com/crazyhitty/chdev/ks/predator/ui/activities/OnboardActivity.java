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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageButton;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthContract;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthPresenter;
import com.crazyhitty.chdev.ks.predator.core.categories.CategoriesContract;
import com.crazyhitty.chdev.ks.predator.core.categories.CategoriesPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.events.OAuthTokenEvent;
import com.crazyhitty.chdev.ks.predator.events.OnboardContinueEvent;
import com.crazyhitty.chdev.ks.predator.events.OnboardSecondAnimateEvent;
import com.crazyhitty.chdev.ks.predator.events.OnboardThirdAnimateEvent;
import com.crazyhitty.chdev.ks.predator.models.Category;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.OnboardPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.views.OnboardingSecondView;
import com.crazyhitty.chdev.ks.predator.ui.views.OnboardingThirdView;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.OAuth;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/4/2017 10:48 PM
 * Description: First activity user will interact with, when he/she starts up the app for the first
 * time.
 */

public class OnboardActivity extends BaseAppCompatActivity implements AuthContract.View {
    private static final String TAG = "OnboardActivity";
    private static final int RC_PERMISSION = 1337;

    @BindView(R.id.view_pager_onboard)
    ViewPager viewPagerOnboard;
    @BindView(R.id.view_pager_indicator)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.button_skip)
    Button btnSkip;
    @BindView(R.id.image_button_previous)
    ImageButton imgBtnPrevious;
    @BindView(R.id.image_button_next)
    ImageButton imgBtnNext;

    private OnboardPagerAdapter mOnboardPagerAdapter;

    private AuthContract.Presenter mAuthPresenter;
    private CategoriesContract.Presenter mCategoriesPresenter;

    private CategoriesContract.View mCategoriesView = new CategoriesContract.View() {
        @Override
        public void showCategories(List<Category> categories) {
            dismissLoadingDialog();
            PredatorSharedPreferences.setOnboardingComplete(getApplicationContext(),
                    true);
            DashboardActivity.startActivity(getApplicationContext());
            finish();
        }

        @Override
        public void categoriesAvailable() {
            // Do nothing.
        }

        @Override
        public void categoriesUnavailable() {
            // Do nothing.
        }

        @Override
        public void setPresenter(CategoriesContract.Presenter presenter) {
            mCategoriesPresenter = presenter;
        }
    };

    /**
     * Start this activity with any extra intent flags
     *
     * @param context Current context of the application
     * @param flags   Intent flags
     */
    public static void startActivity(@NonNull Context context, int flags) {
        Intent intent = new Intent(context, OnboardActivity.class);
        intent.setFlags(flags);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        ButterKnife.bind(this);
        initViewPager();
        mCategoriesView.setPresenter(new CategoriesPresenter(mCategoriesView));
        setPresenter(new AuthPresenter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViewPager() {
        mOnboardPagerAdapter = new OnboardPagerAdapter();
        viewPagerOnboard.setAdapter(mOnboardPagerAdapter);
        viewPagerOnboard.setOffscreenPageLimit(2);

        pageIndicatorView.setViewPager(viewPagerOnboard);
        pageIndicatorView.setSelectedColor(ContextCompat.getColor(getApplicationContext(), R.color.color_accent));
        pageIndicatorView.setUnselectedColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_dark));
        pageIndicatorView.setRadius(getResources().getDimension(R.dimen.indicator_radius));
        pageIndicatorView.setAnimationType(AnimationType.WORM);

        viewPagerOnboard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        onFirstPageSelection();
                        break;
                    case 1:
                        onSecondPageSelection();
                        break;
                    case 2:
                        onThirdPageSelection();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        onFirstPageSelection();
    }

    private void onFirstPageSelection() {
        // disable back button
        imgBtnPrevious.setEnabled(false);
        imgBtnPrevious.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_dark));
    }

    private void onSecondPageSelection() {
        // enable next button
        imgBtnNext.setEnabled(true);
        // enable skip button
        btnSkip.setEnabled(true);
        // enable back button
        imgBtnPrevious.setEnabled(true);

        btnSkip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_black_dark));
        imgBtnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_black_dark));
        imgBtnPrevious.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_black_dark));

        /**
         * Post an event at {@link OnboardingSecondView#animateViews(OnboardSecondAnimateEvent)},
         * so that the animation can automatically be triggered.
         */
        EventBus.getDefault().post(new OnboardSecondAnimateEvent());
    }

    private void onThirdPageSelection() {
        // disable next button
        imgBtnNext.setEnabled(false);
        // disable skip button
        btnSkip.setEnabled(false);

        btnSkip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_dark));
        imgBtnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_primary_dark));

        /**
         * Post an event at {@link OnboardingThirdView#animateViews(OnboardThirdAnimateEvent)},
         * so that the animation can automatically be triggered.
         */
        EventBus.getDefault().post(new OnboardThirdAnimateEvent());
    }

    @OnClick(R.id.button_skip)
    public void onSkip() {
        onContinue(new OnboardContinueEvent(OnboardContinueEvent.TYPE.REGULAR));
    }

    @OnClick(R.id.image_button_previous)
    public void onPrevious() {
        if (viewPagerOnboard.getCurrentItem() != 0) {
            viewPagerOnboard.setCurrentItem(viewPagerOnboard.getCurrentItem() - 1, true);
        }
    }

    @OnClick(R.id.image_button_next)
    public void onNext() {
        if (viewPagerOnboard.getCurrentItem() != 2) {
            viewPagerOnboard.setCurrentItem(viewPagerOnboard.getCurrentItem() + 1, true);
        }
    }

    /**
     * Start token generation procedure on basis of user choice. Invoked from
     * {@link OnboardingThirdView#continueAsRegularUser()},
     * {@link OnboardingThirdView#continueAsRegisteredUser()}} or
     * {@link #onSkip()}
     *
     * @param onboardContinueEvent Event containing info about which type of registration user
     *                             wants to do.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContinue(OnboardContinueEvent onboardContinueEvent) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        showLoadingDialog(false);
        switch (onboardContinueEvent.getType()) {
            case REGULAR:
                mAuthPresenter.retrieveClientAuthToken(this);
                break;
            case REGISTERED:
                // Open oauth url in web browser for registering user.
                Intent intent = new Intent(Intent.ACTION_VIEW, OAuth.getProductHuntOAuthUri());
                startActivity(intent);
                break;
        }
    }

    /**
     * Invoked from {@link OAuthTokenHandlerActivity#onCreate(Bundle)}. It will give the token
     * provided by the ProductHunt website.
     *
     * @param oauthTokenEvent Event containing info about token
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOAuthTokenReceivedFromProductHunt(OAuthTokenEvent oauthTokenEvent) {
        mAuthPresenter.retrieveUserAuthToken(getApplicationContext(), oauthTokenEvent.getToken());
    }

    @Override
    public void onAuthTokenRetrieved(Bundle args, String message) {
        PredatorAccount.getAuthToken(this,
                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                PredatorSharedPreferences.getAuthTokenType(getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        mCategoriesPresenter.fetchCategories(getApplicationContext(), s);
                    }
                });
    }

    @Override
    public void unableToFetchAuthToken(String errorMessage) {
        dismissLoadingDialog();
        showErrorDialog(errorMessage, true);
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        mAuthPresenter = presenter;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                RC_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    onContinue(new OnboardContinueEvent(OnboardContinueEvent.TYPE.REGULAR));
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showLongToast(R.string.permission_denied_err_msg);
                }
                return;
            }
        }
    }
}