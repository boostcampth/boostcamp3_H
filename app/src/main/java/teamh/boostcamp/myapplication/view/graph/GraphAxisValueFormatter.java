package teamh.boostcamp.myapplication.view.graph;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * MPChart LineData의 생성자에 label이 들어가지 않음.
 * IAxisValueFormatter 인터페이스를 구현함으로써 해결이 가능
 */
public class GraphAxisValueFormatter implements IAxisValueFormatter {
    private String[] values;

    // 생성자
    GraphAxisValueFormatter(String[] values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.v("Test",String.valueOf(value));

        try {
            int index = (int) value;
            return values[index];
        }catch (Exception e){
            return "";
        }
    }

}
