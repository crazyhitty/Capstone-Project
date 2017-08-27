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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/1/2017 10:50 PM
 * Description: Unavailable
 */

public class Constants {
    private Constants() {

    }

    public static class Authenticator {
        public static final String ACCOUNT_TYPE = "account_type";
        public static final String AUTH_TOKEN_TYPE = "auth_token_type";
        public static final String PRODUCT_HUNT = "Product Hunt";
        public static final String AUTH_TYPE_CLIENT = "auth_type_client";
        public static final String AUTH_TYPE_USER = "auth_type_user";
        public static final String PREDATOR_ACCOUNT_TYPE = "com.crazyhitty.chdev.ks.predator";

        private Authenticator() {

        }
    }

    public static class Sync {
        public static final int ON = 1;
        public static final int OFF = 0;

        private Sync() {

        }
    }

    public static class Media {
        public static final String IMAGE = "image";
        public static final String VIDEO = "video";
        public static final String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";

        private Media() {

        }
    }

    public static class Posts {
        public static final String CATEGORY_ALL = "all";

        private Posts() {

        }
    }

    public static class Library {
        public static final String LIBRARIES = "libraries";
        public static final String LIBRARY_TITLE = "library_title";
        public static final String LIBRARY_CREATOR = "library_creator";
        public static final String LIBRARY_LICENSE_TYPE = "library_license_type";
        public static final String LIBRARY_REDIRECT_URL = "library_redirect_url";

        private Library() {

        }
    }

    public static class About {
        public static final String URL_PRIVACY_POLICY = "http://www.crazyhitty.com/privacy-policy-for-predator-for-product-hunt/";
        public static final String URL_GITHUB_KAJAL = "https://github.com/kajal-mittal";
        public static final String URL_GITHUB = "https://github.com/crazyhitty/Capstone-Project";
        public static final String URL_GOOGLE_PLUS = "https://plus.google.com/communities/102250921213849521349";
        public static final String URL_DEVELOPER_WEBSITE = "http://crazyhitty.com/";
        public static final String URL_DEVELOPER_IMAGE = "https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces";
        public static final String MAIL_ID = "cr42yh17m4n@gmail.com";
        public static final int DEV_KARTIK_PRODUCT_HUNT_USER_ID = 425173;

        private About() {

        }
    }

    public static class SharedPreferences {
        public static final String IS_TOKEN_VALID = "is_token_valid";
        public static final String IS_ONBOARDING_COMPLETE = "is_onboarding_complete";
        public static final String AUTH_TOKEN_TYPE = "auth_token_type";
        public static final String SYNC_INTERVAL = "sync_interval";

        private SharedPreferences() {

        }
    }
}
