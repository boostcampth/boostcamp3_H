package teamh.boostcamp.myapplication.view.alarm;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity implements AlarmView {

    private static final String TIME_PICKER_TAG = "Time Picker";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
    private SharedPreferenceManager sharedPreferenceManager;
    private ActivityAlarmBinding binding;
    private AlarmPresenter presenter;
    @NonNull
    private List<String> hourList;
    @NonNull
    private List<String> minuteList;
    @NonNull
    private Calendar calendar;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        init();
        generateTimeValues();
    }

    private void init() {
        calendar = Calendar.getInstance();
        sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
        time = sharedPreferenceManager.getPreferencePushTime(null);
        initBinding();
        initActionBar();
        initPresenter();
        initViews();
    }

    // 배열 생성
    private void generateTimeValues() {
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            hourList.add(String.format("%02d", i));
        }

        for (int i = 0; i < 12; i++) {
            minuteList.add(String.format("%02d", (i * 5)));
        }
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        binding.setActivity(AlarmActivity.this);
    }

    private void initActionBar() {
        Toolbar toolbar = binding.toolbarAlarm;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initViews() {
        binding.switchAlarm.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                setActivate(true);
            } else {
                setActivate(false);

                presenter.cancelAlarm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState(time);
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

    private void initPresenter() {
        presenter = new AlarmPresenter(AlarmActivity.this, new AlarmHelperImpl(getApplicationContext()));
    }

    @Override
    public void checkState(String time) {
        if (time != null) {
            setActivate(true);
            binding.switchAlarm.setChecked(true);
            binding.tvAlarmText.setText(String.format(getString(R.string.alarm_description_text), time));
            setNumberPickerValues(time);
        } else {
            setActivate(false);
            binding.switchAlarm.setChecked(false);
            binding.tvAlarmText.setText(getString(R.string.alarm_explain));
            setNumberPickerValues(null);
        }
    }

    private void setNumberPickerValues(String time) {

        int hourIndex = 0;
        int minuteIndex = 0;
        if (time != null) {
            String[] times = time.split(":");

            for (int i = 0; i < hourList.size(); i++) {
                if (times[0].equals(hourList.get(i))) {
                    hourIndex = i;
                }
            }

            for (int i = 0; i < minuteList.size(); i++) {
                if (times[1].equals(minuteList.get(i))) {
                    minuteIndex = i;
                }
            }
        }

        String[] hourValues = new String[hourList.size()];
        hourValues = hourList.toArray(hourValues);
        Log.v("15855 Test",String.valueOf(hourValues.length));
        String[] minuteValues = new String[minuteList.size()];
        minuteValues = minuteList.toArray(minuteValues);
        Log.v("15855 Test",String.valueOf(minuteValues.length));

        binding.npAlarmHour.setDisplayedValues(hourValues);
        binding.npAlarmHour.setMinValue(0);
        binding.npAlarmHour.setMaxValue(hourValues.length - 1);
        binding.npAlarmHour.setValue(Integer.parseInt(hourValues[hourIndex]));

        binding.npAlarmMinute.setDisplayedValues(minuteValues);
        binding.npAlarmMinute.setMinValue(0);
        binding.npAlarmMinute.setMaxValue(minuteValues.length - 1);
        binding.npAlarmMinute.setValue(minuteIndex);
    }


    @Override
    public void setActivate(boolean isActivated) {
        if (isActivated) {
            binding.npAlarmHour.setEnabled(true);
            binding.npAlarmMinute.setEnabled(true);
            binding.buttonAlarmSet.setEnabled(true);
            binding.buttonAlarmSet.setEnabled(true);
        } else {
            binding.npAlarmHour.setEnabled(false);
            binding.npAlarmMinute.setEnabled(false);
            binding.buttonAlarmSet.setEnabled(false);
            binding.buttonAlarmSet.setEnabled(false);
        }
    }

    @Override
    public void updateCancelTimeText(boolean isCanceled) {
        if (isCanceled) {
            binding.tvAlarmText.setText(getApplicationContext().getResources().getString(R.string.alarm_explain));
        } else {
            binding.tvAlarmText.setText(getApplicationContext().getResources().getString(R.string.alarm_error_text));
        }
    }

    @Override
    public void updateCalendar(Calendar calendar) {
        if (calendar != null) {
            String time = simpleDateFormat.format(calendar.getTime());
            binding.tvAlarmText.setText(time);
            presenter.setAlarm(calendar);
        }
    }

    @Override
    public void showAlarmSuccessMessage() {
        showToast(R.string.alarm_set_text);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getApplicationContext(), getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public void onAlarmButtonClick(int id) {
        switch (id) {
            case R.id.button_alarm_set:
                createData();
                break;
        }
    }

    private void createData() {
        int hourIndex = binding.npAlarmHour.getValue();
        String hour = hourList.get(hourIndex);
        int minuteIndex = binding.npAlarmMinute.getValue();
        String minute = minuteList.get(minuteIndex);

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        calendar.set(Calendar.SECOND, 0);
        presenter.setAlarm(calendar);
        showAlarmSuccessMessage();
        binding.tvAlarmText.setText(String.format(getString(R.string.alarm_description_text), simpleDateFormat.format(calendar.getTime())));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
