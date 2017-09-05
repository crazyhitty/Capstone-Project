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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:30 PM
 * Description: Unavailable
 */

public abstract class BaseSupportFragment extends Fragment {
    private static final String TAG = "BaseSupportFragment";

    private Menu mMenu;

    private final CustomTabsIntent mCustomTabsIntent = new CustomTabsIntent.Builder()
            .enableUrlBarHiding()
            .setShowTitle(true)
            .build();

    private final CustomTabsActivityHelper.CustomTabsFallback mCustomTabsFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, R.string.no_application_available_to_open_this_url, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            };

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

    protected void changeMenuItemColorBasedOnTheme(Menu menu) {
        int color = ContextCompat.getColor(getContext(), R.color.material_grey_900);
        switch (PredatorSharedPreferences.getCurrentTheme(getContext())) {
            case LIGHT:
                color = ContextCompat.getColor(getContext(), R.color.material_grey_900);
                break;
            case DARK:
                color = ContextCompat.getColor(getContext(), R.color.material_grey_100);
                break;
            case AMOLED:
                color = ContextCompat.getColor(getContext(), R.color.material_grey_100);
                break;
        }

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon()
                    .mutate()
                    .setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        }
    }

    protected CustomTabsIntent getCustomTabsIntent() {
        return mCustomTabsIntent;
    }

    protected CustomTabsActivityHelper.CustomTabsFallback getCustomTabsFallback() {
        return mCustomTabsFallback;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mMenu = menu;
    }

    protected void disableMenuItem(@IdRes int itemId) {
        if (mMenu != null) {
            mMenu.findItem(itemId)
                .setEnabled(false);
        } else {
            Logger.e(TAG, "disableMenuItem: Unable to disable menu item as menu itself is null");
        }
    }

    protected void enableMenuItem(@IdRes int itemId) {
        if (mMenu != null) {
            mMenu.findItem(itemId)
                    .setEnabled(true);
        } else {
            Logger.e(TAG, "enableMenuItem: Unable to enable menu item as menu itself is null");
        }
    }
}
