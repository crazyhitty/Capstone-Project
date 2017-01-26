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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.About;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/2017 10:25 PM
 * Description: Unavailable
 */

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AboutRecyclerAdapter";

    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_DEVELOPER = 2;
    private static final int VIEW_TYPE_SPECIAL_THANKS = 3;
    private static final int VIEW_TYPE_LIBRARY = 4;

    private List<About> mAbout;
    private OnLibraryItemClickListener mOnLibraryItemClickListener;

    public AboutRecyclerAdapter(List<About> about, OnLibraryItemClickListener onLibraryItemClickListener) {
        mAbout = about;
        mOnLibraryItemClickListener = onLibraryItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                View titleView = layoutInflater.inflate(R.layout.item_post_details_title, parent, false);
                viewHolder = new TitleViewHolder(titleView);
                break;
            case VIEW_TYPE_DEVELOPER:
                View developerView = layoutInflater.inflate(R.layout.item_post_user, parent, false);
                viewHolder = new DeveloperViewHolder(developerView);
                break;
            case VIEW_TYPE_SPECIAL_THANKS:
                View specialThanksView = layoutInflater.inflate(R.layout.item_special_thanks, parent, false);
                viewHolder = new SpecialThanksViewHolder(specialThanksView);
                break;
            case VIEW_TYPE_LIBRARY:
                View libraryView = layoutInflater.inflate(R.layout.item_library, parent, false);
                viewHolder = new LibraryViewHolder(libraryView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_TITLE:
                onBindTitleViewHolder((TitleViewHolder) holder, position);
                break;
            case VIEW_TYPE_DEVELOPER:
                onBindDeveloperViewHolder((DeveloperViewHolder) holder, position);
                break;
            case VIEW_TYPE_SPECIAL_THANKS:
                onBindSpecialThanksViewHolder((SpecialThanksViewHolder) holder, position);
                break;
            case VIEW_TYPE_LIBRARY:
                onBindLibraryViewHolder((LibraryViewHolder) holder, position);
                break;
        }
    }

    private void onBindTitleViewHolder(TitleViewHolder titleViewHolder, int position) {
        titleViewHolder.txtTitle.setText(mAbout.get(position).getTitle());
    }

    private void onBindDeveloperViewHolder(DeveloperViewHolder developerViewHolder, int position) {
        developerViewHolder.txtUserName.setText(mAbout.get(position).getUsername());
        developerViewHolder.txtUserType.setText(mAbout.get(position).getUserType());

        developerViewHolder.imgViewUser.getHierarchy()
                .setPlaceholderImage(R.drawable.ic_kartik);
    }

    private void onBindSpecialThanksViewHolder(SpecialThanksViewHolder specialThanksViewHolder, int position) {
        specialThanksViewHolder.txtData.setText(Html.fromHtml(mAbout.get(position).getSpecialThanks()));
    }

    private void onBindLibraryViewHolder(final LibraryViewHolder libraryViewHolder, int position) {
        libraryViewHolder.txtName.setText(mAbout.get(position).getLibraryTitle());
        libraryViewHolder.txtCreator.setText(mAbout.get(position).getLibraryCreator());
        libraryViewHolder.txtLicenseType.setText(mAbout.get(position).getLibraryLicenseType());

        libraryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLibraryItemClickListener.onLibraryItemClick(libraryViewHolder.getAdapterPosition(),
                        mAbout.get(libraryViewHolder.getAdapterPosition()).getLibraryRedirectUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAbout.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mAbout.get(position).getType()) {
            case TITLE:
                return VIEW_TYPE_TITLE;
            case DEVELOPER:
                return VIEW_TYPE_DEVELOPER;
            case SPECIAL_THANKS:
                return VIEW_TYPE_SPECIAL_THANKS;
            default:
                return VIEW_TYPE_LIBRARY;
        }
    }

    public interface OnLibraryItemClickListener {
        void onLibraryItemClick(int position, String redirectUrl);
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_title)
        TextView txtTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class DeveloperViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_user_name)
        TextView txtUserName;
        @BindView(R.id.text_view_user_type)
        TextView txtUserType;
        @BindView(R.id.image_view_user)
        SimpleDraweeView imgViewUser;

        public DeveloperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Add start margin of 16dp to this item.
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            layoutParams.setMargins(ScreenUtils.dpToPxInt(itemView.getContext(), 16.0f),
                    0,
                    ScreenUtils.dpToPxInt(itemView.getContext(), 16.0f),
                    0);
        }
    }

    public static class SpecialThanksViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_data)
        TextView txtData;

        public SpecialThanksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Add start margin of 16dp to this item.
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            layoutParams.setMargins(ScreenUtils.dpToPxInt(itemView.getContext(), 16.0f),
                    ScreenUtils.dpToPxInt(itemView.getContext(), 4.0f),
                    ScreenUtils.dpToPxInt(itemView.getContext(), 16.0f),
                    ScreenUtils.dpToPxInt(itemView.getContext(), 4.0f));
        }
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
    }
}
