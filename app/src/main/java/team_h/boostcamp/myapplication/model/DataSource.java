package team_h.boostcamp.myapplication.model;

import java.util.ArrayList;

public interface DataSource {

    interface LoadDataCallback {
        void onDataLoaded(ArrayList<String> list);
    }

    void getHashTag(int size, LoadDataCallback loadDataCallback);
}
