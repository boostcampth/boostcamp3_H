package teamh.boostcamp.myapplication.view.statistics;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.repository.StatisticsRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentStatisticsBinding;
import teamh.boostcamp.myapplication.view.diarylist.DiaryRxEventBus;

public class StatisticsFragment extends Fragment implements StatisticsView {

    private static final String TAG = StatisticsFragment.class.getSimpleName();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd(E)", Locale.KOREA);
    private Context context;
    private FragmentStatisticsBinding binding;
    private String[] emojis;
    private StatisticsPresenter statisticsPresenter;

    public StatisticsFragment() {

    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        statisticsPresenter.viewDestroyed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false);
        initPresenter();
        initData();
        return binding.getRoot();
    }

    private void initPresenter() {
        statisticsPresenter = new StatisticsPresenter(StatisticsFragment.this,
                StatisticsRepositoryImpl.getInstance(AppDatabase.getInstance(
                        context.getApplicationContext()).diaryDao()));
        binding.setPresenter(statisticsPresenter);
    }

    private void initData() {
        emojis = context.getResources().getStringArray(R.array.graph_emojis);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statisticsPresenter.loadStatisticsData();
        statisticsPresenter.loadTagList();
    }

    @Override
    public void updateStatisticsData(@NonNull List<Pair<EmotionHistory, EmotionHistory>> emotionHistoryList) {
        int size = emotionHistoryList.size();

        //  Bar
        List<BarEntry> selectedEmotionListBar = new ArrayList<>();
        List<BarEntry> analyzedEmotionListBar = new ArrayList<>();

        String[] dates = new String[size];
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < size; i++) {
            // 페어로 처리.
            Pair<EmotionHistory, EmotionHistory> p = emotionHistoryList.get(i);
            Date date = emotionHistoryList.get(i).first.getDate();
            calendar.setTime(date);

            String monthAndDate = simpleDateFormat.format(date);
            dates[i] = monthAndDate;
            int selectedEmotion = p.first.getEmotion().getEmotion(); // 선택
            int analyzedEmotion = p.second.getEmotion().getEmotion(); // 분석

            // Bar
            selectedEmotionListBar.add(new BarEntry(i, selectedEmotion));
            analyzedEmotionListBar.add(new BarEntry(i, analyzedEmotion));
        }


        BarDataSet selectedDataSetBar = new BarDataSet(selectedEmotionListBar, null);
        selectedDataSetBar.setColor(getResources().getColor(R.color.main_dark));

        BarDataSet analyzedDataSetBar = new BarDataSet(analyzedEmotionListBar, null);
        analyzedDataSetBar.setColor(getResources().getColor(R.color.main));


        final XAxis xAxis;
        final YAxis yLeftAxis, yRightAxis;
        float groupSpace = 0.26f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.35f; // x2 dataset
        Legend legend = binding.lcEmotionGraph.getLegend();
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"


        xAxis = binding.lcEmotionGraph.getXAxis();
        yLeftAxis = binding.lcEmotionGraph.getAxisLeft();
        yRightAxis = binding.lcEmotionGraph.getAxisRight();

        // x축 설정
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(dates.length - 1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(dates.length - 1, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new GraphAxisValueFormatter(dates));
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(5f);

        // y축 왼쪽 설정
        yLeftAxis.setAxisMinimum(0f);
        yLeftAxis.setValueFormatter(new GraphAxisValueFormatter(emojis));
        yLeftAxis.setTextSize(20f);
        // max 값
        yLeftAxis.setAxisMaximum(4.0f);
        // min 값
        yLeftAxis.setAxisMinimum(0.0f);
        yLeftAxis.setGranularityEnabled(true);
        yLeftAxis.setDrawGridLines(true);
        yLeftAxis.enableGridDashedLine(14, 16, 0);

        // y축의 오른쪽은 비활성화
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawGridLines(false);

        // legend
        legend.setEnabled(false);

        BarData data = new BarData(selectedDataSetBar, analyzedDataSetBar);
        data.setBarWidth(barWidth); // set the width of each bar
        data.setDrawValues(false);
        binding.lcEmotionGraph.setVisibleXRangeMaximum(5);
        binding.lcEmotionGraph.setHorizontalScrollBarEnabled(true);
        binding.lcEmotionGraph.moveViewToX(0);
        binding.lcEmotionGraph.setDescription(null);
        binding.lcEmotionGraph.setData(data);
        binding.lcEmotionGraph.groupBars(0, groupSpace, barSpace);
        binding.lcEmotionGraph.animateY(1000);
        binding.lcEmotionGraph.invalidate(); // refresh
    }

    @Override
    public void updateTagListData(@NonNull List<CountedTag> countedTagList) {
        final List<CountedTag> hashTagItems = countedTagList;

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < hashTagItems.size(); i++) {
            final View tagView = inflater.inflate(R.layout.layout_graph_hash_tag, null, false);
            final TextView tagTextView = tagView.findViewById(R.id.tv_hash_tag);

            tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hashTagItems.get(i).getCount() * 10);
            Log.v("tag count : ", hashTagItems.get(i).getTagName() + ", " + "갯수 : " + hashTagItems.get(i).getCount());
            if (hashTagItems.get(i).getCount() > 3) {
                tagTextView.setTextColor(context.getResources().getColor(R.color.main_dark));
            }

            tagTextView.setText(hashTagItems.get(i).getTagName());
            binding.hashTagCustomLayout.addView(tagView);
        }
    }

    @Override
    public void checkLoadStatisticsDataSuccessMessage() {
        Log.v(TAG, "Graph Data Load Success");
    }

    @Override
    public void checkLoadStatisticsDataFailMessage() {
        Log.v(TAG, "Graph Data Load Fail");
    }

    @Override
    public void checkLoadTagListSuccessMessage() {
        Log.v(TAG, "Tag Data Load Success");
    }

    @Override
    public void checkLoadTagListFailMessage() {
        Log.v(TAG, "Graph Data Load Fail");
    }
}
