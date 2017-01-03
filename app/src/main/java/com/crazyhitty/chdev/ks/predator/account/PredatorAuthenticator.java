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

package com.crazyhitty.chdev.ks.predator.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.ui.activities.AuthenticatorActivity;

import java.lang.ref.WeakReference;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/1/2017 9:14 PM
 * Description: Authenticator account manager for predator.
 */

public class PredatorAuthenticator extends AbstractAccountAuthenticator {
    private static final String TAG = "PredatorAuthenticator";

    private WeakReference<Context> mContextWeakReference;

    public PredatorAuthenticator(Context context) {
        super(context);
        mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        // Check if the context was null or not.
        if (mContextWeakReference.get() == null) {
            return null;
        }

        Intent intent = new Intent(mContextWeakReference.get(), AuthenticatorActivity.class);
        intent.putExtra(Constants.Authenticator.ACCOUNT_TYPE, accountType);
        intent.putExtra(Constants.Authenticator.AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Constants.Authenticator.ACCOUNT_AUTHENTICATION_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Check if the context was null or not.
        if (mContextWeakReference.get() == null) {
            return null;
        }

        // Check if any authToken is available or not.
        AccountManager accountManager = AccountManager.get(mContextWeakReference.get());
        String authToken = accountManager.peekAuthToken(account, authTokenType);

        // If the authToken is available, return it.
        if (!TextUtils.isEmpty(authToken)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Authenticator.ACCOUNT_NAME, account.name);
            bundle.putString(Constants.Authenticator.ACCOUNT_TYPE, account.type);
            bundle.putString(Constants.Authenticator.AUTH_TOKEN, authToken);
            return bundle;
        }

        // If the authToken is unavailable, retry for a new one.
        Intent intent = new Intent(mContextWeakReference.get(), AuthenticatorActivity.class);
        intent.putExtra(Constants.Authenticator.ACCOUNT_TYPE, account.type);
        intent.putExtra(Constants.Authenticator.AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Constants.Authenticator.ACCOUNT_AUTHENTICATION_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
