package team_h.boostcamp.myapplication.view.graph;

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
        return values[(int) value];
    }
}
