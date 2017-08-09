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

package com.crazyhitty.chdev.ks.predator.ui.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.crazyhitty.chdev.ks.predator.R;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     8/10/17 12:33 AM
 * Description: Unavailable
 */

public class AppLinkingDialogHeaderView extends FrameLayout {
    private static final String TAG = "AppLinkingDialogHeaderView";

    @BindView(R.id.view_animation_background)
    View viewAnimationBackground;
    @BindView(R.id.view_status_background)
    View viewStatusBackground;
    @BindView(R.id.lottie_animation_view)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.image_view_status)
    SimpleDraweeView imgViewStatus;

    private TYPE mType;

    public AppLinkingDialogHeaderView(@NonNull Context context) {
        super(context);
        initializeViews(context);
    }

    public AppLinkingDialogHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public AppLinkingDialogHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AppLinkingDialogHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_app_linking_dialog_header, this);

        // bind views
        ButterKnife.bind(this);

        // set layout params
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        LayoutTransition layoutTransition = new LayoutTransition();
        setLayoutTransition(layoutTransition);
    }

    public TYPE getType() {
        return mType;
    }

    public void setType(TYPE type) {
        mType = type;
        switch (type) {
            case LOADING:
                setLoadingState();
                break;
            case SUCCESS:
                setSuccessState();
                break;
            case FAILURE:
                setFailureState();
                break;
        }
    }

    private void setLoadingState() {
        imgViewStatus.setVisibility(View.GONE);
        viewStatusBackground.setVisibility(View.GONE);
    }

    private void setSuccessState() {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.ic_done))
                .build();

        imgViewStatus.setImageURI(uri);
        imgViewStatus.setVisibility(View.VISIBLE);

        viewStatusBackground.setBackgroundColor(getStatusBackgroundColor(true));
        viewStatusBackground.setVisibility(View.VISIBLE);
    }

    private void setFailureState() {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.ic_error_alternative))
                .build();


        imgViewStatus.setImageURI(uri);
        imgViewStatus.setVisibility(View.VISIBLE);

        viewStatusBackground.setBackgroundColor(getStatusBackgroundColor(false));
        viewStatusBackground.setVisibility(View.VISIBLE);
    }

    private int getStatusBackgroundColor(boolean isSuccess) {
        int statusBackgroundColorRes;
        if (isSuccess) {
            int[] attrs = {R.attr.appLinkingSuccessColor};
            TypedArray ta = getContext().obtainStyledAttributes(attrs);
            statusBackgroundColorRes = ta.getResourceId(0, android.R.color.black);
            ta.recycle();
        } else {
            int[] attrs = {R.attr.appLinkingFailureColor};
            TypedArray ta = getContext().obtainStyledAttributes(attrs);
            statusBackgroundColorRes = ta.getResourceId(0, android.R.color.black);
            ta.recycle();
        }
        return ContextCompat.getColor(getContext(), statusBackgroundColorRes);
    }

    public enum TYPE {
        LOADING,
        SUCCESS,
        FAILURE
    }
}
