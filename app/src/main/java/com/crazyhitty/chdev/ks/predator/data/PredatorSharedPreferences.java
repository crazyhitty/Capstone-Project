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

package com.crazyhitty.chdev.ks.predator.data;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/6/2017 11:08 AM
 * Description: Unavailable
 */

public class PredatorSharedPreferences {
    private static final String SP_TAG = "predator_shared_preferences";

    private PredatorSharedPreferences() {

    }

    public static void clear(Context context) {
        SharedPreferencesManager.clear(context);
    }

    public static void setValidToken(Context context, boolean status) {
        SharedPreferencesManager.saveBoolean(context,
                Constants.SharedPreferences.IS_TOKEN_VALID,
                status);
    }

    public static boolean hasValidToken(Context context) {
        return SharedPreferencesManager.getBoolean(context,
                Constants.SharedPreferences.IS_TOKEN_VALID,
                false);
    }

    public static void setOnboardingComplete(Context context, boolean status) {
        SharedPreferencesManager.saveBoolean(context,
                Constants.SharedPreferences.IS_ONBOARDING_COMPLETE,
                status);
    }

    public static boolean isOnboardingComplete(Context context) {
        return SharedPreferencesManager.getBoolean(context,
                Constants.SharedPreferences.IS_ONBOARDING_COMPLETE,
                false);
    }

    public static void setAuthTokenType(Context context, String type) {
        SharedPreferencesManager.saveString(context,
                Constants.SharedPreferences.AUTH_TOKEN_TYPE,
                type);
    }

    public static String getAuthTokenType(Context context) {
        return SharedPreferencesManager.getString(context,
                Constants.SharedPreferences.AUTH_TOKEN_TYPE,
                Constants.Authenticator.AUTH_TYPE_CLIENT);
    }

    public static boolean isSyncEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.settings_background_sync_key), false);
    }

    public static long getSyncIntervalInMillis(Context context) {
        String hours = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.settings_sync_interval_key),
                        context.getString(R.string.settings_sync_interval_default_value));

        return DateUtils.hoursToMillis(hours);
    }

    public static boolean isExperimentalFeaturesEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.settings_enable_experimental_features_key), false);
    }

    public static String getCurrentFont(Context context) {
        String fontName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.settings_change_font_key),
                        context.getString(R.string.settings_change_font_default_value));
        if (TextUtils.equals(fontName, context.getString(R.string.settings_change_font_default_value))) {
            return null;
        } else {
            return fontName;
        }
    }

    public static void restoreDefaultFont(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.settings_change_font_key),
                        context.getString(R.string.settings_change_font_default_value))
                .apply();
    }

    public static boolean areNotificationsEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.settings_notifications_key), false);
    }

    public static THEME_TYPE getCurrentTheme(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.settings_manage_themes_key),
                        context.getString(R.string.settings_manage_themes_default_value))) {
            case "light":
                return THEME_TYPE.LIGHT;
            case "dark":
                return THEME_TYPE.DARK;
            case "amoled":
                return THEME_TYPE.AMOLED;
            default:
                return THEME_TYPE.LIGHT;
        }
    }

    public enum THEME_TYPE {
        LIGHT,
        DARK,
        AMOLED
    }

    private static class SharedPreferencesManager {
        private SharedPreferencesManager() {

        }

        private static void saveBoolean(Context context, String key, boolean value) {
            context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(key, value)
                    .apply();
        }

        private static void saveString(Context context, String key, String value) {
            context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .edit()
                    .putString(key, value)
                    .apply();
        }

        private static void saveInt(Context context, String key, int value) {
            context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(key, value)
                    .apply();
        }

        private static boolean getBoolean(Context context, String key) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getBoolean(key, false);
        }

        private static boolean getBoolean(Context context, String key, boolean defaultValue) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getBoolean(key, defaultValue);
        }

        private static String getString(Context context, String key) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getString(key, null);
        }

        private static String getString(Context context, String key, String defaultValue) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getString(key, defaultValue);
        }

        private static int getInt(Context context, String key) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getInt(key, -1);
        }

        private static int getInt(Context context, String key, int defaultValue) {
            return context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .getInt(key, defaultValue);
        }

        private static void clear(Context context) {
            context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
        }
    }
}
