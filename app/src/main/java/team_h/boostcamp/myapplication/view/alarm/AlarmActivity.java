package team_h.boostcamp.myapplication.view.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityAlarmBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class AlarmActivity extends BaseActivity<ActivityAlarmBinding> implements
        AlarmContractor.View, TimePickerDialog.OnTimeSetListener {


    private AlarmPresenter presenter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        // 람다식
        binding.switchAlarm.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                binding.llAlarmTimeLayout.setVisibility(View.VISIBLE);
            } else {
                binding.llAlarmTimeLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        presenter = new AlarmPresenter();
        binding.setActivity(AlarmActivity.this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm;
    }

    public void onClickButton(int id) {
        switch (id) {
            case R.id.iv_alarm_back_button:
                finish();
                break;
            case R.id.tv_alarm_done_button:
                // SharedPreference 저장하는 로직.
                startAlarm(calendar);
                break;
            case R.id.btn_alarm_select:
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "time picker");
                break;
        }
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        this.calendar = calendar;
        updateTimeText(calendar);
    }

    private void updateTimeText(Calendar calendar) {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        binding.tvAlarmTimeText.setText(timeText);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmActivity.this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

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
    }
}
