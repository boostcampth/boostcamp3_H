package teamh.boostcamp.myapplication.view.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;

public class AlarmHelperImpl implements AlarmHelper {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private Context context;
    private AlarmManager alarmManager;
    private SharedPreferenceManager sharedPreferenceManager;


    AlarmHelperImpl(@NonNull Context context) {
        this.context = context;
        this.sharedPreferenceManager = SharedPreferenceManager.getInstance(context); // 초기화 작업
    }

    @NonNull
    @Override
    public String loadCalendar(@NonNull Calendar calendar) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
    }

    // 알람 설정
    @Override
    public void setAlarm(@NonNull Calendar calendar) {
        final long currentTime = System.currentTimeMillis();
        final long INTERVAL = 1000 * 2 * 60;
        final long INTERVAL_TIME = TimeUnit.DAYS.toMillis(1); // 하루를 밀리초로 변환
        long userSettingTime = calendar.getTimeInMillis();
        Log.v("22758 current : ",String.valueOf(currentTime));
        Log.v("22758  INTERVAL_TIME : ",String.valueOf(INTERVAL_TIME));
        Log.v("22758 userSetting : ",String.valueOf(userSettingTime));

        // 설정한 시간이 현재 시간보다 작다면 다음 날 울리도록 INTERVAL_TIME 을 더한다.
        if (currentTime > userSettingTime) {
            userSettingTime += INTERVAL_TIME;
        }

        calendar.setTimeInMillis(userSettingTime);
        Log.v("22758 userSetting2 : ",String.valueOf(userSettingTime));
        //Log.v("22758 userSetting2 : ",String.valueOf(calendar.setTimeInMillis(userSettingTime)));

        // 알람 매니저가 AlertReceiver 라는 브로드 캐스트 리시버로 펜딩 인텐트로 날린다.
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        // 버전별 알람 매니저에게 시간을 설정.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, userSettingTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, userSettingTime, pendingIntent);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, userSettingTime, INTERVAL, pendingIntent);

        // SharedPreferenceManager 저장하는 로직. String으로 저장.
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        String time = simpleDateFormat.format(calendar.getTime());
        Log.v("22758 timeText : ",timeText);
        Log.v("22758 time : ",time);
        sharedPreferenceManager.setPreferencePushTime(time);
    }

    // 알람 해제
    @NonNull
    @Override
    public boolean cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intent = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

            alarmManager.cancel(pendingIntent);
            sharedPreferenceManager.removePreferencePushTime();
            Log.v("22758 alarm cancel : ","cancel");
            return true; // 시간 설정 text
        } else {
            return false; // 오류 text
        }
    }
}
