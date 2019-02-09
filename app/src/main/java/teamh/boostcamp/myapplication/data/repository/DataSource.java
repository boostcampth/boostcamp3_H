package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;

public interface DataSource {

    interface LoadDataCallback {
        void onDataLoaded(ArrayList<String> list);
    }

    void getHashTag(int size, LoadDataCallback loadDataCallback);
}
