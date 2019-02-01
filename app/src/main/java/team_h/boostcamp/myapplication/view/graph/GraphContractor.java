package team_h.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface GraphContractor {

    interface View extends BaseView {

        void updateHashTagWord(ArrayList<String> list);

        void updateEntries(List<Entry> entries);
    }

    interface Presenter extends BasePresenter {

        void loadHashTagWord(int size);
    }
}
