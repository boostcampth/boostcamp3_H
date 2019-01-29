package team_h.boostcamp.myapplication.view.graph;

import android.content.Context;

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
    private String[] mEmojis = {"\uD83D\uDE21", "😞", "\uD83D\uDE10", "\uD83D\uDE0A", "\uD83D\uDE0D"};
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
        initEntry();

        setLineDataset();

        view.setLineData(lineData);
        xAxis = view.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(R.color.black);
        xAxis.setValueFormatter(new GraphAxisValueFormatter(mDays));
        xAxis.enableGridDashedLine(8, 24, 0);

        yLAxis = view.getYLeftAxis();
        yLAxis.setTextColor(R.color.black);
        // y축 텍스트 사이즈 지정.
        yLAxis.setTextSize(20f);
        yLAxis.setValueFormatter(new GraphYAxisValueFormatter(mEmojis));
        // max 값
        yLAxis.setAxisMaximum(4.0f);
        // min 값
        yLAxis.setAxisMinimum(0.0f);
        yLAxis.setGranularityEnabled(true);
        // 증가 간격
        yLAxis.setGranularity(1.0f);
        yLAxis.setSpaceMax(500f);

        // 비활성화
        setDisableYRightAxis();
    }

    void initEntry() {
        entries.add(new Entry(0, 1.0f));
        entries.add(new Entry(1, 2.0f));
        entries.add(new Entry(2, 3.0f));
        entries.add(new Entry(3, 4.0f));
        entries.add(new Entry(4, 2.0f));
        entries.add(new Entry(5, 4.0f));
        entries.add(new Entry(6, 0.0f));
    }

    void setLineDataset(){
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
    }

    void setDisableYRightAxis() {
        yRAxis = view.getYRightAxis();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
    }

    @Override
    public void onViewDetached() {
        // 리소스 해제
        mContext = null;
        mEmojis = null;
    }
}
