package com.example.android.sunshine.app.muzei;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.sync.SunshineSyncAdapter;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;

/**
 * Author vgrec, on 26.06.16.
 */
public class WeatherMuzeiSource extends MuzeiArtSource {

    private String[] WEATHER_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_SHORT_DESC = 1;


    public WeatherMuzeiSource() {
        super("WeatherMuzeiSource");
    }

    @Override
    protected void onUpdate(int reason) {
        String location = Utility.getPreferredLocation(this);
        Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                location, System.currentTimeMillis());
        Cursor cursor = getContentResolver().query(weatherUri, WEATHER_PROJECTION, null, null, null);

        if (cursor == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            int weatherId = cursor.getInt(INDEX_WEATHER_ID);
            String desc = cursor.getString(INDEX_SHORT_DESC);

            String imageUri = Utility.getImageUrlForWeatherCondition(weatherId);
            if (imageUri != null) {
                publishArtwork(new Artwork.Builder()
                        .imageUri(Uri.parse(imageUri))
                        .title(desc)
                        .byline(location)
                        .viewIntent(new Intent(this, MainActivity.class))
                        .build());
            }
        }
        cursor.close();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        boolean dataUpdated = intent != null &&
                SunshineSyncAdapter.ACTION_DATA_CHANGED.equals(intent.getAction());
        if (dataUpdated && isEnabled()) {
            onUpdate(UPDATE_REASON_OTHER);
        }
    }
}
