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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/13/18 1:01 PM
 * Description: Provides the internet status information.
 */
class InternetHelper @Inject constructor(private val context: Context) {
    private val connectivityManager: ConnectivityManager = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Callback for providing status internet status updates.
     */
    private var internetStatusCallback: InternetStatusCallback? = null

    /**
     * This broadcast receiver listens for internet connectivity changes.
     */
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            internetStatusCallback?.internetAvailability(isAvailable())
        }
    }

    /**
     * Checks if internet is currently available or not.
     *
     * @return
     * true, it available, otherwise false.
     */
    fun isAvailable(): Boolean = connectivityManager.activeNetworkInfo
                .isConnectedOrConnecting

    /**
     * Set the callback which will receive internet status updates. This will help you get
     * internet availability updates.
     */
    fun setInternetStatusCallback(internetStatusCallback: InternetStatusCallback) {
        this.internetStatusCallback = internetStatusCallback

        // Start listening for network connection availability.
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * Remove the callback if no longer required.
     */
    fun removeInternetStatusCallback() {
        this.internetStatusCallback = null

        // Stop listening for network connection availability.
        context.applicationContext.unregisterReceiver(broadcastReceiver)
    }

    interface InternetStatusCallback {
        fun internetAvailability(status: Boolean)
    }
}