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

package com.crazyhitty.chdev.ks.predator.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;


/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/9/2017 11:56 AM
 * Description: Unavailable
 */

public class ListItemDecorator extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    private int mDividerLeftPadding;
    private int mSpacingPx;

    public ListItemDecorator(Context context, int dividerLeftPaddingDp) {
        super();
        mSpacingPx = ScreenUtils.dpToPxInt(context, 16);
        mDividerLeftPadding = ScreenUtils.dpToPxInt(context, dividerLeftPaddingDp);
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        switch (PredatorSharedPreferences.getCurrentTheme(context)) {
            case DARK:
                DrawableCompat.setTint(mDivider, ContextCompat.getColor(context, R.color.material_grey_400));
                break;
            case AMOLED:
                DrawableCompat.setTint(mDivider, ContextCompat.getColor(context, R.color.material_grey_400));
                break;
        }
        styledAttributes.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = mDividerLeftPadding;
            int top = child.getBottom() + layoutParams.bottomMargin;
            int right = child.getRight();
            int bottom = top + mDivider.getIntrinsicHeight();

            if (parent.getResources().getBoolean(R.bool.is_right_to_left)) {
                left = child.getLeft();
                right = child.getRight() - mDividerLeftPadding;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = -mSpacingPx;
        }
    }
}