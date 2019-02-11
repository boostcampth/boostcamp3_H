package teamh.boostcamp.myapplication.view.graph;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public class StatisticsFragment extends Fragment implements StatisticsView {

    private StatisticsPresenter statisticsPresenter;

    public StatisticsFragment(){

    }

    public static StatisticsFragment newInstance(){
        return new StatisticsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void updateStatisticsData(@NonNull List<EmotionHistory> emotionHistoryList) {

    }

    @Override
    public void showLoadStatisticsDataSuccessMessage() {

    }

    @Override
    public void showLoadStatisticsDataFailMessage() {

    }
}
