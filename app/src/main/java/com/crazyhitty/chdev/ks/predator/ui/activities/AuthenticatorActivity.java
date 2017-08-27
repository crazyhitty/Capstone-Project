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
import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthContract;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthPresenter;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.ui.views.LoadingDialogHeaderView;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/1/2017 10:14 PM
 * Description: Enables a user to register with product hunt. It is a type of dialog.
 */

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements AuthContract.View {
    private static final String TAG = "AuthenticatorActivity";

    @BindView(R.id.relative_layout_authenticator)
    RelativeLayout relativeLayoutAuthenticator;
    @BindView(R.id.loading_dialog_header_view)
    LoadingDialogHeaderView loadingDialogHeaderView;
    @BindView(R.id.text_view_content)
    TextView txtContent;
    @BindView(R.id.button_retry)
    Button btnRetry;

    private AuthContract.Presenter mAuthPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageThemes();
        setContentView(R.layout.activity_authenticator);
        ButterKnife.bind(this);
        manageBackgroundColor();
        setPresenter(new AuthPresenter(this));
        mAuthPresenter.subscribe();
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            mAuthPresenter.retrieveClientAuthToken(this);
        } else {
            loadingDialogHeaderView.setType(LoadingDialogHeaderView.TYPE.FAILURE);
            txtContent.setText(R.string.activity_authenticator_network_error);
            btnRetry.setVisibility(View.VISIBLE);
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
        mAuthPresenter.unSubscribe();
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        mAuthPresenter = presenter;
    }

    @Override
    public void onAuthTokenRetrieved(Bundle args, String message) {
        setAccountAuthenticatorResult(args);
        setResult(RESULT_OK, new Intent().putExtras(args));
        finish();
    }

    @Override
    public void unableToFetchAuthToken(String errorMessage) {
        Logger.d(TAG, "unableToFetchAuthToken: " + errorMessage);
        loadingDialogHeaderView.setType(LoadingDialogHeaderView.TYPE.FAILURE);
        txtContent.setText(R.string.authentication_failure);
        btnRetry.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClick() {
        finish();
    }

    @OnClick(R.id.button_retry)
    public void onRetryClick() {
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            loadingDialogHeaderView.setType(LoadingDialogHeaderView.TYPE.LOADING);
            txtContent.setText(R.string.activity_authenticator_message);
            btnRetry.setVisibility(View.GONE);
            mAuthPresenter.retrieveClientAuthToken(this);
        } else {
            loadingDialogHeaderView.setType(LoadingDialogHeaderView.TYPE.FAILURE);
            txtContent.setText(R.string.activity_authenticator_network_error);
            btnRetry.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), R.string.not_connected_to_network_err, Toast.LENGTH_SHORT).show();
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

    private void manageBackgroundColor() {
        switch (PredatorSharedPreferences.getCurrentTheme(getApplicationContext())) {
            case LIGHT:
                relativeLayoutAuthenticator.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.background_color));
                break;
            case DARK:
                relativeLayoutAuthenticator.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.background_color_inverse));
                break;
            case AMOLED:
                relativeLayoutAuthenticator.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.background_color_amoled));
                break;
        }
    }
}
