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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.AboutLibrary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/13/2017 5:05 PM
 * Description: Unavailable
 */

public class AboutLibrariesRecyclerAdapter extends RecyclerView.Adapter<AboutLibrariesRecyclerAdapter.LibraryViewHolder> {
    private List<AboutLibrary> mAboutLibraries;
    private OnLibraryItemClickListener mOnLibraryItemClickListener;
    private int mLastPosition = -1;

    public AboutLibrariesRecyclerAdapter(@Nullable List<AboutLibrary> aboutLibraries,
                                         @NonNull OnLibraryItemClickListener onLibraryItemClickListener) {
        mAboutLibraries = aboutLibraries;
        mOnLibraryItemClickListener = onLibraryItemClickListener;
    }

    public void updateLibraries(@Nullable List<AboutLibrary> aboutLibraries) {
        mLastPosition = -1;
        mAboutLibraries = aboutLibraries;
        notifyDataSetChanged();
    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_about_library, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LibraryViewHolder holder, int position) {
        holder.txtName.setText(mAboutLibraries.get(position).getTitle());
        holder.txtCreator.setText(mAboutLibraries.get(position).getCreator());
        holder.txtLicenseType.setText(mAboutLibraries.get(position).getLicenseType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLibraryItemClickListener.onLibraryItemClick(holder.getAdapterPosition(),
                        mAboutLibraries.get(holder.getAdapterPosition()).getRedirectUrl());
            }
        });

        manageAnimation(holder.itemView, position);
    }

    private void manageAnimation(View view, int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                    R.anim.anim_bottom_top_fade_in);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mAboutLibraries != null ? mAboutLibraries.size() : 0;
    }

    @Override
    public void onViewDetachedFromWindow(LibraryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    public interface OnLibraryItemClickListener {
        void onLibraryItemClick(int position, String redirectUrl);
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_name)
        TextView txtName;
        @BindView(R.id.text_view_creator)
        TextView txtCreator;
        @BindView(R.id.text_view_license_type)
        TextView txtLicenseType;

        public LibraryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
