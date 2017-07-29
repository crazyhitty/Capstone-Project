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

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.FontsPreviewRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.ui.adapters.recycler.PostsRecyclerAdapter;
import com.crazyhitty.chdev.ks.predator.utils.ListItemDecorator;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/23/17 7:00 PM
 * Description: Unavailable
 */

public class FontPreviewPagerAdapter extends PagerAdapter {
    private String[] mFontNames;
    private List<Post> mDummyPosts;

    public FontPreviewPagerAdapter(String[] fontNames, List<Post> dummyPosts) {
        mFontNames = fontNames;
        mDummyPosts = dummyPosts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_font_preview, container, false);
        container.addView(view);

        final FontPreviewViewHolder fontPreviewViewHolder = new FontPreviewViewHolder(view,
                mDummyPosts,
                mFontNames[position]);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mFontNames.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public static class FontPreviewViewHolder {
        View itemView;

        @BindView(R.id.recycler_view_dummy_posts)
        RecyclerView recyclerViewDummyPosts;

        public FontPreviewViewHolder(View itemView, List<Post> posts, String fontName) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            recyclerViewDummyPosts.setLayoutManager(linearLayoutManager);
            recyclerViewDummyPosts.addItemDecoration(new ListItemDecorator(itemView.getContext(),
                    72));

            FontsPreviewRecyclerAdapter.TYPE type = FontsPreviewRecyclerAdapter.TYPE.LIST;
            HashMap<Integer, String> dateHashMap = new HashMap<>();
            dateHashMap.put(0, fontName);

            FontsPreviewRecyclerAdapter  fontsPreviewRecyclerAdapter = new FontsPreviewRecyclerAdapter(posts,
                    type,
                    dateHashMap);
            recyclerViewDummyPosts.setAdapter(fontsPreviewRecyclerAdapter);
        }
    }
}
