package teamh.boostcamp.myapplication.view.graph;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.FragmentGraphBinding;
import teamh.boostcamp.myapplication.data.repository.DataRepository;
import teamh.boostcamp.myapplication.utils.ResourceSendUtil;
import teamh.boostcamp.myapplication.view.BaseFragment;

/**
 * 추상 클래스인 BaseFragment를 상속받음.
 * 추상 클래스의 추상 메소드를 구현해야 한다.
 */
public class LegacyGraphFragment extends BaseFragment<FragmentGraphBinding> implements GraphContractor.View {

    private static final String EMOTION = "선택한 감정";
    private static final String LAST_WEEK_EMOTION = "분석된 감정";
    private LegacyGraphPresenter presenter;
    private ResourceSendUtil resourceSendUtil;
    private Context context;
    private String[] days;
    private String[] emojis;
    private ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private View tagView;
    private TextView tagTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // binding할 때 만들어지는 View를 바로 리턴하면 된다.
        // getRoot()를 통해서!
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (resourceSendUtil == null) {
            resourceSendUtil = new ResourceSendUtil(context);
        }
        days = resourceSendUtil.getStringArray(R.array.graph_days);
        emojis = resourceSendUtil.getStringArray(R.array.graph_emojis);
        // Presenter 설정
        presenter = new LegacyGraphPresenter(LegacyGraphFragment.this, DataRepository.getInstance());
        binding.setPresenter(presenter);
        presenter.onViewAttached();
        presenter.loadHashTagWord(20);

        drawGraph();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    public void updateHashTagWord(ArrayList<String> list) {
        ArrayList<String> hashTagItems;
        hashTagItems = list;

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < hashTagItems.size(); i++) {
            tagView = inflater.inflate(R.layout.layout_graph_hash_tag, null, false);
            tagTextView = tagView.findViewById(R.id.tv_hash_tag);

            if (i % 3 == 0) {
                tagTextView.setTextColor(resourceSendUtil.getColor(R.color.graphColor));
            }

            tagTextView.setTextSize(30f);
            tagTextView.setText(hashTagItems.get(i));
            binding.hashTagCustomLayout.addView(tagView);
        }
    }

    @Override
    public void updateThisWeekEntries(java.util.List<Entry> thisWeekEntries) {
        final LineDataSet thisWeekLineDataSet = new LineDataSet(thisWeekEntries, EMOTION);
        setLineData(thisWeekLineDataSet, 1);
    }

    @Override
    public void updateLastWeekEntries(java.util.List<Entry> lastWeekEntries) {
        final LineDataSet lastWeekLineDataSet = new LineDataSet(lastWeekEntries, LAST_WEEK_EMOTION);
        setLineData(lastWeekLineDataSet, 2);
        final LineData lineData = new LineData(dataSets);
        binding.lcEmotionGraph.setData(lineData);
    }

    private void setLineData(LineDataSet lineDataSet, int type) {
        lineDataSet.setLineWidth(2); // 곡률
        lineDataSet.setCircleRadius(5); // 원 색상 지정
        switch (type) {
            case 1:
                lineDataSet.setCircleColor(resourceSendUtil.getColor(R.color.graphColor));
                lineDataSet.setCircleHoleColor(resourceSendUtil.getColor(R.color.graphColor));
                lineDataSet.setColor(resourceSendUtil.getColor(R.color.graphColor));
                break;
            case 2:
                lineDataSet.setCircleColor(resourceSendUtil.getColor(R.color.graph_analyzed_color));
                lineDataSet.setCircleHoleColor(resourceSendUtil.getColor(R.color.graph_analyzed_color));
                lineDataSet.setColor(resourceSendUtil.getColor(R.color.graph_analyzed_color));
                break;
        }
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);
        dataSets.add(lineDataSet);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onViewDetached();
        emojis = null;
        days = null;
        tagTextView = null;
        tagView = null;
    }

    private void drawGraph() {
        final XAxis xAxis;
        final YAxis yLeftAxis, yRightAxis;

        xAxis = binding.lcEmotionGraph.getXAxis();
        yLeftAxis = binding.lcEmotionGraph.getAxisLeft();
        yRightAxis = binding.lcEmotionGraph.getAxisRight();
        // X축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(R.color.black);
        xAxis.setValueFormatter(new GraphAxisValueFormatter(days));
        xAxis.enableGridDashedLine(8, 24, 0);

        // y축 왼쪽 설정
        yLeftAxis.setTextColor(R.color.black);
        // y축 텍스트 사이즈 지정.
        yLeftAxis.setTextSize(20f);
        yLeftAxis.setValueFormatter(new GraphAxisValueFormatter(emojis));
        // max 값
        yLeftAxis.setAxisMaximum(4.0f);
        // min 값
        yLeftAxis.setAxisMinimum(0.0f);
        yLeftAxis.setGranularityEnabled(true);
        // 증가 간격
        yLeftAxis.setGranularity(1.0f);
        yLeftAxis.setSpaceMax(500f);

        // y축 오른쪽 설정
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawGridLines(false);

        binding.lcEmotionGraph.setDescription(null);
        binding.lcEmotionGraph.setBackgroundColor(Color.TRANSPARENT);
        binding.lcEmotionGraph.setDoubleTapToZoomEnabled(false);
        binding.lcEmotionGraph.setDrawGridBackground(false);
        binding.lcEmotionGraph.animateY(2000, Easing.EaseInCubic);
        binding.lcEmotionGraph.invalidate();
    }
}
