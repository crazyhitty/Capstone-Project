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

package com.crazyhitty.chdev.ks.predator.ui.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Post;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     9/6/17 2:11 AM
 * Description: Unavailable
 */

public class PostNotification {
    private static final String TAG = "PostNotification";

    private static final int NOTIFICATION_ID = 1;

    private Context mContext;
    private NotificationManager mNotificationManager;

    public PostNotification(@NonNull Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show(Post post) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setColor(ContextCompat.getColor(mContext, R.color.notification_color))
                .setSmallIcon(R.drawable.ic_notification_predator)
                .setLargeIcon(getBitmap(R.mipmap.ic_launcher))
                .setContentTitle(post.getName())
                .setContentText(post.getTagline());

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private Bitmap getBitmap(@DrawableRes int drawableRes) {
        return BitmapFactory.decodeResource(mContext.getResources(),
                drawableRes);
    }
}
