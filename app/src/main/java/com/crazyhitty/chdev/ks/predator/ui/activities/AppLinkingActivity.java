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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.appLinking.AppLinkingContract;
import com.crazyhitty.chdev.ks.predator.core.appLinking.AppLinkingPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.ui.views.AppLinkingDialogHeaderView;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/30/2017 01:20 AM
 * Description: Used for managing app indexing.
 */

public class AppLinkingActivity extends Activity implements AppLinkingContract.View {
    private static final String TAG = "AppLinkingActivity";
    private static final int HANDLER_MSG_OPEN_POST_DETAILS = 1;
    private static final long HANDLER_DURATION_OPEN_POST_DETAILS = TimeUnit.SECONDS.toMillis(1);
    private static final String HANDLER_EXTRA_POST_ID = "post_id";

    @BindView(R.id.app_linking_dialog_header_view)
    AppLinkingDialogHeaderView appLinkingDialogHeaderView;
    @BindView(R.id.text_view_content)
    TextView txtContent;
    @BindView(R.id.button_retry)
    Button btnRetry;

    private AppLinkingContract.Presenter mAppLinkingPresenter;

    private Handler mOpenPostDetailsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MSG_OPEN_POST_DETAILS:
                    PostDetailsActivity.startActivity(getApplicationContext(),
                            msg.getData().getInt(HANDLER_EXTRA_POST_ID));
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageThemes();
        setContentView(R.layout.activity_app_linking);
        ButterKnife.bind(this);
        setPresenter(new AppLinkingPresenter(this));
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            handleIntent(getIntent());
        } else {
            appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.FAILURE);
            txtContent.setText(R.string.activity_app_linking_network_error);
            btnRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            handleIntent(intent);
        } else {
            appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.FAILURE);
            txtContent.setText(R.string.activity_app_linking_network_error);
            btnRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Attach calligraphy context if font name is not null. Font name will only be null if user
        // has selected default font.

        if (TextUtils.isEmpty(PredatorSharedPreferences.getCurrentFont(newBase))) {
            super.attachBaseContext(newBase);
        } else {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOpenPostDetailsHandler.removeMessages(HANDLER_MSG_OPEN_POST_DETAILS);
        mAppLinkingPresenter.unSubscribe();
    }

    @Override
    public void setPresenter(AppLinkingContract.Presenter presenter) {
        mAppLinkingPresenter = presenter;
    }

    @Override
    public void showPostDetails(int postId) {
        appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.SUCCESS);

        Message message = new Message();
        message.what = HANDLER_MSG_OPEN_POST_DETAILS;

        Bundle bundle = new Bundle();
        bundle.putInt(HANDLER_EXTRA_POST_ID, postId);
        message.setData(bundle);

        mOpenPostDetailsHandler.sendMessageDelayed(message, HANDLER_DURATION_OPEN_POST_DETAILS);
    }

    @Override
    public void unableToFetchPostDetails() {
        appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.FAILURE);
        txtContent.setText(R.string.activity_app_linking_unable_fetching_details_error);
        btnRetry.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClick() {
        finish();
    }

    @OnClick(R.id.button_retry)
    public void onRetryClick() {
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.LOADING);
            txtContent.setText(R.string.activity_app_linking_loading);
            btnRetry.setVisibility(View.GONE);
            handleIntent(getIntent());
        } else {
            appLinkingDialogHeaderView.setType(AppLinkingDialogHeaderView.TYPE.FAILURE);
            txtContent.setText(R.string.activity_app_linking_network_error);
            btnRetry.setVisibility(View.VISIBLE);
        }
    }

    private void manageThemes() {
        switch (PredatorSharedPreferences.getCurrentTheme(getApplicationContext())) {
            case LIGHT:
                setTheme(R.style.DialogActivity_NoTitle_Light);
                break;
            case DARK:
                setTheme(R.style.DialogActivity_NoTitle_Dark);
                break;
            case AMOLED:
                setTheme(R.style.DialogActivity_NoTitle_Amoled);
                break;
        }
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String slug = appLinkData.getLastPathSegment();
            getPostDetailsFromSlug(slug);
        }
    }

    private void getPostDetailsFromSlug(final String slug) {
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
                        mAppLinkingPresenter.fetchPostDetailsFromSlug(s, slug);
                    }
                });
    }
}
