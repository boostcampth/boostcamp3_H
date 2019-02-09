package teamh.boostcamp.myapplication.view.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.utils.ResourceSendUtil;
import teamh.boostcamp.myapplication.utils.SharedPreference;

public class AlarmPresenter implements AlarmContractor.Presenter {

    private AlarmContractor.View view;
    private Context context;
    private ResourceSendUtil resourceSendUtil;

    AlarmPresenter(AlarmContractor.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onViewAttached() {
        SharedPreference.getInstance().loadSharedPreference(context);
        resourceSendUtil = new ResourceSendUtil(context);
    }

    @Override
    public void onViewDetached() {
        view = null;
        context = null;
    }

    @Override
    public void setAlarm(Calendar calendar) {

        final long currentTime = System.currentTimeMillis();
        final long INTERVAL_TIME = 1000 * 60 * 60 * 24;
        long userAlarmTime = calendar.getTimeInMillis();

        // 설정한 시간이 현재 시간보다 작다면 다음 날 울리도록 INTERVAL_TIME 을 더한다.
        if (currentTime > userAlarmTime) {
            userAlarmTime += INTERVAL_TIME;
        }

        // 알람 매니저가 AlertReceiver 라는 브로드 캐스트 리시버로 펜딩 인텐트로 날린다.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        // 버전별 알람 매니저에게 시간을 설정.
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, userAlarmTime, pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, userAlarmTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, userAlarmTime, pendingIntent);
            }
        }
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, userAlarmTime, INTERVAL_TIME, pendingIntent);

        // SharedPreference 저장하는 로직. String으로 저장.
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        SharedPreference.getInstance().setPreferencePushTime(timeText);
    }

    @Override
    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "아랆 취소함~~",Toast.LENGTH_SHORT).show();
        view.updateTimeText(resourceSendUtil.getString(R.string.alarm_explain));
    }

    @Override
    public void loadCalendar(Calendar calendar) {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        view.updateTimeText(timeText);
    }

}
