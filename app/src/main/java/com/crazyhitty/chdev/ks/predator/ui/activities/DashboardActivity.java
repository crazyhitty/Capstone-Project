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

package com.crazyhitty.chdev.ks.predator.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.OAuthClientOnlyData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;
import com.google.gson.Gson;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:32 PM
 * Description: This activity holds a navigation menu as well as acts as a main container for mostly
 * the entire app.
 */

public class DashboardActivity extends BaseAppCompatActivity {
    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Button btnTest = (Button) findViewById(R.id.button_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testApi();
            }
        });
    }

    private void testApi() {
        ProductHuntRestApi.getApi()
                .oAuthClient()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OAuthClientOnlyData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "testApi: completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "testApi: onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(OAuthClientOnlyData oAuthClientOnlyData) {
                        Log.d(TAG, "testApi: onNext: " + new Gson().toJson(oAuthClientOnlyData));
                    }
                });
    }
}
