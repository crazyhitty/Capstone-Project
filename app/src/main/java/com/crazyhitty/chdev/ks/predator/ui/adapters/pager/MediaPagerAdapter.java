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

package com.crazyhitty.chdev.ks.predator.ui.adapters.pager;

import android.graphics.drawable.Animatable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/23/2017 5:33 PM
 * Description: Unavailable
 */

public class MediaPagerAdapter extends PagerAdapter {
    private List<Media> mMedia;

    private OnImageClickListener mOnImageClickListener;

    public MediaPagerAdapter(List<Media> media, OnImageClickListener onImageClickListener) {
        mMedia = media;
        mOnImageClickListener = onImageClickListener;
    }

    @Override
    public int getCount() {
        return mMedia != null ? mMedia.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_media_image, container, false);
        container.addView(view);

        final MediaImageViewHolder mediaImageViewHolder = new MediaImageViewHolder(view);

        String mediaImageUrl = mMedia.get(position).getImageUrl();

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(mediaImageUrl);
        controller.setAutoPlayAnimations(true);
        controller.setOldController(mediaImageViewHolder.imgMedia.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                mediaImageViewHolder.progressBarLoading.setVisibility(View.GONE);
                if (imageInfo == null || mediaImageViewHolder.imgMedia == null) {
                    return;
                }
                mediaImageViewHolder.imgMedia.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mediaImageViewHolder.imgMedia.setController(controller.build());
        mediaImageViewHolder.imgMedia.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                mOnImageClickListener.onImageClicked();
            }
        });

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnImageClickListener {
        void onImageClicked();
    }

    public static class MediaImageViewHolder {
        View itemView;

        @BindView(R.id.image_view_media)
        PhotoDraweeView imgMedia;
        @BindView(R.id.progress_bar_loading)
        ProgressBar progressBarLoading;

        public MediaImageViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
