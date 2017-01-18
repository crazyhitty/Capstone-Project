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

package com.crazyhitty.chdev.ks.predator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/2/2017 9:06 PM
 * Description: Unavailable
 */

public class CoreUtils {
    private CoreUtils() {

    }

    /**
     * Set the fragment on the particular layout programmatically.
     *
     * @param fragmentManager FragmentManager
     * @param layoutResId     Layout which will contain this new fragment
     * @param fragment        Fragment to be set
     * @param addToBackStack  Add this fragment to fragment manager backstack or not
     */
    public static void setFragment(@NonNull FragmentManager fragmentManager,
                                   @IdRes int layoutResId,
                                   @NonNull Fragment fragment,
                                   boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(layoutResId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    /**
     * Set the fragment(non-support) on the particular layout programmatically.
     *
     * @param fragmentManager FragmentManager
     * @param layoutResId     Layout which will contain this new fragment
     * @param fragment        Fragment to be set
     * @param addToBackStack  Add this fragment to fragment manager backstack or not
     */
    public static void setFragment(@NonNull android.app.FragmentManager fragmentManager,
                                   @IdRes int layoutResId,
                                   @NonNull android.app.Fragment fragment,
                                   boolean addToBackStack) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(layoutResId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    /**
     * Starts a new activity.
     *
     * @param context  Current context of the application
     * @param activity Activity you want to start
     */
    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    /**
     * Starts a new activity with extra properties.
     *
     * @param context  Current context of the application
     * @param activity Activity you want to start
     * @param args     Bundle arguments you want to send with the intent
     * @param flags    Extra flags you want to send with the intent
     */
    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activity,
                                     @NonNull Bundle args,
                                     int flags) {
        Intent intent = new Intent(context, activity);
        intent.putExtras(args);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    /**
     * Append Bearer tag in front of the auth code so it can be used by the web services.
     *
     * @param token Authorization token
     * @return Auth Token to be used with product hunt web services
     */
    public static String getAuthToken(String token) {
        return "Bearer " + token;
    }
}
