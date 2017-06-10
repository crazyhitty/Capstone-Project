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
import android.content.ContentResolver;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/28/2016 11:02 PM
 * Description: Unavailable
 */

public class MainApplication extends Application {
    private static WeakReference<ContentResolver> sContentResolverWeakReference;

    static {
        // Supporting vector drawable resources on pre lollipop devices.
        // Source: http://stackoverflow.com/a/38012842
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static ContentResolver getContentResolverInstance() {
        return sContentResolverWeakReference.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize stetho.
        Stetho.initializeWithDefaults(getApplicationContext());

        // Create a static reference to content resolver so that it can be accessed anywhere.
        sContentResolverWeakReference = new WeakReference<ContentResolver>(getContentResolver());

        // Initialize fresco.
        Fresco.initialize(this);

        // Initialize calligraphy.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
