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

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.BuildConfig;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.fragments.AboutFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/2017 12:31 AM
 * Description: Unavailable
 */

public class AboutActivity extends BaseAppCompatActivity {
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

    private AboutFragment mAboutFragment;

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

        // Only set fragment when saved instance is null.
        // This is done inorder to stop reloading fragment on orientation changes.
        if (savedInstanceState == null) {
            initAboutFragment();
        }
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
        Drawable githubDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                R.drawable.ic_github_24dp);
        btnGithub.setCompoundDrawablesWithIntrinsicBounds(githubDrawable, null, null, null);

        Drawable googlePlusDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                R.drawable.ic_google_plus_24dp);
        btnGooglePlus.setCompoundDrawablesWithIntrinsicBounds(googlePlusDrawable, null, null, null);

        Drawable mailDrawable = AppCompatResources.getDrawable(getApplicationContext(),
                R.drawable.ic_mail_24dp);
        btnMail.setCompoundDrawablesWithIntrinsicBounds(mailDrawable, null, null, null);
    }

    private void initAboutFragment() {
        mAboutFragment = AboutFragment.newInstance();
        setFragment(R.id.frame_layout_about_container,
                mAboutFragment,
                false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_github)
    public void onGithubClick() {
        mAboutFragment.openGithub();
    }

    @OnClick(R.id.button_google_plus)
    public void onGooglePlusClick() {
        mAboutFragment.openGooglePlus();
    }

    @OnClick(R.id.button_mail)
    public void onMailClick() {
        mAboutFragment.openMail();
    }

}