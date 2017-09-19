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

package com.crazyhitty.chdev.ks.predator;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/28/2016 11:02 PM
 * Description: Unavailable
 */

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";

    static {
        // Supporting vector drawable resources on pre lollipop devices.
        // Source: http://stackoverflow.com/a/38012842
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize stetho.
        Stetho.initializeWithDefaults(getApplicationContext());

        // Initialize Predator Database.
        PredatorDatabase.init(getApplicationContext());

        // Initialize fresco.
        Fresco.initialize(this);

        // Initialize calligraphy.
        if (!TextUtils.isEmpty(PredatorSharedPreferences.getCurrentFont(getApplicationContext()))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(String.format("fonts/%s", PredatorSharedPreferences.getCurrentFont(getApplicationContext())))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
    }

    public static void reInitializeCalligraphy(Context context, String fontName) {
        if (!TextUtils.equals(fontName, context.getString(R.string.settings_change_font_system))) {
            Logger.d(TAG, "re initializing calligraphy");
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(String.format("fonts/%s", fontName))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
    }
}
