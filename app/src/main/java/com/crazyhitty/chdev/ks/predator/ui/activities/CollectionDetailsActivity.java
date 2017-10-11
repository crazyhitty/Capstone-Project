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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.fragments.CollectionDetailsFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/20/2017 9:36 PM
 * Description: Unavailable
 */

public class CollectionDetailsActivity extends BaseAppCompatActivity implements CollectionDetailsFragment.OnFragmentInteractionListener {
    private static final String TAG = "CollectionDetailsActivity";

    private static final String ARG_COLLECTION_TABLE_COLLECTION_ID = "collection_id";
    private static final String ARG_COLLECTION_FALLBACK_DATA = "collection_fallback_data";

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_view_collection)
    SimpleDraweeView imgViewCollection;
    @BindView(R.id.text_view_collection_title)
    TextView txtCollectionTitle;
    @BindView(R.id.text_view_collection_desc)
    TextView txtCollectionDesc;

    public static void startActivity(Context context, int collectionId) {
        Intent intent = new Intent(context, CollectionDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_COLLECTION_TABLE_COLLECTION_ID, collectionId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,
                                     int collectionId,
                                     Collection collectionFallback) {
        Intent intent = new Intent(context, CollectionDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_COLLECTION_TABLE_COLLECTION_ID, collectionId);
        intent.putExtra(ARG_COLLECTION_FALLBACK_DATA, collectionFallback);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_collection_details);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();

        // Only set fragment when saved instance is null.
        // This is done inorder to stop reloading fragment on orientation changes.
        if (savedInstanceState == null) {
            initCollectionDetailsFragment();
        }

        // Check if fallback data is available or not.
        if (getIntent().getParcelableExtra(ARG_COLLECTION_FALLBACK_DATA) != null) {
            Collection collectionFallback = getIntent().getParcelableExtra(ARG_COLLECTION_FALLBACK_DATA);
            setToolbarTitle(collectionFallback.getTitle());
        }
    }

    private void applyTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
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

        appBarLayout.setExpanded(false);
    }

    private void initToolbar() {
        attachToolbar(toolbar);

        setSupportActionBar(toolbar);
        hideToolbarTitle(false);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initCollectionDetailsFragment() {
        // Get collectionId from intent.
        int collectionId = getIntent().getExtras().getInt(ARG_COLLECTION_TABLE_COLLECTION_ID, -1);
        // Send this id to the fragment which will be hosted under this activity.
        setFragment(R.id.frame_layout_collection_details_container,
                CollectionDetailsFragment.newInstance(collectionId),
                false);
    }

    @Override
    public void setToolbarTitle(String title) {
        // Set toolbar title.
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setAppBarContents(Collection collection) {
        txtCollectionTitle.setText(collection.getName());

        if (TextUtils.isEmpty(collection.getTitle())) {
            txtCollectionDesc.setText(R.string.item_collection_no_desc_available);
        } else {
            txtCollectionDesc.setText(collection.getTitle());
        }

        if (TextUtils.isEmpty(collection.getBackgroundImageUrl())) {
            imgViewCollection.setVisibility(View.GONE);
        } else {
            imgViewCollection.setImageURI(collection.getBackgroundImageUrl());
        }
    }

    @Override
    public void resortToFallbackData() {
        if (getIntent().getParcelableExtra(ARG_COLLECTION_FALLBACK_DATA) != null) {
            Collection collectionFallback = getIntent().getParcelableExtra(ARG_COLLECTION_FALLBACK_DATA);
            setAppBarContents(collectionFallback);
        }
    }

    @Override
    public void expandAppBar() {
        appBarLayout.setExpanded(true, true);
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
}
