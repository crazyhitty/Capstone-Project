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

import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/9/2017 4:00 PM
 * Description: Unavailable
 */

public class RecyclerViewItemAnimations {
    private static final String TAG = "RecyclerViewItemAnimations";

    private RecyclerViewItemAnimations() {

    }

    public static void setListTranslateYAnim(View view, int startDelayMs) {
        view.setTranslationY(ScreenUtils.dpToPx(view.getContext(), 16.0f));
        view.setAlpha(0.0f);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .alpha(1.0f)
                .setStartDelay(startDelayMs)
                .start();
    }
}
