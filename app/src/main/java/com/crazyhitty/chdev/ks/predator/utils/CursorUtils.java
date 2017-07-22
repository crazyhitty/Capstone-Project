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

import android.database.Cursor;
import android.support.annotation.NonNull;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/9/2017 10:55 AM
 * Description: Utility class for getting values from cursor.
 */

public class CursorUtils {
    private CursorUtils() {

    }

    /**
     * Get the string value for that particular column.
     *
     * @param cursor     Cursor containing database data.
     * @param columnName Column name, whose data you will be getting.
     * @return String value of that particular column.
     */
    public static String getString(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    /**
     * Get the int value for that particular column.
     *
     * @param cursor     Cursor containing database data.
     * @param columnName Column name, whose data you will be getting.
     * @return int value of that particular column.
     */
    public static int getInt(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    /**
     * Get the long value for that particular column.
     *
     * @param cursor     Cursor containing database data.
     * @param columnName Column name, whose data you will be getting.
     * @return long value of that particular column.
     */
    public static long getLong(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
