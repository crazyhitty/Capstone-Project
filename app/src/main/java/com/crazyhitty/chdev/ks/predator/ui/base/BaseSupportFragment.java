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

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:30 PM
 * Description: Unavailable
 */

public abstract class BaseSupportFragment extends Fragment {
    protected void setFragment(@IdRes int layoutResId, Fragment fragment, boolean addToBackStack) {
        CoreUtils.setFragment(getFragmentManager(), layoutResId, fragment, addToBackStack);
    }

    protected boolean isNetworkAvailable(boolean showToastMessage) {
        boolean isNetworkAvailable = NetworkConnectionUtil.isNetworkAvailable(getContext());
        if (!isNetworkAvailable && showToastMessage) {
            showLongToast(R.string.not_connected_to_network_err);
        }
        return isNetworkAvailable;
    }

    protected void showShortToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(@StringRes int messageRes) {
        Toast.makeText(getContext(), messageRes, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void showLongToast(@StringRes int messageRes) {
        Toast.makeText(getContext(), messageRes, Toast.LENGTH_LONG).show();
    }
}
