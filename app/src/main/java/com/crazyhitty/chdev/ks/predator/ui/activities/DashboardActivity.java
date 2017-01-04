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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.fragments.PostsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/24/2016 7:32 PM
 * Description: This activity holds a navigation menu as well as acts as a main container for mostly
 * the entire app.
 */

public class DashboardActivity extends BaseAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout_dashboard)
    DrawerLayout drawerLayoutDashboard;

    @BindView(R.id.navigation_view_dashboard)
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initToolbar();
        initDrawer();

        // Only set fragment when saved instance is null.
        // This is done inorder to stop reloading fragment on orientation changes.
        if (savedInstanceState == null) {
            initFragment();
        }
    }

    /**
     * Initialize toolbar.
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    /**
     * Initialize navigation drawer.
     */
    private void initDrawer() {
        // Set up the hamburger icon which will open/close nav drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayoutDashboard,
                toolbar,
                R.string.dashboard_open_nav_drawer,
                R.string.dashboard_close_nav_drawer);
        drawerLayoutDashboard.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up navigation drawer item clicks
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Initialize fragment.
     */
    private void initFragment() {
        setFragment(R.id.frame_layout_dashboard_container,
                PostsFragment.newInstance(),
                false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayoutDashboard.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_posts:
                return true;
            case R.id.nav_collections:
                return true;
            case R.id.nav_bookmarks:
                return true;
            case R.id.nav_my_profile:
                return false;
            case R.id.nav_settings:
                return false;
            case R.id.nav_about:
                return false;
            case R.id.nav_donate:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutDashboard.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutDashboard.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
