package teamh.boostcamp.myapplication.view.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import teamh.boostcamp.myapplication.utils.NotificationHelper;

/**
 * BroadcastReceiver
 * NotificationHelper를 이용해 Notification 생성
 * */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder builder = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, builder.build());
    }
}
