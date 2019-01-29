package team_h.boostcamp.myapplication.view.memories;

import android.content.Context;

public class MemoriesPresenter implements MemoriesContractor.Presenter {

    private MemoriesContractor.View view;
    private Context mContext;

    public MemoriesPresenter(MemoriesContractor.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {

    }
}
