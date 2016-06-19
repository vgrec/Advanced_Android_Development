package com.example.android.sunshine.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

/**
 * Author vgrec, on 19.06.16.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        startWidgetIntentService(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        startWidgetIntentService(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SunshineSyncAdapter.ACTION_DATA_CHANGED.equals(intent.getAction())) {
            startWidgetIntentService(context);
        }
    }

    private void startWidgetIntentService(Context context) {
        Intent intentService = new Intent(context, WeatherIntentService.class);
        context.startService(intentService);
    }
}
