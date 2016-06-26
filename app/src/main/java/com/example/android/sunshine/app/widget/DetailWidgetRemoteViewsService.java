package com.example.android.sunshine.app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Author vgrec, on 25.06.16.
 */
public class DetailWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DetailWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

}
