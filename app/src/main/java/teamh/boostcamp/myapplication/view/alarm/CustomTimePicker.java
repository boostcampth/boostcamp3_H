package teamh.boostcamp.myapplication.view.alarm;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import teamh.boostcamp.myapplication.R;

public class CustomTimePicker extends DialogFragment implements NumberPicker.OnValueChangeListener {

    private static AlarmView views;
    private Dialog dialog;
    private NumberPicker hourNumberPicker;
    private NumberPicker minuteNumberPicker;
    private TextView tvAlarmOk;
    private TextView tvAlarmCancel;
    // SimpleDateFormat을 이용해 시간 관련 값들을 편하게 바꿀 수 있음.
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
    private Calendar calendar;
    private List<String> hourList;
    private List<String> minuteList;


    public CustomTimePicker() {
        // default 생성자.

    }

    public static CustomTimePicker newInstance(@NonNull AlarmView alarmView) {
        views = alarmView;
        return new CustomTimePicker();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        generateTimeValues();
        initDialog();
        return dialog;
    }

    private void initDialog() {
        calendar = Calendar.getInstance();
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_alarm);
        setNumberPickerValues();
        setButtonClick();
    }

    private void setNumberPickerValues(){
        hourNumberPicker = dialog.findViewById(R.id.np_dialog_alarm_hour);
        minuteNumberPicker = dialog.findViewById(R.id.np_dialog_alarm_minute);
        tvAlarmCancel = dialog.findViewById(R.id.tv_alarm_cancel);
        tvAlarmOk = dialog.findViewById(R.id.tv_alarm_ok);

        hourNumberPicker.setMinValue(00);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setDisplayedValues(hourList.toArray(new String[0]));

        minuteNumberPicker.setMinValue(00);
        minuteNumberPicker.setMaxValue(12);
        minuteNumberPicker.setDisplayedValues(minuteList.toArray(new String[0]));
    }

    private void generateTimeValues() {
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();

        for (int i = 0; i <= 24; i++) {
            hourList.add(String.format("%02d", i));
        }

        for (int i = 0; i <= 12; i++) {
            minuteList.add(String.format("%02d", (i*5)));
        }
    }

    private void setButtonClick() {
        tvAlarmCancel.setOnClickListener(view -> {
            dismiss();
        });

        tvAlarmOk.setOnClickListener(view -> {
            int hourIndex = hourNumberPicker.getValue();
            String hour = hourList.get(hourIndex);
            int minuteIndex = minuteNumberPicker.getValue();
            String minute = minuteList.get(minuteIndex);

            try {
                calendar.setTime(simpleDateFormat.parse(hour + ":" + minute));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("1156 Testing", String.valueOf(calendar.getTime()));
            // 밑에 꺼 사용 하면 될 듯.
            Log.v("1156 Testing", String.valueOf(simpleDateFormat.format(calendar.getTime())));
            Log.v("1156 Testing", String.valueOf(calendar));
            String time = simpleDateFormat.format(calendar.getTime());
            views.updateCalendar(calendar);

            dismiss();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        hourNumberPicker = null;
        minuteNumberPicker = null;
        tvAlarmCancel = null;
        tvAlarmOk = null;
        hourList = null;
        minuteList = null;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(getActivity().getApplicationContext(), numberPicker.getValue(), Toast.LENGTH_SHORT).show();
    }

}
