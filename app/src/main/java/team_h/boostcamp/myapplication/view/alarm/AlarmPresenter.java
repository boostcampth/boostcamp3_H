package team_h.boostcamp.myapplication.view.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.DateFormat;
import java.util.Calendar;

import team_h.boostcamp.myapplication.utils.SharedPreference;

public class AlarmPresenter implements AlarmContractor.Presenter {

    private AlarmContractor.View view;
    private Context context;

    AlarmPresenter(AlarmContractor.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onViewAttached() {
        SharedPreference.getInstance().loadSharedPreference(context);
    }

    @Override
    public void onViewDetached() {
        view = null;
        context = null;
    }

    @Override
    public void setAlarm(Calendar calendar) {
        // 알람 매니저가 AlertReceiver 라는 브로드 캐스트 리시버로 펜딩 인텐트로 날린다.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        // 버전별 알람 매니저에게 시간을 설정.
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        // SharedPreference 저장하는 로직.
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        SharedPreference.getInstance().setPreferencePushTime(timeText);
    }

    @Override
    public void loadCalendar(Calendar calendar) {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        view.updateTimeText(timeText);
    }

}
