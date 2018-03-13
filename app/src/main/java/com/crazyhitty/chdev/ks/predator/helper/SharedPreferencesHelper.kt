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

import android.content.Context
import android.content.SharedPreferences
import com.crazyhitty.chdev.ks.predator.data.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/16/18 11:02 AM
 * Description: Save/fetch application specific data from shared preferences.
 */
@Singleton
class SharedPreferencesHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }

    fun setValidToken(status: Boolean) {
        sharedPreferences.edit()
                .putBoolean(Constants.SharedPreferences.IS_TOKEN_VALID, status)
                .apply()
    }

    fun setAuthTokenType(type: String) {
        sharedPreferences.edit()
                .putString(Constants.SharedPreferences.AUTH_TOKEN_TYPE, type)
                .apply()
    }

    fun getAuthTokenType(): String {
        return sharedPreferences.getString(Constants.SharedPreferences.AUTH_TOKEN_TYPE,
                Constants.Authenticator.AUTH_TYPE_CLIENT)
    }
}