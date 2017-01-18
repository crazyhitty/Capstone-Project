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

package com.crazyhitty.chdev.ks.producthunt_wrapper.utils;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/16/2017 1:36 PM
 * Description: Unavailable
 */

public class ImageUtils {
    private ImageUtils() {

    }

    public static String getCustomPostThumbnailImageUrl(String imageUrl, int heightPx, int widthPx) {
        return imageUrl.replace("h=570&w=430", "h=" + heightPx + "&w=" + widthPx);
    }

    public static String getCustomUserThumbnailUrl(String imageUrl, int heightPx, int widthPx) {
        return imageUrl.replace("w=100&h=100", "w=" + widthPx + "&h=" + heightPx);
    }

    public static String getCustomMediaImageThumbnailUrl(String imageUrl, int heightPx, int widthPx) {
        return imageUrl.concat("h=" + heightPx + "&w=" + widthPx);
    }

    public static String getCustomCommentUserImageThumbnailUrl(String imageUrl, int heightPx, int widthPx) {
        return imageUrl.replace("w=100&h=100", "w=" + widthPx + "&h=" + heightPx);
    }
}
