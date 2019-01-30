package team_h.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;

public class GraphPresenter implements GraphContractor.Presenter {
    public static final ObservableField<String> OBSERVER = new ObservableField<>("Statics");
    public static final ObservableField<String> HASHTAG_OBSERVER = new ObservableField<>("#HashTags");
    private List<Entry> entries = new ArrayList<>();
    private String[] hashtags = new String[20];
    private String[] mDays;
    private String[] mEmojis = {"\uD83D\uDE21", "😞", "\uD83D\uDE10", "\uD83D\uDE0A", "\uD83D\uDE0D"};
    private GraphContractor.View view;
    private ResourceSendUtil resourceSendUtil;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private XAxis xAxis;
    private YAxis yLAxis, yRAxis;

    GraphPresenter(GraphContractor.View view, ResourceSendUtil resourceSendUtil) {
        this.view = view;
        this.resourceSendUtil = resourceSendUtil;
    }

    @Override
    public void onViewAttached() {
        mDays = resourceSendUtil.getStringArray(R.array.graph_days);
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
        initHashTagWord();
    }

    void initHashTagWord() {
        /**
         * Model로부터 데이터를 가지고 와서 View에게 넘겨준다.
         * 이 부분에서 Model에 접근해서 가공된 데이터를 가지고 오는 로직이 있어야 함.
         *
         * 지금은 Presenter에서 만든 데이터를 View 쪽으로 넘겨주고 있다.
         * */
        for (int i = 0; i < hashtags.length; i++) {
            hashtags[i] = "#tag" + i;
        }
        view.loadHastTagWord(hashtags);
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

    void setLineDataset() {
        lineDataSet = new LineDataSet(entries, "Emotion");

        lineDataSet.setLineWidth(2);
        // 곡률
        lineDataSet.setCircleRadius(5);
        // 원 색상 지정
        lineDataSet.setCircleColor(resourceSendUtil.getColor(R.color.graphColor));
        lineDataSet.setCircleHoleColor(resourceSendUtil.getColor(R.color.graphColor));
        lineDataSet.setColor(resourceSendUtil.getColor(R.color.graphColor));
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
        mEmojis = null;
    }
}
