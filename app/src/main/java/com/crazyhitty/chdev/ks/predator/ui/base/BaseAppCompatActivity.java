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
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.BuildConfig;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.NetworkConnectionUtil;
import com.crazyhitty.chdev.ks.predator.utils.ToolbarUtils;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:31 PM
 * Description: Unavailable
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private static final String TAG = "BaseAppCompatActivity";
    private static final int ANIM_TOOLBAR_TITLE_APPEARING_DURATION = 300;
    private static final int ANIM_TOOLBAR_TITLE_DISAPPEARING_DURATION = 300;

    private Toolbar mToolbar;
    private String mCurrentFragmentTag;
    private ProgressDialog mLoadingDialog;
    private AlertDialog mErrorDialog;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageThemes();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Attach calligraphy context if font name is not null. Font name will only be null if user
        // has selected default font.

        if (TextUtils.isEmpty(PredatorSharedPreferences.getCurrentFont(newBase))) {
            super.attachBaseContext(newBase);
        } else {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        View viewToolbarShadow = findViewById(R.id.view_toolbar_shadow_pre_lollipop);

        // Hide artificial shadow for post lollipop devices.
        if (viewToolbarShadow != null &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewToolbarShadow.setVisibility(View.GONE);
        }
    }

    private void manageThemes() {
        switch (PredatorSharedPreferences.getCurrentTheme(getApplicationContext())) {
            case LIGHT:
                setTheme(R.style.AppTheme_NoActionBar);
                break;
            case DARK:
                setTheme(R.style.AppThemeDark_NoActionBar);
                break;
            case AMOLED:
                setTheme(R.style.AppThemeAmoled_NoActionBar);
                break;
        }
    }

    protected void attachToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    protected void setFragment(@IdRes int layoutResId,
                               Fragment fragment,
                               boolean addToBackStack) {
        mCurrentFragmentTag = fragment.getClass().getSimpleName();
        CoreUtils.setFragment(getSupportFragmentManager(), layoutResId, fragment, addToBackStack);
    }

    protected void setFragment(@IdRes int layoutResId,
                               android.app.Fragment fragment,
                               boolean addToBackStack) {
        mCurrentFragmentTag = fragment.getClass().getSimpleName();
        CoreUtils.setFragment(getFragmentManager(), layoutResId, fragment, addToBackStack);
    }

    public String getCurrentFragmentTag() {
        return mCurrentFragmentTag;
    }

    public boolean isFragmentVisible(Fragment fragment) {
        return TextUtils.equals(fragment.getClass().getSimpleName(), mCurrentFragmentTag);
    }

    public boolean isFragmentVisible(android.app.Fragment fragment) {
        return TextUtils.equals(fragment.getClass().getSimpleName(), mCurrentFragmentTag);
    }

    protected void showLoadingDialog(boolean isCancellable) {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle(R.string.loading);
        mLoadingDialog.setMessage(getString(R.string.please_wait));
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setCancelable(isCancellable);
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected void showErrorDialog(String message, boolean isCancellable) {
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

    protected void showErrorDialog(String title, String message, boolean isCancellable) {
        mErrorDialog = new AlertDialog.Builder(this)
                .setTitle(title)
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

    protected void dismissErrorDialog() {
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
    }

    protected void showShortToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(@StringRes int messageRes) {
        Toast.makeText(getApplicationContext(), messageRes, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void showLongToast(@StringRes int messageRes) {
        Toast.makeText(getApplicationContext(), messageRes, Toast.LENGTH_LONG).show();
    }

    protected void hideToolbarTitle(boolean withAnimation) {
        ToolbarUtils.getTitleTextView(mToolbar)
                .animate()
                .alpha(0.0f)
                .setDuration(withAnimation ? ANIM_TOOLBAR_TITLE_APPEARING_DURATION : 0)
                .start();
    }

    protected void showToolbarTitle(boolean withAnimation) {
        ToolbarUtils.getTitleTextView(mToolbar)
                .animate()
                .alpha(1.0f)
                .setDuration(withAnimation ? ANIM_TOOLBAR_TITLE_DISAPPEARING_DURATION : 0)
                .start();
    }

    protected void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
        if (intent.resolveActivity(getPackageManager()) != null) {
            showLongToast(R.string.rate_app_message);
            startActivity(intent);
        }
    }

    protected void shareApp() {
        String title = getString(R.string.share_app_title);
        String body = getString(R.string.share_app_body, BuildConfig.APPLICATION_ID);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }

    protected boolean isNetworkAvailable(boolean showToastMessage) {
        boolean isNetworkAvailable = NetworkConnectionUtil.isNetworkAvailable(getApplicationContext());
        if (!isNetworkAvailable && showToastMessage) {
            showLongToast(R.string.not_connected_to_network_err);
        }
        return isNetworkAvailable;
    }

    protected void changeMenuItemColorBasedOnTheme(Menu menu) {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.material_grey_900);
        switch (PredatorSharedPreferences.getCurrentTheme(getApplicationContext())) {
            case LIGHT:
                color = ContextCompat.getColor(getApplicationContext(), R.color.material_grey_900);
                break;
            case DARK:
                color = ContextCompat.getColor(getApplicationContext(), R.color.material_grey_100);
                break;
            case AMOLED:
                color = ContextCompat.getColor(getApplicationContext(), R.color.material_grey_100);
                break;
        }

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon()
                    .mutate()
                    .setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        }
    }

    protected void changeTabTypeface(TabLayout tabLayout) {
        if (TextUtils.isEmpty(PredatorSharedPreferences.getCurrentFont(getApplicationContext())) ||
                TextUtils.equals(PredatorSharedPreferences.getCurrentFont(getApplicationContext()), getString(R.string.settings_change_font_system))) {
            return;
        }

        Typeface fontTypeFace = Typeface.createFromAsset(getAssets(),
                String.format("fonts/%s", PredatorSharedPreferences.getCurrentFont(getApplicationContext())));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            try {
                Field tabViewField = TabLayout.Tab.class.getDeclaredField("mView");
                tabViewField.setAccessible(true);
                Object tabViewObject = tabViewField.get(tab);

                Field textViewField = tabViewObject.getClass().getDeclaredField("mTextView");
                textViewField.setAccessible(true);

                TextView textView = (TextView) textViewField.get(tabViewObject);
                textView.setTypeface(fontTypeFace);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected CustomTabsIntent getCustomTabsIntent() {
        return mCustomTabsIntent;
    }

    protected CustomTabsActivityHelper.CustomTabsFallback getCustomTabsFallback() {
        return mCustomTabsFallback;
    }
}