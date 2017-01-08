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

package com.crazyhitty.chdev.ks.predator.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:31 PM
 * Description: Unavailable
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    private static final String TAG = "BaseAppCompatActivity";

    private ProgressDialog mLoadingDialog;
    private AlertDialog mErrorDialog;

    public void setFragment(@IdRes int layoutResId, Fragment fragment, boolean addToBackStack) {
        CoreUtils.setFragment(getSupportFragmentManager(), layoutResId, fragment, addToBackStack);
    }

    public void showLoadingDialog(boolean isCancellable) {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle(R.string.loading);
        mLoadingDialog.setMessage(getString(R.string.please_wait));
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setCancelable(isCancellable);
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showErrorDialog(String message, boolean isCancellable) {
        mErrorDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(message)
                .setIcon(R.drawable.ic_error_outline_24dp)
                .setCancelable(isCancellable)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void dismissErrorDialog() {
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
    }

    public void showShortToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(@StringRes int messageRes) {
        Toast.makeText(getApplicationContext(), messageRes, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showLongToast(@StringRes int messageRes) {
        Toast.makeText(getApplicationContext(), messageRes, Toast.LENGTH_LONG).show();
    }
}
