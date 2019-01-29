package team_h.boostcamp.myapplication.view.graph;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import team_h.boostcamp.myapplication.R;

public class GraphPresenter implements GraphContractor.Presenter {
    public static final ObservableField<String> observer = new ObservableField<>("Statics");
    private List<Entry> entries = new ArrayList<>();
    private String[] mDays = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
    private String[] mEmojis = new String[5];
    private GraphContractor.View view;
    private Context mContext;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private XAxis xAxis;
    private YAxis yLAxis, yRAxis;
    private Description description;

    GraphPresenter(GraphContractor.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void onViewAttached() {
        mEmojis[0] = "\uD83D\uDE0D";
        mEmojis[1] = "\uD83D\uDE0E";
        mEmojis[2] = "\uD83D\uDE21";
        mEmojis[3] = "\uD83D\uDE0D";
        mEmojis[4] = "\uD83D\uDE10";
        entries.add(new Entry(0,0, mEmojis[0]));
        entries.add(new Entry(1, 1, mEmojis[1]));
        entries.add(new Entry(2, 2, mEmojis[2]));
      /*  entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 2));
        entries.add(new Entry(6, 1));*/
        lineDataSet = new LineDataSet(entries, "Emotion");

        lineDataSet.setLineWidth(2);
        // 곡률
        lineDataSet.setCircleRadius(5);
        // 원 색상 지정
        lineDataSet.setCircleColor(ContextCompat.getColor(mContext, R.color.graphColor));
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(mContext, R.color.graphColor));
        lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.graphColor));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);
        lineData = new LineData(lineDataSet);

        view.setLineData(lineData);
        xAxis = view.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(R.color.black);
        xAxis.setValueFormatter(new GraphAxisValueFormatter(mDays));
        xAxis.enableGridDashedLine(8, 24, 0);

        yLAxis = view.getYLeftAxis();
        yLAxis.setTextColor(R.color.black);
        // 비활성화
        yRAxis = view.getYRightAxis();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        // y축 텍스트 사이즈 지정.
        yLAxis.setTextSize(20f);
        yLAxis.setValueFormatter(new GraphYAxisValueFormatter(mEmojis));

        description = new Description();
        description.setText("");

        view.setDescription(description);
    }

    @Override
    public void onViewDetached() {
        // 리소스 해제
        mContext = null;
        mEmojis = null;
    }
}
