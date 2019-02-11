package teamh.boostcamp.myapplication.view.alarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreference;
import teamh.boostcamp.myapplication.databinding.ActivityAlarmBinding;
import teamh.boostcamp.myapplication.view.BaseActivity;
import teamh.boostcamp.myapplication.view.Handlers;

public class AlarmActivity extends BaseActivity<ActivityAlarmBinding> implements
        AlarmContractor.View, TimePickerDialog.OnTimeSetListener, Handlers {

    private AlarmPresenter presenter;
    private Calendar calendar;
    private boolean isChecked = false;
    private AlarmHelperImpl alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        // 람다식
        binding.switchAlarm.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                this.isChecked = true;
                setVisibility(true);
            } else {
                this.isChecked = false;
                presenter.cancelAlarm();
                setVisibility(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //presenter.onViewDetached();
        calendar = null;
        presenter = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
    }

    private void initView() {
        SharedPreference.getInstance().loadSharedPreference(AlarmActivity.this);
        alarmHelper = new AlarmHelperImpl(getApplicationContext());
        presenter = new AlarmPresenter(AlarmActivity.this, alarmHelper);
        binding.setActivity(AlarmActivity.this);
        binding.setHandlers(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm;
    }

    public void onClickButton(int id) {
        switch (id) {
            case R.id.btn_alarm_select:
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.show(getSupportFragmentManager(), "time picker");
                break;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTimeText(String timeText) {
        binding.tvAlarmTimeText.setText(timeText);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        this.calendar = calendar;
        presenter.loadCalendar(calendar);
    }

    @Override
    public void checkState() {
        String time = SharedPreference.getInstance().getPreferencePushTime(null);
        if (time != null) {
            setVisibility(true);
            binding.tvAlarmTimeText.setText(time);
            binding.switchAlarm.setChecked(true);
        } else {
            setVisibility(false);
        }
    }

    @Override
    public void setVisibility(boolean isChecked) {
        if (isChecked) {
            binding.llAlarmTimeLayout.setVisibility(View.VISIBLE);
        } else {
            binding.llAlarmTimeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickListener(int id) {
        switch (id) {
            case R.id.iv_back_button:
                finish();
                break;
            case R.id.tv_done_button:
                if (isChecked) {
                    if (calendar == null) {
                        showToast("시간을 선택해주세요.");
                    } else {
                        presenter.setAlarm(calendar);
                        showToast("알람을 설정하였습니다.");
                        finish();
                    }
                } else {
                    finish();
                }
                break;
        }
    }
}
