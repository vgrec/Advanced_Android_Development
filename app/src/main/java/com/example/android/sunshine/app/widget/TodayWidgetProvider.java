package com.example.android.sunshine.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;

/**
 * Author vgrec, on 19.06.16.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        int weatherArtResourceId = R.drawable.art_clear;
        double maxTemp = 24;
        String formattedMaxTemperature = Utility.formatTemperature(context, maxTemp);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_today);

            remoteViews.setImageViewResource(R.id.widget_weather_icon, weatherArtResourceId);
            remoteViews.setTextViewText(R.id.widget_temperature, formattedMaxTemperature);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
