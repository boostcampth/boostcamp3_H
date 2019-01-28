package team_h.boostcamp.myapplication.view.graph;

import android.databinding.ObservableField;

public class GraphPresenter implements GraphContractor.Presenter {
    private GraphContractor.View view;
    public static final ObservableField<String> observer = new ObservableField<>("Statics");

    GraphPresenter(GraphContractor.View view){
        this.view = view;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {

    }
}
