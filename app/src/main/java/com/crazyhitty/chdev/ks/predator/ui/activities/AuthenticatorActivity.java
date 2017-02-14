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
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthContract;
import com.crazyhitty.chdev.ks.predator.core.auth.AuthPresenter;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/1/2017 10:14 PM
 * Description: Enables a user to register with product hunt. It is a type of dialog.
 */

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements AuthContract.View {
    private AuthContract.Presenter mAuthPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        ButterKnife.bind(this);
        setPresenter(new AuthPresenter(this));
        Toast.makeText(getApplicationContext(), R.string.not_yet_implemented, Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick(R.id.button_continue)
    void onContinue() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mAuthPresenter.retrieveClientAuthToken(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    }
}
