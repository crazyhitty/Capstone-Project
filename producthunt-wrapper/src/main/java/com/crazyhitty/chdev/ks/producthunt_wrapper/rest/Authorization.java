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

package com.crazyhitty.chdev.ks.producthunt_wrapper.rest;

import com.crazyhitty.chdev.ks.producthunt_wrapper.BuildConfig;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/26/2016 9:51 PM
 * Description: Unavailable
 */

class Authorization {
    static final String BASE_URL = "https://api.producthunt.com/v1/";

    static final String REDIRECT_URI = "predator://com.crazyhitty.chdev.ks.predator/oauth2redirect";

    static final String API_KEY = BuildConfig.API_KEY;
    static final String API_SECRET = BuildConfig.API_SECRET;

    static final String SEARCH_URL = BuildConfig.SEARCH_URL;

    static final String X_ANGOLIA_AGENT = BuildConfig.X_ANGOLIA_AGENT;
    static final String X_ANGOLIA_APPLICATION_ID = BuildConfig.X_ANGOLIA_APPLICATION_ID;
    static final String X_ANGOLIA_API_KEY = BuildConfig.X_ANGOLIA_API_KEY;

    private Authorization() {

    }
}
