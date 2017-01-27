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

import android.net.Uri;

import java.util.HashMap;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/6/2017 3:17 PM
 * Description: Unavailable
 */

public class OAuth {
    public static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "public private";

    private OAuth() {

    }

    public static Uri getProductHuntOAuthUri() {
        return Uri.parse(Authorization.BASE_URL + ApiUrls.OAUTH_USER_AUTHENTICATION)
                .buildUpon()
                .appendQueryParameter(Constants.CLIENT_ID, Authorization.API_KEY)
                .appendQueryParameter(Constants.REDIRECT_URI, Authorization.REDIRECT_URI)
                .appendQueryParameter(Constants.RESPONSE_TYPE, RESPONSE_TYPE)
                .appendQueryParameter(Constants.SCOPE, SCOPE)
                .build();
    }

    public static HashMap<String, String> getClientAuthRequestBody() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.CLIENT_ID, Authorization.API_KEY);
        params.put(Constants.CLIENT_SECRET, Authorization.API_SECRET);
        params.put(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
        return params;
    }

    public static HashMap<String, String> geUserAuthRequestBody(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.CLIENT_ID, Authorization.API_KEY);
        params.put(Constants.CLIENT_SECRET, Authorization.API_SECRET);
        params.put(Constants.GRANT_TYPE, Constants.AUTHORIZATION_CODE);
        params.put(Constants.REDIRECT_URI, Authorization.REDIRECT_URI);
        params.put(Constants.CODE, token);
        return params;
    }
}
