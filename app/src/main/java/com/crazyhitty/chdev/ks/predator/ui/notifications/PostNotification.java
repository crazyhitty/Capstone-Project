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
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.utils.ImageFetcher;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.ScreenUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.ImageUtils;

import java.util.UUID;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     9/6/17 2:11 AM
 * Description: Unavailable
 */

public class PostNotification {
    private static final String TAG = "PostNotification";

    private static final int SUMMARY_ID = 1337;
    private static final String NOTIFICATION_GROUP_KEY = "post_notification_group";

    private Context mContext;
    private NotificationManager mNotificationManager;

    public PostNotification(@NonNull Context context) {
        mContext = context.getApplicationContext();
        prepareNotification();
    }

    private void prepareNotification() {
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO: Create notification channel for devices with Android O and above.
        }
    }

    public void show(final Post post) {
        String url = ImageUtils.getCustomPostThumbnailImageUrl(post.getThumbnailImageUrl(),
                ScreenUtils.dpToPxInt(mContext, 64.0f),
                ScreenUtils.dpToPxInt(mContext, 64.0f));

        ImageFetcher.getBitmap(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Bitmap>(){
                    @Override
                    public void onNext(Bitmap bitmap) {
                        Logger.d(TAG, "Thumbnail for post fetched from url");
                        showPostNotification(post, bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError: " + e.getMessage(), e);
                        showPostNotification(post, null);
                    }

                    @Override
                    public void onComplete() {
                        // Done.
                    }
                });
    }

    private void showPostNotification(Post post, Bitmap bitmap) {
        showHeaderNotification();

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                post.getPostId(),
                PostDetailsActivity.getLaunchIntent(mContext, post.getPostId()),
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setColor(ContextCompat.getColor(mContext, R.color.notification_color_child))
                .setSmallIcon(R.drawable.ic_notification_predator)
                .setLargeIcon(bitmap != null ? bitmap : getBitmap(R.mipmap.ic_launcher))
                .setContentTitle(post.getName())
                .setContentText(post.getTagline())
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS |
                        NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(post.getPostId(), builder.build());
    }

    private void showHeaderNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setColor(ContextCompat.getColor(mContext, R.color.notification_color_header))
                .setContentTitle(mContext.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification_predator)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP_KEY);

        mNotificationManager.notify(SUMMARY_ID, builder.build());
    }

    private Bitmap getBitmap(@DrawableRes int drawableRes) {
        return BitmapFactory.decodeResource(mContext.getResources(),
                drawableRes);
    }
}
