package teamh.boostcamp.myapplication.view.statistics;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.jobs.MoveViewJob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.repository.StatisticsRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentStatisticsBinding;
import teamh.boostcamp.myapplication.utils.EventBus;

public class StatisticsFragment extends Fragment implements StatisticsView {

    private static String TAG = StatisticsFragment.class.getSimpleName();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd(E)", Locale.KOREA);
    private Context context;
    private FragmentStatisticsBinding binding;
    private String[] emojis;
    private StatisticsPresenter statisticsPresenter;
    private CompositeDisposable compositeDisposable;

    public StatisticsFragment() {

    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
        emojis = null;
        TAG = null;
        compositeDisposable.dispose();
        // 싱글톤 객체로 화면이 꺼져도 참조를 풀지 않아서 명시적으로 참조 해제를 해줘야 한다.
        MoveViewJob.getInstance(null, 0, 0, null, null);
        statisticsPresenter.viewDestroyed();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false);

        initPresenter();
        setEmojiData();

        compositeDisposable.add(EventBus.get().filter(event -> event.equals(Event.CLEAR_COMPLETE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    // 데이터 초기화
                    updateStatisticsData(new ArrayList<>());
                    updateTagListData(new ArrayList<>());
                }, Throwable::printStackTrace));


        return binding.getRoot();
    }

    private void initPresenter() {
        statisticsPresenter = new StatisticsPresenter(StatisticsFragment.this,
                StatisticsRepositoryImpl.getInstance(AppDatabase.getInstance(
                        context.getApplicationContext()).diaryDao()));
        binding.setPresenter(statisticsPresenter);
    }

    private void setEmojiData() {
        emojis = new String[5];
        for (int i = 0; i < 5; i++) {
            emojis[i] = Emotion.fromValue(i).getEmoji();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statisticsPresenter.loadStatisticsData();
        statisticsPresenter.loadTagList();
    }

    @Override
    public void updateStatisticsData(@NonNull List<Pair<EmotionHistory, EmotionHistory>> emotionHistoryList) {
        final int size = emotionHistoryList.size();
        //  Bar
        List<BarEntry> selectedEmotionListBar = new ArrayList<>();
        List<BarEntry> analyzedEmotionListBar = new ArrayList<>();

        String[] dates = new String[size];

        for (int i = 0; i < size; i++) {
            // 페어로 처리.
            Pair<EmotionHistory, EmotionHistory> p = emotionHistoryList.get(i);
            Date date = emotionHistoryList.get(i).first.getDate();

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
        selectedDataSetBar.setHighlightEnabled(false);

        BarDataSet analyzedDataSetBar = new BarDataSet(analyzedEmotionListBar, null);
        analyzedDataSetBar.setColor(getResources().getColor(R.color.main));
        analyzedDataSetBar.setHighlightEnabled(false);

        setBarData(dates, selectedDataSetBar, analyzedDataSetBar);

    }

    private void setBarData(String[] dates, BarDataSet selectedDataSetBar, BarDataSet analyzedDataSetBar) {
        final XAxis xAxis;
        final YAxis yLeftAxis, yRightAxis;
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        final Legend legend = binding.lcEmotionGraph.getLegend();

        xAxis = binding.lcEmotionGraph.getXAxis();
        yLeftAxis = binding.lcEmotionGraph.getAxisLeft();
        yRightAxis = binding.lcEmotionGraph.getAxisRight();

        // x축 설정
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(dates.length);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(dates.length);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(5f);
        xAxis.setGranularity(1f); // 해결책.. 왜인지는 아직 파악하지 못함.

        // y축 왼쪽 설정
        yLeftAxis.setAxisMinimum(0f);
        yLeftAxis.setValueFormatter(new IndexAxisValueFormatter(emojis));
        yLeftAxis.setTextSize(20f);
        yLeftAxis.setAxisMaximum(4.0f); // max 값
        yLeftAxis.setAxisMinimum(0.0f); // min 값
        yLeftAxis.setGranularityEnabled(true);
        yLeftAxis.setDrawGridLines(true);
        yLeftAxis.enableGridDashedLine(14, 16, 0);

        // y축의 오른쪽은 비활성화
        yRightAxis.setDrawLabels(false);
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setDrawGridLines(false);

        // legend
        legend.setEnabled(false);

        // BarData 세팅
        BarData barData = new BarData(selectedDataSetBar, analyzedDataSetBar);
        barData.setBarWidth(barWidth); // set the width of each bar
        barData.setDrawValues(false);
        binding.lcEmotionGraph.setVisibleXRangeMaximum(5);
        binding.lcEmotionGraph.setScaleEnabled(false);
        binding.lcEmotionGraph.setHorizontalScrollBarEnabled(false);
        binding.lcEmotionGraph.setPinchZoom(false);
        //binding.lcEmotionGraph.moveViewToX(0);
        binding.lcEmotionGraph.setDescription(null);
        binding.lcEmotionGraph.setData(barData);
        binding.lcEmotionGraph.groupBars(0, groupSpace, barSpace);
        binding.lcEmotionGraph.animateY(1000);
        binding.lcEmotionGraph.invalidate(); // refresh

    }

    @Override
    public void updateTagListData(@NonNull List<CountedTag> countedTagList) {
        binding.hashTagCustomLayout.removeAllViews();
        final List<CountedTag> hashTagItems = countedTagList;

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < hashTagItems.size(); i++) {
            final View tagView = inflater.inflate(R.layout.layout_graph_hash_tag, null, false);
            final TextView tagTextView = tagView.findViewById(R.id.tv_hash_tag);
            final int tagCount = hashTagItems.get(i).getCount();


            switch (tagCount) {
                case 1:
                    tagTextView.setTextColor(context.getResources().getColor(R.color.hashtag_light));
                    break;
                case 2:
                    tagTextView.setTextColor(context.getResources().getColor(R.color.hashtag));
                    break;
                case 3:
                    tagTextView.setTextColor(context.getResources().getColor(R.color.hashtag_dark));
                    break;
                default:
                    tagTextView.setTextColor(context.getResources().getColor(R.color.hashtag_dark));
                    break;
            }
            tagTextView.setText(hashTagItems.get(i).getTagName());
            binding.hashTagCustomLayout.addView(tagView);
        }
    }

    @Override
    public void checkLoadStatisticsDataSuccessMessage() {
        //Log.v(TAG, "Graph Data Load Success");
    }

    @Override
    public void checkLoadStatisticsDataFailMessage() {
        /*Log.v(TAG, "Graph Data Load Fail");*/
    }

    @Override
    public void checkLoadTagListSuccessMessage() {
       /* Log.v(TAG, "Tag Data Load Success");*/
    }

    @Override
    public void checkLoadTagListFailMessage() {
        /*Log.v(TAG, "Graph Data Load Fail");*/
    }
}
