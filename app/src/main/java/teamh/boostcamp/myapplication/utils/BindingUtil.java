package teamh.boostcamp.myapplication.utils;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.databinding.BindingAdapter;
import teamh.boostcamp.myapplication.data.model.Emotion;

public class BindingUtil {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /* 선택된 감정을 이모지로 변경 */
    @BindingAdapter({"emotion", "entries"})
    public static void convertTextToEmotion(TextView textView, Emotion emotion, String[] entries) {
        textView.setText(entries[emotion.getEmotion()]);
    }

    /* 선택된 일자를 형식에 맞게 변경 */
    @BindingAdapter({"date", "month"})
    public static void convertDateForm(TextView textView, Date date, String[] month) {
        String[] strings = SIMPLE_DATE_FORMAT.format(date).split("-");

        textView.setText(String.format("%s %s, %s",
                strings[0],
                month[Integer.parseInt(strings[1])],
                strings[2]));
    }

    @BindingAdapter({"tags"})
    public static void convertTagListToString(TextView textView, List<String> tags) {
        textView.setText(tags.toString());
    }
}
