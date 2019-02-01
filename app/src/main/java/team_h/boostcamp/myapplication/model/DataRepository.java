package team_h.boostcamp.myapplication.model;

import team_h.boostcamp.myapplication.model.source.local.DataLocalSource;

public class DataRepository implements DataSource {

    private static DataRepository dataRepository;
    private DataLocalSource dataLocalSource;

    private DataRepository() {
        dataLocalSource = new DataLocalSource();
    }

    public static DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    @Override
    public void getHashTag(int size, LoadDataCallback loadDataCallback) {
        // 람다식
        dataLocalSource.getHashTag(size, list -> {
            if (list != null) {
                loadDataCallback.onDataLoaded(list);
            }
        });

    }
}
