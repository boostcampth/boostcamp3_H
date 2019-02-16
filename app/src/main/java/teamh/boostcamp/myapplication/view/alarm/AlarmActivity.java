package teamh.boostcamp.myapplication.view.alarm;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity implements
        AlarmView {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
    private SharedPreferenceManager sharedPreferenceManager;
    private static final String TIME_PICKER_TAG = "Time Picker";
    private ActivityAlarmBinding binding;
    private AlarmPresenter presenter;
    private Calendar calendar;
    private boolean isChecked = false;
    @Nullable
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
        initBinding();
        initActionBar();
        initPresenter();
        initViews();
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        binding.setActivity(AlarmActivity.this);
    }

    private void initActionBar() {
        Toolbar toolbar = binding.toolbarAlarm;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initPresenter() {
        presenter = new AlarmPresenter(AlarmActivity.this, new AlarmHelperImpl(getApplicationContext()));
    }

    private void initViews() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calendar = null;
        presenter = null;
        sharedPreferenceManager = null;
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

    }
/*

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        this.calendar = calendar;
        presenter.loadTimeText(calendar);
        setVisibility(true);
    }
*/

    @Override
    public void checkState() {
        time = sharedPreferenceManager.getPreferencePushTime(null);
        //Log.v("check 21023 : ",time);
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
            //String date = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());
            if(sharedPreferenceManager.getPreferencePushTime() !=null){
                binding.tvAlarmTimeText.setText(time);
            }else {
                binding.tvAlarmTimeText.setText("시간을 설정해주세요.");
            }
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

    // presenter로 보내 알람 설정
    @Override
    public void updateCalendar(Calendar calendar) {
        if (calendar != null) {
            String time = simpleDateFormat.format(calendar.getTime());
            binding.tvAlarmTimeText.setText(time);
            Log.v("21023 : ", time);
            Log.v("21023 : ", calendar.toString());
            Log.v("21023 : ", String.valueOf(calendar.getTimeInMillis()));
            presenter.setAlarm(calendar);
        }
    }

    public void ll_alarm_time_layout(int id) {
        switch (id) {
            // 시간을 클릭하여 numberpicker를 띄움.
            case R.id.ll_alarm_time_layout:
                CustomTimePicker customTimePicker = CustomTimePicker.newInstance(this);
                customTimePicker.show(getSupportFragmentManager(), TIME_PICKER_TAG);
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
