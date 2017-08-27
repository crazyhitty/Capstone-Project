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

import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.OAuthData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.OAuth;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/2/2017 9:59 AM
 * Description: Unavailable
 */

public class AuthPresenter implements AuthContract.Presenter {
    private static final String MSG_TOKEN_SUCCESS = "Token retrieved successfully";

    @NonNull
    private AuthContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    public AuthPresenter(@NonNull AuthContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        // Do nothing
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void retrieveClientAuthToken(final Context context) {
        mCompositeDisposable.add(ProductHuntRestApi.getApi()
                .oAuthClient(OAuth.getClientAuthRequestBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OAuthData>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.unableToFetchAuthToken(e.getMessage());
                    }

                    @Override
                    public void onNext(OAuthData oAuthData) {
                        Bundle args = new Bundle();
                        args.putString(AccountManager.KEY_ACCOUNT_NAME, Constants.Authenticator.PRODUCT_HUNT);
                        args.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.Authenticator.PREDATOR_ACCOUNT_TYPE);
                        args.putString(AccountManager.KEY_AUTHTOKEN, oAuthData.getAccessToken());

                        PredatorAccount.addAccount(context,
                                Constants.Authenticator.PRODUCT_HUNT,
                                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                                oAuthData.getAccessToken(),
                                Constants.Authenticator.AUTH_TYPE_CLIENT);

                        // valid token is retrieved
                        PredatorSharedPreferences.setValidToken(context, true);
                        PredatorSharedPreferences.setAuthTokenType(context, Constants.Authenticator.AUTH_TYPE_CLIENT);

                        mView.onAuthTokenRetrieved(args, MSG_TOKEN_SUCCESS);
                    }
                }));
    }

    @Override
    public void retrieveUserAuthToken(final Context context, String token) {
        mCompositeDisposable.add(ProductHuntRestApi.getApi()
                .oAuthUser(OAuth.geUserAuthRequestBody(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OAuthData>() {
                    @Override
                    public void onComplete() {
                        // Done
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.unableToFetchAuthToken(e.getMessage());
                    }

                    @SuppressWarnings("MissingPermission")
                    @Override
                    public void onNext(OAuthData oAuthData) {
                        Bundle args = new Bundle();
                        args.putString(AccountManager.KEY_ACCOUNT_NAME, Constants.Authenticator.PRODUCT_HUNT);
                        args.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.Authenticator.PREDATOR_ACCOUNT_TYPE);
                        args.putString(AccountManager.KEY_AUTHTOKEN, oAuthData.getAccessToken());

                        PredatorAccount.addAccount(context,
                                Constants.Authenticator.PRODUCT_HUNT,
                                Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                                oAuthData.getAccessToken(),
                                Constants.Authenticator.AUTH_TYPE_USER);

                        // valid token is retrieved
                        PredatorSharedPreferences.setValidToken(context, true);
                        PredatorSharedPreferences.setAuthTokenType(context, Constants.Authenticator.AUTH_TYPE_USER);

                        mView.onAuthTokenRetrieved(args, MSG_TOKEN_SUCCESS);
                    }
                }));
    }
}