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
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.fragments.PostDetailsFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.ToolbarUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 1:30 PM
 * Description: Unavailable
 */

public class PostDetailsActivity extends BaseAppCompatActivity implements PostDetailsFragment.OnFragmentInteractionListener {
    private static final String TAG = "PostDetailsActivity";
    private static final String ARG_POST_TABLE_ID = "id";
    private static final String ARG_POST_TABLE_POST_ID = "post_id";
    private static final int ANIM_TOOLBAR_TITLE_APPEARING_DURATION = 300;
    private static final int ANIM_TOOLBAR_TITLE_DISAPPEARING_DURATION = 300;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_post_title)
    TextView txtPostTitle;
    @BindView(R.id.text_view_post_short_desc)
    TextView txtPostShortDesc;
    @BindView(R.id.text_view_post_date)
    TextView txtPostDate;
    @BindView(R.id.image_view_post)
    SimpleDraweeView imgViewPost;
    /*@BindView(R.id.fab_bookmark)
    FloatingActionButton fabBookmark;*/

    public static void startActivity(Context context, int id, int postId) {
        Intent intent = new Intent(context, PostDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_POST_TABLE_ID, id);
        intent.putExtra(ARG_POST_TABLE_POST_ID, postId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        initAppBarLayout();
        initToolbar();

        // Only set fragment when saved instance is null.
        // This is done inorder to stop reloading fragment on orientation changes.
        if (savedInstanceState == null) {
            initPostDetailsFragment();
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
        setSupportActionBar(toolbar);
        hideToolbarTitle(false);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideToolbarTitle(boolean withAnimation) {
        ToolbarUtils.getTitleTextView(toolbar)
                .animate()
                .alpha(0.0f)
                .setDuration(withAnimation ? ANIM_TOOLBAR_TITLE_APPEARING_DURATION : 0)
                .start();
    }

    private void showToolbarTitle(boolean withAnimation) {
        ToolbarUtils.getTitleTextView(toolbar)
                .animate()
                .alpha(1.0f)
                .setDuration(withAnimation ? ANIM_TOOLBAR_TITLE_DISAPPEARING_DURATION : 0)
                .start();
    }

    private void initPostDetailsFragment() {
        // Get id and postId from intent.
        int id = getIntent().getExtras().getInt(ARG_POST_TABLE_ID);
        int postId = getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID);
        // Send this id to the fragment which will be hosted under this activity.
        setFragment(R.id.frame_layout_post_details_container,
                PostDetailsFragment.newInstance(id, postId),
                false);
    }

    /**
     * Show event details on this activity, like title, description, etc. This method will be
     * invoked from {@link PostDetailsFragment#showDetails(Cursor)}.
     *
     * @param postDetails Event containing post details.
     */
    @Override
    public void showPostDetails(final PostDetails postDetails) {
        txtPostTitle.setText(postDetails.getTitle());
        txtPostShortDesc.setText(postDetails.getDescription());

        String date = DateUtils.getPredatorPostCompleteDate(postDetails.getDate());

        if (TextUtils.isEmpty(date)) {
            txtPostDate.setText(postDetails.getDay());
        } else {
            txtPostDate.setText(date);
        }

        // The code inside runnable will only run when this view is initialized on the screen.
        imgViewPost.post(new Runnable() {
            @Override
            public void run() {
                int height = imgViewPost.getHeight();
                int width = imgViewPost.getWidth();
                Logger.d(TAG, "imgViewPost height: " + height + ", width: " + width);

                String postImageUrl = postDetails.getBackdropUrl();

                if (height != 0 && width != 0) {
                    postImageUrl = ImageUtils.getCustomPostThumbnailImageUrl(postImageUrl,
                            height,
                            width);
                }

                // Set post image, also animate automatically if it is a gif.
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(postImageUrl)
                        .setAutoPlayAnimations(true)
                        .build();
                imgViewPost.setController(controller);
            }
        });

        // Set toolbar title.
        getSupportActionBar().setTitle(postDetails.getTitle());
    }

    /*@OnClick(R.id.fab_bookmark)
    public void onBookmark() {
        fabBookmark.setSelected(!fabBookmark.isSelected());
    }*/

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
