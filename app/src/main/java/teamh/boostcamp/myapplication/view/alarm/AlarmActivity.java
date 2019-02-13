package teamh.boostcamp.myapplication.view.alarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity implements
        AlarmView, TimePickerDialog.OnTimeSetListener {

    private ActivityAlarmBinding binding;
    private AlarmPresenter presenter;
    private Calendar calendar;
    private boolean isChecked = false;
    @Nullable
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        init();
    }

    private void init() {
        SharedPreferenceManager.getInstance(AlarmActivity.this);

        initViews();
        initPresenter();
    }

    private void initViews() {
        binding.setActivity(AlarmActivity.this);

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

    private void initPresenter() {
        presenter = new AlarmPresenter(AlarmActivity.this, new AlarmHelperImpl(getApplicationContext()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calendar = null;
        presenter = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
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
        presenter.loadTimeText(calendar);
    }

    @Override
    public void checkState() {
        time = SharedPreferenceManager.getInstance(AlarmActivity.this).getPreferencePushTime(null);
        System.out.println("time test : " + time);
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
    public void updateCancelTimeText(boolean isCanceled) {
        if (isCanceled) {
            binding.tvAlarmTimeText.setText(getApplicationContext().getResources().getString(R.string.alarm_explain));
        } else {
            binding.tvAlarmTimeText.setText(getApplicationContext().getResources().getString(R.string.alarm_error_text));
        }
    }

    public void onClickAlarmButtons(int id) {
        switch (id) {
            case R.id.iv_back_button:
                finish();
                break;
            case R.id.tv_done_button:
                if (isChecked) {
                    isEmptyCalendar(calendar);
                } else {
                    finish();
                }
                break;
            case R.id.btn_alarm_select:
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.show(getSupportFragmentManager(), "time picker");
                break;
        }
    }

    private void isEmptyCalendar(Calendar calendar) {
        if (calendar == null) {
            if (!time.equals(getApplicationContext().getResources().getString(R.string.alarm_explain))) {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.alarm_modify_text), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.alarm_explain), Toast.LENGTH_SHORT).show();
            }

        } else {
            presenter.setAlarm(calendar);
            showToast(getApplicationContext().getResources().getString(R.string.alarm_set_text));
            finish();
        }
    }
}
