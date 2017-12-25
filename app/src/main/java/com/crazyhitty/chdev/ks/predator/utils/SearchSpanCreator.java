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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/17/17 8:06 PM
 * Description: Unavailable
 */

public class SearchSpanCreator {
    private static final String TAG = "SearchSpanCreator";

    private static SearchSpanCreator sSearchSpanCreator;

    public static final int COLOR_NONE = -1;

    public static SearchSpanCreator getInstance() {
        if (sSearchSpanCreator == null) {
            sSearchSpanCreator = new SearchSpanCreator();
        }
        return sSearchSpanCreator;
    }

    private SearchSpanCreator() {

    }

    public @Nullable Spannable create(int color, @NonNull String text, @NonNull List<String> selections) {
        if (color == COLOR_NONE)  {
            return null;
        }

        if (TextUtils.isEmpty(text)) {
            return null;
        }

        String patternString = getPattern(selections);
        Logger.d(TAG, "create: pattern: " + patternString);
        if (TextUtils.isEmpty(patternString)) {
            return null;
        }

        Spannable spannable = new SpannableString(text);

        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            Logger.d(TAG, String.format("create: group: %s; start: %s; end: %s",
                    matcher.group(),
                    matcher.start(),
                    matcher.end()));
            spannable.setSpan(new ForegroundColorSpan(color),
                    matcher.start(),
                    matcher.end(),
                    0);
        }

        return spannable;
    }

    private @Nullable String getPattern(@NonNull List<String> selections) {
        if (selections.isEmpty()) {
            return null;
        }

        String pattern = "(";

        if (selections.size() == 1) {
            return pattern.concat(selections.get(0) + ")");
        }

        for (String selection : selections) {
            pattern = pattern.concat(selection + "|");
        }

        return pattern.substring(0, pattern.length() - 1).concat(")");
    }
}
