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

package com.crazyhitty.chdev.ks.predator.ui.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/17/2017 4:51 PM
 * Description: Unavailable
 */

public class MediaRecyclerAdapter extends RecyclerView.Adapter<MediaRecyclerAdapter.MediaViewHolder> {
    private List<Media> mMedia;
    private OnMediaItemClickListener mOnMediaItemClickListener;

    public MediaRecyclerAdapter(List<Media> media, OnMediaItemClickListener onMediaItemClickListener) {
        mMedia = media;
        mOnMediaItemClickListener = onMediaItemClickListener;
    }

    public void updateMedia(List<Media> media) {
        mMedia = media;
        notifyDataSetChanged();
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MediaViewHolder holder, int position) {
        String mediaImageUrl = mMedia.get(position).getImageUrl();
        mediaImageUrl = ImageUtils.getCustomMediaImageThumbnailUrl(mediaImageUrl,
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 100),
                ScreenUtils.dpToPxInt(holder.itemView.getContext(), 200));

        // Set media image, also animate automatically if it is a gif.
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(mediaImageUrl)
                .setAutoPlayAnimations(true)
                .build();
        holder.imgViewMedia.setController(controller);

        int mediaTypeTxtRes = R.string.item_media_type_image;
        switch (mMedia.get(position).getMediaType()) {
            case Constants.Media.IMAGE:
                mediaTypeTxtRes = R.string.item_media_type_image;
                break;
            case Constants.Media.VIDEO:
                mediaTypeTxtRes = R.string.item_media_type_video;
                break;
        }

        // Set media type.
        holder.txtMediaType.setText(mediaTypeTxtRes);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMediaItemClickListener.onMediaItemClick(holder.getAdapterPosition(),
                        mMedia.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMedia != null ? mMedia.size() : 0;
    }

    public interface OnMediaItemClickListener {
        void onMediaItemClick(int position, Media media);
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_media)
        SimpleDraweeView imgViewMedia;
        @BindView(R.id.text_view_media_type)
        TextView txtMediaType;

        public MediaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
