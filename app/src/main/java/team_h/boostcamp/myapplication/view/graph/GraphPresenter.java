package team_h.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.model.DataRepository;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;

public class GraphPresenter implements GraphContractor.Presenter {
    public static final ObservableField<String> OBSERVER = new ObservableField<>("Statics");
    public static final ObservableField<String> HASHTAG_OBSERVER = new ObservableField<>("#HashTags");
    private List<Entry> entries = new ArrayList<>();
    private GraphContractor.View view;
    private ResourceSendUtil resourceSendUtil;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private DataRepository dataRepository;

    GraphPresenter(GraphContractor.View view, ResourceSendUtil resourceSendUtil, DataRepository dataRepository) {
        this.view = view;
        this.resourceSendUtil = resourceSendUtil;
        this.dataRepository = dataRepository;
    }

    @Override
    public void onViewAttached() {
        initEntry();
        view.updateEntries(entries);
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
/*
    void setLineDataSet() {
        view.updateEntries(entries);
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
    }*/

    @Override
    public void onViewDetached() {
        // 리소스 해제
    }

    @Override
    public void loadHashTagWord(int size) {
        dataRepository.getHashTag(size, list -> {
            if(list !=null){
                view.updateHashTagWord(list);
            }
        });
    }
}
