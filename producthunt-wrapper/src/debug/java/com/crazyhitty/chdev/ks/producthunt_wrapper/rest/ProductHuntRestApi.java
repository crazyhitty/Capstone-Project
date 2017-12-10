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

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/26/2016 9:50 PM
 * Description: Unavailable
 */

public class ProductHuntRestApi {
    // Connection timeout
    private static final long CONNECTION_TIMEOUT = 30L;

    /**
     * This interceptor will add some predefined params/headers to each request.
     */
    private static Interceptor sInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .addHeader(Constants.ACCEPT, Constants.APPLICATION_JSON)
                    .addHeader(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
                    .addHeader(Constants.HOST, Constants.HOST_PRODUCT_HUNT)
                    .addHeader(Constants.IF_NONE_MATCH, Authorization.E_TAG)
                    .build();

            return chain.proceed(request);
        }
    };

    private ProductHuntRestApi() {

    }

    /**
     * Access the web services provided in {@link ProductHuntService}
     *
     * @return {@link ProductHuntService}
     */
    public static ProductHuntService getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Authorization.BASE_URL)
                .client(getOkHttpClient(true))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ProductHuntService.class);
    }

    public static ProductHuntService getSearchApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Authorization.SEARCH_URL)
                .client(getOkHttpClient(false))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ProductHuntService.class);
    }

    private static OkHttpClient getOkHttpClient(boolean withInterceptor) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        if (withInterceptor) {
            okHttpClientBuilder.addInterceptor(sInterceptor);
        }

        return okHttpClientBuilder.build();
    }
}
