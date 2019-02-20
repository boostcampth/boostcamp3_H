package teamh.boostcamp.myapplication.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.LogEvent;

public class FirebaseEventLogger {

    private static volatile FirebaseEventLogger INSTANCE;

    private volatile FirebaseAnalytics logger;

    private FirebaseEventLogger(@NonNull Context context) {
        logger = FirebaseAnalytics.getInstance(context);
    }

    public static FirebaseEventLogger getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (FirebaseEventLogger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseEventLogger(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public void addLogEvent(@NonNull LogEvent logEvent) {
        Bundle eventData = new Bundle();
        eventData.putString(FirebaseAnalytics.Param.ITEM_ID, logEvent.getItemId());
        eventData.putString(FirebaseAnalytics.Param.ITEM_NAME, logEvent.getItemName());
        eventData.putString(FirebaseAnalytics.Param.CONTENT_TYPE, logEvent.getContentType());
        logger.logEvent(logEvent.getEventName(), eventData);
    }
}
