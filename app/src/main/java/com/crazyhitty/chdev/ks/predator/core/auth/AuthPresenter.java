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

package com.crazyhitty.chdev.ks.predator.core.auth;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.OAuthClientOnlyData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/2/2017 9:59 AM
 * Description: Unavailable
 */

public class AuthPresenter implements AuthContract.Presenter {
    private static final String MSG_TOKEN_SUCCESS = "Token retrieved successfully: %s";

    @NonNull
    private AuthContract.View mView;

    private CompositeSubscription mCompositeSubscription;

    public AuthPresenter(@NonNull AuthContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        // Do nothing
    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }

    @Override
    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    public void retrieveClientAuthToken(final Context context) {
        // check if the

        mCompositeSubscription.add(ProductHuntRestApi.getApi()
                .oAuthClient()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OAuthClientOnlyData>() {
                    @Override
                    public void onCompleted() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.unableToFetchAuthToken(e.getMessage());
                    }

                    @SuppressWarnings("MissingPermission")
                    @Override
                    public void onNext(OAuthClientOnlyData oAuthClientOnlyData) {
                        Bundle args = new Bundle();
                        args.putString(AccountManager.KEY_ACCOUNT_NAME, Constants.Authenticator.PRODUCT_HUNT);
                        args.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.Authenticator.AUTH_TYPE_CLIENT);
                        args.putString(AccountManager.KEY_AUTHTOKEN, oAuthClientOnlyData.getAccessToken());

                        mView.onAuthTokenRetrieved(args,
                                String.format(MSG_TOKEN_SUCCESS, oAuthClientOnlyData.getAccessToken()));

                        PredatorAccount.addAccount(context,
                                Constants.Authenticator.PRODUCT_HUNT,
                                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                                oAuthClientOnlyData.getAccessToken(),
                                Constants.Authenticator.AUTH_TYPE_CLIENT);
                    }
                }));
    }

    @Override
    public void retrieveUserAuthToken() {
        // Do nothing
    }
}