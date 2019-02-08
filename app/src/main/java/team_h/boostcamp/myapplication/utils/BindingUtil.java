package team_h.boostcamp.myapplication.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingUtil {

    /* 선택된 감정을 이모지로 변경 */
    @BindingAdapter({"emotion", "entries"})
    public static void convertTextToEmotion(TextView textView, int emotion, String[] entries) {
        textView.setText(entries[emotion]);
    }

    /* 선택된 일자를 형식에 맞게 변경 */
    @BindingAdapter({"date", "month"})
    public static void convertDateForm(TextView textView, String date, String[] month) {
        String[] strings = date.split("-");

        textView.setText(String.format("%s %s, %s",
                strings[0],
                month[Integer.parseInt(strings[1])],
                strings[2]));
    }
}
