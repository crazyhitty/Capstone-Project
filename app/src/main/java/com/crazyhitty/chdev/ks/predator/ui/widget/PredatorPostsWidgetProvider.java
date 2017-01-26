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

package com.crazyhitty.chdev.ks.predator.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.ui.activities.PostDetailsActivity;
import com.crazyhitty.chdev.ks.predator.ui.activities.SplashActivity;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/26/2017 5:15 PM
 * Description: Unavailable
 */

public class PredatorPostsWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "PredatorPostsWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int numberOfWidgetsAvail = appWidgetIds.length;
        for (int i = 0; i < numberOfWidgetsAvail; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent widgetServiceIntent = new Intent(context, PredatorPostsWidgetService.class);
            widgetServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            widgetServiceIntent.setData(Uri.parse(widgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_posts);

            views.setRemoteAdapter(R.id.list_view_posts, widgetServiceIntent);

            views.setEmptyView(R.id.list_view_posts, R.id.text_view_posts_unavailable);

            Intent clickIntentMyStocks = new Intent(context, SplashActivity.class);
            PendingIntent clickPendingIntentMyStocks = PendingIntent
                    .getActivity(context, 0,
                            clickIntentMyStocks,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.text_view_posts_unavailable, clickPendingIntentMyStocks);

            Intent clickIntentMyStockDetails = new Intent(context, PostDetailsActivity.class);
            PendingIntent clickPendingIntentMyStockDetails = PendingIntent
                    .getActivity(context, 0,
                            clickIntentMyStockDetails,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view_posts, clickPendingIntentMyStockDetails);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);

            Logger.d(TAG, "onUpdate: widgetId: " + appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
