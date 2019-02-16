package teamh.boostcamp.myapplication.view.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import teamh.boostcamp.myapplication.utils.NotificationHelper;

/**
 * BroadcastReceiver
 * NotificationHelper를 이용해 Notification 생성
 */
public class AlertReceiver extends BroadcastReceiver {

    private String action;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            action = intent.getAction();
        }
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder builder;
        builder = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, builder.build());
        /*switch (action) {
            case "setAlarm":
                builder = notificationHelper.getChannelNotification();
                notificationHelper.getManager().notify(1, builder.build());
                Log.v("999792", action);
                break;
            case "cancelAlarm":
                notificationHelper.getManager().cancelAll();
                Log.v("999792", action);
                break;
        }*/


    }
}
