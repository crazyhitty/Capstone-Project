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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.BuildConfig;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.ui.adapters.pager.AboutPagerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseSupportFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.AboutDeveloperFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.AboutLibrariesFragment;
import com.crazyhitty.chdev.ks.predator.ui.fragments.ChangelogDialogFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/2017 12:31 AM
 * Description: Unavailable
 */

public class AboutActivity extends BaseAppCompatActivity {
    private static final String TAG = "AboutActivity";
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
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_app_version)
    TextView txtVersion;
    @BindView(R.id.button_github)
    Button btnGithub;
    @BindView(R.id.button_google_plus)
    Button btnGooglePlus;
    @BindView(R.id.button_mail)
    Button btnMail;
    @BindView(R.id.tab_layout_about)
    TabLayout tabLayoutAbout;
    @BindView(R.id.view_pager_about)
    ViewPager viewPagerAbout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();
        setVersion();
        addIconsToButtons();
        initViewPager();
    }

    private void initAppBarLayout() {
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case COLLAPSED:
                        showToolbarTitle(true);
                        break;
                    case EXPANDED:
                        hideToolbarTitle(true);
                        break;
                }
            }
        });
    }

    private void initToolbar() {
        attachToolbar(toolbar);

        toolbar.setTitle(R.string.activity_about);
        hideToolbarTitle(false);

        setSupportActionBar(toolbar);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setVersion() {
        String version = getString(R.string.activity_about_version, BuildConfig.VERSION_NAME);
        txtVersion.setText(version);
    }

    /**
     * Add left drawables on buttons. This is done programmatically because on pre lollipop devices
     * vector drawables cannot be directly accessed in buttons(xml).
     */
    private void addIconsToButtons() {
        boolean isLightTheme = PredatorSharedPreferences.getCurrentTheme(getApplicationContext()) ==
                PredatorSharedPreferences.THEME_TYPE.LIGHT;

        Drawable githubDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                isLightTheme ? R.drawable.ic_github_24dp : R.drawable.ic_github_inverse_24dp);
        btnGithub.setCompoundDrawablesWithIntrinsicBounds(githubDrawable, null, null, null);

        Drawable googlePlusDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                isLightTheme ? R.drawable.ic_google_plus_24dp : R.drawable.ic_google_plus_inverse_24dp);
        btnGooglePlus.setCompoundDrawablesWithIntrinsicBounds(googlePlusDrawable, null, null, null);

        Drawable mailDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                isLightTheme ? R.drawable.ic_mail_24dp : R.drawable.ic_mail_inverse_24dp);
        btnMail.setCompoundDrawablesWithIntrinsicBounds(mailDrawable, null, null, null);
    }

    private void initViewPager() {
        AboutPagerAdapter aboutPagerAdapter = new AboutPagerAdapter(getSupportFragmentManager(),
                new BaseSupportFragment[]{AboutDeveloperFragment.newInstance(), AboutLibrariesFragment.newInstance()},
                new String[]{getString(R.string.activity_about_developer_title), getString(R.string.activity_about_library_title)});
        viewPagerAbout.setAdapter(aboutPagerAdapter);

        tabLayoutAbout.setupWithViewPager(viewPagerAbout);
        changeTabTypeface(tabLayoutAbout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        // Change menu item icons based on theme
        changeMenuItemColorBasedOnTheme(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_feedback:
                openFeedback();
                break;
            case R.id.menu_changelog:
                openChangelog();
                break;
            case R.id.menu_privacy_policy:
                openPrivacyPolicy();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFeedback() {
        CoreUtils.openFeedback(this);
    }

    private void openChangelog() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(ChangelogDialogFragment.class.getSimpleName());
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        ChangelogDialogFragment.newInstance().show(fragmentTransaction, ChangelogDialogFragment.class.getSimpleName());
    }

    private void openPrivacyPolicy() {
        CustomTabsHelperFragment.open(this,
                mCustomTabsIntent,
                Uri.parse(Constants.About.URL_PRIVACY_POLICY),
                mCustomTabsFallback);
    }

    @OnClick(R.id.button_github)
    public void onGithubClick() {
        CustomTabsHelperFragment.open(this,
                mCustomTabsIntent,
                Uri.parse(Constants.About.URL_GITHUB),
                mCustomTabsFallback);
    }

    @OnClick(R.id.button_google_plus)
    public void onGooglePlusClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.About.URL_GOOGLE_PLUS));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.button_mail)
    public void onMailClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", Constants.About.MAIL_ID, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.activity_about_mail_subject));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.activity_about_mail_share_intent_title)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}