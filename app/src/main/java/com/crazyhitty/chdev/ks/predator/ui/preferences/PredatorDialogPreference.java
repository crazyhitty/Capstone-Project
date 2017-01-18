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

package com.crazyhitty.chdev.ks.predator.ui.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.crazyhitty.chdev.ks.predator.R;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/15/2017 8:30 PM
 * Description: Unavailable
 */

public class PredatorDialogPreference extends DialogPreference {
    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    private OnNegativeButtonClickListener mOnNegativeButtonClickListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PredatorDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeAttributes(attrs);
    }

    public PredatorDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(attrs);
    }

    public PredatorDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PredatorDialogPreference(Context context) {
        super(context);
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener) {
        this.mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public void setOnNegativeButtonClickListener(OnNegativeButtonClickListener onNegativeButtonClickListener) {
        this.mOnNegativeButtonClickListener = onNegativeButtonClickListener;
    }

    private void initializeAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PredatorDialogPreference,
                0, 0);

        try {
            String positiveButtonText = a.getString(R.styleable.PredatorDialogPreference_positiveButtonText);
            String negativeButtonText = a.getString(R.styleable.PredatorDialogPreference_negativeButtonText);

            if (!TextUtils.isEmpty(positiveButtonText)) {
                setPositiveButtonText(positiveButtonText);
            }

            if (!TextUtils.isEmpty(negativeButtonText)) {
                setNegativeButtonText(negativeButtonText);
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (mOnPositiveButtonClickListener != null) {
                    mOnPositiveButtonClickListener.onClick(dialog);
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (mOnNegativeButtonClickListener != null) {
                    mOnNegativeButtonClickListener.onClick(dialog);
                }
                break;
        }
    }

    public interface OnPositiveButtonClickListener {
        void onClick(DialogInterface dialog);
    }

    public interface OnNegativeButtonClickListener {
        void onClick(DialogInterface dialog);
    }
}
