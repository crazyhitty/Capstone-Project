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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.MediaRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.base.BaseAppCompatActivity;
import com.crazyhitty.chdev.ks.predator.ui.fragments.PostDetailsFragment;
import com.crazyhitty.chdev.ks.predator.utils.AppBarStateChangeListener;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.MediaItemDecorator;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.predator.utils.StartSnapHelper;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 1:30 PM
 * Description: Unavailable
 */

public class PostDetailsActivity extends BaseAppCompatActivity implements PostDetailsFragment.OnFragmentInteractionListener, MediaRecyclerAdapter.OnMediaItemClickListener {
    public static final String ARG_POST_TABLE_POST_ID = "post_id";
    private static final String TAG = "PostDetailsActivity";
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_media)
    RecyclerView recyclerViewMedia;
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

    private MediaRecyclerAdapter mMediaRecyclerAdapter;
    private PostDetailsFragment mPostDetailsFragment;

    public static void startActivity(Context context, int postId) {
        Intent intent = new Intent(context, PostDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        setMediaRecyclerViewProperties();

        // Only set fragment when saved instance is null.
        // This is done inorder to stop reloading fragment on orientation changes.
        if (savedInstanceState == null) {
            initPostDetailsFragment();
        }
    }

    private void setMediaRecyclerViewProperties() {
        // Set up the recycler view properties.
        // Create a horizontal layout manager for recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewMedia.setLayoutManager(layoutManager);

        // Add appropriate decorations to the recycler view items.
        recyclerViewMedia.addItemDecoration(new MediaItemDecorator(getApplicationContext(), 16));

        // Create adapter that will power this recycler view.
        mMediaRecyclerAdapter = new MediaRecyclerAdapter(null, this);
        recyclerViewMedia.setAdapter(mMediaRecyclerAdapter);

        // Add snaphelper that will snap the recycler view items at start.
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewMedia);
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

        setSupportActionBar(toolbar);
        hideToolbarTitle(false);

        // Add back button to go back to the previous screen.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initPostDetailsFragment() {
        // Get postId from intent.
        int postId = getIntent().getExtras().getInt(ARG_POST_TABLE_POST_ID);
        // Send this id to the fragment which will be hosted under this activity.
        mPostDetailsFragment = PostDetailsFragment.newInstance(postId);
        setFragment(R.id.frame_layout_post_details_container,
                mPostDetailsFragment,
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

        String postImageUrl = ImageUtils.getCustomPostThumbnailImageUrl(postDetails.getBackdropUrl(),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44.0f),
                ScreenUtils.dpToPxInt(getApplicationContext(), 44.0f));
        imgViewPost.setImageURI(postImageUrl);

        // Set toolbar title.
        getSupportActionBar().setTitle(postDetails.getTitle());
    }

    @Override
    public void showMedia(List<Media> media) {
        mMediaRecyclerAdapter.updateMedia(media);
        recyclerViewMedia.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMediaItemClick(int position, Media media) {
        mPostDetailsFragment.openMedia(media);
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
