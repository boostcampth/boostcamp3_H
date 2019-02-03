package team_h.boostcamp.myapplication.view.graph;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentGraphBinding;
import team_h.boostcamp.myapplication.model.DataRepository;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;
import team_h.boostcamp.myapplication.view.BaseFragment;

/**
 * 추상 클래스인 BaseFragment를 상속받음.
 * 추상 클래스의 추상 메소드를 구현해야 한다.
 */
public class GraphFragment extends BaseFragment<FragmentGraphBinding> implements GraphContractor.View {

    public static final String EMOTION = "Emotion";
    private GraphPresenter presenter;
    private ResourceSendUtil resourceSendUtil;
    private Context context;
    private String[] days;
    private String[] emojis;
    private LineDataSet lineDataSet;
    private LineData lineData;
    LayoutInflater inflater;
    View tagView;
    TextView tagTextView;

    @Override
    public void onAttach(Context context) {
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
        presenter = new GraphPresenter(GraphFragment.this, new ResourceSendUtil(context), DataRepository.getInstance());
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
        ArrayList<String> hashTagItems = new ArrayList<>();
        hashTagItems = list;

        inflater = getLayoutInflater();

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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onViewDetached();
        emojis = null;
        days = null;
        tagTextView = null;
        tagView = null;
    }

    @Override
    public void updateEntries(List<Entry> entries) {
        lineDataSet = new LineDataSet(entries,EMOTION);
        lineDataSet.setLineWidth(2); // 곡률
        lineDataSet.setCircleRadius(5); // 원 색상 지정
        lineDataSet.setCircleColor(resourceSendUtil.getColor(R.color.graphColor));
        lineDataSet.setCircleHoleColor(resourceSendUtil.getColor(R.color.graphColor));
        lineDataSet.setColor(resourceSendUtil.getColor(R.color.graphColor));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);
        lineData = new LineData(lineDataSet);
        binding.lcEmotionGraph.setData(lineData);
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
