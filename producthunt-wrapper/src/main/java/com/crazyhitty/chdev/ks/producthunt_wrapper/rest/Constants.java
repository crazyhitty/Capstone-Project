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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/26/2016 10:35 PM
 * Description: Unavailable
 */

class Constants {
    // core request headers
    static final String ACCEPT = "Accept";
    static final String CONTENT_TYPE = "Content-Type";
    static final String AUTHORIZATION = "Authorization";
    static final String HOST = "Host";
    static final String IF_NONE_MATCH = "If-None-Match";

    // core request values
    static final String APPLICATION_JSON = "application/json";
    static final String HOST_PRODUCT_HUNT = "api.producthunt.com";
    static final String BEARER = "Bearer ";

    // posts
    static final String CATEGORY_NAME = "category_name";
    static final String DAYS_AGO = "days_ago";

    // comments
    static final String POST_ID = "post_id";
    static final String PAGE = "page";
    static final String PER_PAGE = "per_page";

    // user profile
    static final String USER_ID = "user_id";

    // oauth
    static final String CLIENT_ID = "client_id";
    static final String CLIENT_SECRET = "client_secret";
    static final String GRANT_TYPE = "grant_type";
    static final String CLIENT_CREDENTIALS = "client_credentials";
    static final String REDIRECT_URI = "redirect_uri";
    static final String RESPONSE_TYPE = "response_type";
    static final String SCOPE = "scope";
    static final String CODE = "code";
    static final String AUTHORIZATION_CODE = "authorization_code";

    private Constants() {

    }
}
