package team_h.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface GraphContractor {

    interface View extends BaseView {

        void setLineData(LineData lineData);

        XAxis getXAxis();

        YAxis getYLeftAxis();

        YAxis getYRightAxis();

        void loadHastTagWord(String[] hashTags);
    }

    interface Presenter extends BasePresenter {

    }
}
