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

package com.crazyhitty.chdev.ks.predator.helper

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.crazyhitty.chdev.ks.predator.data.Constants
import io.reactivex.Single
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/15/18 3:03 PM
 * Description: Manages account related operations for the application.
 */
class AccountHelper @Inject constructor(private val context: Context) {

    /**
     * Adds a new account to the account manager.
     *
     * @param accountName   Name of the account
     * @param accountType   Type of the account
     * @param authToken     Actual token to be saved
     * @param authTokenType Type of authentication token, possible inputs are:
     *                      [Constants.Authenticator.AUTH_TYPE_CLIENT] -> Normal user
     *                      [Constants.Authenticator.AUTH_TYPE_USER] -> Logged in user
     */
    fun addAccount(accountName: String,
                   accountType: String,
                   authToken: String,
                   authTokenType: String) {
        AccountManager.get(context.applicationContext).apply {
            // Create account object.
            val account = Account(accountName, accountType)

            // Add a new account.
            addAccountExplicitly(account, null, null)

            // Set the authentication code.
            setAuthToken(account, authTokenType, authToken)
        }
    }

    /**
     * Get the auth token from current account. It would try to fetch it automatically if not
     * present currently.
     *
     * @param activity
     * @param authTokenType Type of authentication token, possible inputs are:
     *                      [Constants.Authenticator.AUTH_TYPE_CLIENT] -> Normal user
     *                      [Constants.Authenticator.AUTH_TYPE_USER] -> Logged in user
     *
     * @return
     * Single which will either provide the auth token string or error if something happens while
     * fetching token.
     */
    fun getAuthToken(activity: Activity, authTokenType: String): Single<String> {
        return Single.create({ emitter ->
            AccountManager.get(context.applicationContext).apply {
                getAuthTokenByFeatures(Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                        authTokenType,
                        null,
                        activity,
                        null,
                        null,
                        { future: AccountManagerFuture<Bundle>? ->
                            if (future?.isCancelled == true) {
                                emitter.onError(RuntimeException("Auth request was cancelled"))
                            } else {
                                val authToken = future?.result
                                        ?.getString(AccountManager.KEY_AUTHTOKEN)
                                if (authToken == null) {
                                    emitter.onError(NullPointerException("Auth token is null"))
                                } else {
                                    emitter.onSuccess(authToken)
                                }
                            }
                        },
                        null)
            }
        })
    }

    /**
     * Fetches the available account from AccountManager API.
     *
     * @return
     * Current account associated with this application. It will return null if no account
     * associated with this application is found.
     */
    fun getAccount(): Account? {
        return AccountManager.get(context.applicationContext)
                .getAccountsByType(Constants.Authenticator.PREDATOR_ACCOUNT_TYPE)
                .takeIf { arrayOfAccounts -> arrayOfAccounts.isNotEmpty() }
                ?.get(0)
    }

    /**
     * Removes the account. Should not be used in **production environment**.
     *
     * @param account   Account to be removed
     *
     * @return
     * True, if account is removed, otherwise false.
     */
    @TestOnly
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun removeAccount(account: Account): Boolean {
        return AccountManager.get(context)
                .removeAccountExplicitly(account)
    }
}