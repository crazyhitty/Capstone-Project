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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.crazyhitty.chdev.ks.predator.data.Constants;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/2/2017 11:07 AM
 * Description: Unavailable
 */

public class PredatorAccount {
    private PredatorAccount() {

    }

    /**
     * Add a new account to the device for this application, this account can either be either
     * contain a client token or user token.
     *
     * @param context       Current context of the application
     * @param accountName   Name of the account
     * @param accountType   Account type
     * @param authToken     Authentication token
     * @param authTokenType Type of the authentication token
     */
    public static void addAccount(Context context,
                                  String accountName,
                                  String accountType,
                                  String authToken,
                                  String authTokenType) {
        Account account = new Account(accountName, accountType);
        AccountManager accountManager = AccountManager.get(context);

        // Add a new account.
        accountManager.addAccountExplicitly(account, null, null);

        // Set the authentication code.
        accountManager.setAuthToken(account, authTokenType, authToken);
    }

    /**
     * Get a authentication token if available, otherwise add a new account and then get a new
     * authentication token.
     *
     * @param activity      Current activity
     * @param accountType   Name of the account
     * @param authTokenType Type of the authentication token
     * @return An string observable which will provide auth token
     */
    public static Observable<String> getAuthToken(final Activity activity,
                                                  final String accountType,
                                                  final String authTokenType) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                AccountManager accountManager = AccountManager.get(activity.getApplicationContext());
                accountManager.getAuthTokenByFeatures(accountType,
                        authTokenType,
                        null,
                        activity,
                        null,
                        null,
                        new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                Bundle bundle = null;
                                try {
                                    bundle = future.getResult();
                                    final String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                                    emitter.onNext(authToken);
                                    emitter.onComplete();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    emitter.onError(e);
                                }
                            }
                        },
                        null);
            }
        });
    }

    public static Account getAccount(Context context) {
        Account[] accounts = AccountManager.get(context)
                .getAccountsByType(Constants.Authenticator.PREDATOR_ACCOUNT_TYPE);
        if (accounts.length == 0) {
            return null;
        } else {
            return accounts[0];
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private static void removeAccount(Context context, Account account) {
        AccountManager.get(context).removeAccountExplicitly(account);
    }
}
