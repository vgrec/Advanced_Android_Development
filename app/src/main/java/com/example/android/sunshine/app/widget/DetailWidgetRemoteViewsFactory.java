package com.example.android.sunshine.app.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Author vgrec, on 25.06.16.
 */
public class DetailWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };
    // these indices must match the projection
    static final int INDEX_WEATHER_ID = 0;
    static final int INDEX_WEATHER_DATE = 1;
    static final int INDEX_WEATHER_CONDITION_ID = 2;
    static final int INDEX_WEATHER_DESC = 3;
    static final int INDEX_WEATHER_MAX_TEMP = 4;
    static final int INDEX_WEATHER_MIN_TEMP = 5;

    private Cursor data = null;
    private Context context;

    public DetailWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // do nothing
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }

        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission
        final long identityToken = Binder.clearCallingIdentity();
        String location = Utility.getPreferredLocation(context);
        Uri weatherForLocationUri = WeatherContract.WeatherEntry
                .buildWeatherLocationWithStartDate(location, System.currentTimeMillis());
        data = context.getContentResolver().query(weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_today_list_item);
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        int weatherArtResourceId = Utility.getIconResourceForWeatherCondition(weatherId);

        String description = data.getString(INDEX_WEATHER_DESC);
        long dateInMillis = data.getLong(INDEX_WEATHER_DATE);
        String formattedDate = Utility.getFriendlyDayString(context, dateInMillis, false);
        double maxTemp = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        double minTemp = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String formattedMaxTemperature =
                Utility.formatTemperature(context, maxTemp);
        String formattedMinTemperature =
                Utility.formatTemperature(context, minTemp);
        views.setImageViewResource(R.id.widget_list_item_icon, weatherArtResourceId);
        views.setTextViewText(R.id.widget_date, formattedDate);
        views.setTextViewText(R.id.widget_description, description);
        views.setTextViewText(R.id.widget_high_temperature, formattedMaxTemperature);
        views.setTextViewText(R.id.widget_min_temperature, formattedMinTemperature);

        final Intent fillInIntent = new Intent();
        String locationSetting =
                Utility.getPreferredLocation(context);
        Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                locationSetting,
                dateInMillis);
        fillInIntent.setData(weatherUri);
        views.setOnClickFillInIntent(R.id.widget_today_list_item, fillInIntent);
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget_today_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (data.moveToPosition(position))
            return data.getLong(INDEX_WEATHER_ID);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
