package teamh.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import teamh.boostcamp.myapplication.view.BasePresenter;

public interface GraphContractor {

    interface View {

        void updateHashTagWord(ArrayList<String> list);

        void updateThisWeekEntries(List<Entry> thisWeekEntries);

        void updateLastWeekEntries(List<Entry> lastWeekEntries);

    }

    interface Presenter extends BasePresenter {

        void loadHashTagWord(int size);
    }
}
