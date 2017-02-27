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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crazyhitty.chdev.ks.predator.account.PredatorAccount;
import com.crazyhitty.chdev.ks.predator.core.categories.CategoriesContract;
import com.crazyhitty.chdev.ks.predator.core.categories.CategoriesPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.models.Category;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/6/2017 11:06 AM
 * Description: Unavailable
 */

public class SplashActivity extends AppCompatActivity implements CategoriesContract.View {
    private static final String TAG = "SplashActivity";

    private CategoriesContract.Presenter mCategoriesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new CategoriesPresenter(this));
        mCategoriesPresenter.subscribe();
        // check if user has a already completed onboarding or not, if not then redirect him/her to
        // the onboarding activity, otherwise redirect him/her to dashboard activity.
        if (!PredatorSharedPreferences.isOnboardingComplete(getApplicationContext())) {
            OnboardActivity.startActivity(getApplicationContext(),
                    Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
        } else {
            mCategoriesPresenter.checkIfCategoriesAreAvailable();
        }
    }

    @Override
    public void categoriesAvailable() {
        DashboardActivity.startActivity(getApplicationContext(),
                Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }

    @Override
    public void categoriesUnavailable() {
        if (NetworkConnectionUtil.isNetworkAvailable(getApplicationContext())) {
            PredatorAccount.getAuthToken(this,
                    Constants.Authenticator.PREDATOR_ACCOUNT_TYPE,
                    PredatorSharedPreferences.getAuthTokenType(getApplicationContext()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<String>() {
                        @Override
                        public void onComplete() {
                            // Done
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(TAG, "onError: " + e.getMessage(), e);
                        }

                        @Override
                        public void onNext(String s) {
                            mCategoriesPresenter.fetchCategories(getApplicationContext(), s);
                        }
                    });
        } else {
            mCategoriesPresenter.fetchCategoriesOffline(getApplicationContext());
        }
    }

    @Override
    public void showCategories(List<Category> categories) {
        Logger.d(TAG, "showCategories: categoriesSize: " + categories.size());

        DashboardActivity.startActivity(getApplicationContext(),
                Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }

    @Override
    public void setPresenter(CategoriesContract.Presenter presenter) {
        mCategoriesPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCategoriesPresenter.unSubscribe();
    }
}
