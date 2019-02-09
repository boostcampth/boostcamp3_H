package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;

public class DataLocalSource implements DataSource {
    /**
     * TODO : Room DB로부터 원하는 데이터를 가지고 와서 처리할 로직 추가 예정.
     */
    @Override
    public void getHashTag(int size, LoadDataCallback loadDataCallback) {
        ArrayList<String> list = new ArrayList<>();
        String tag;
        for (int i = 0; i < size; i++) {
            tag = "#tag" + i;
            list.add(tag);
        }
        if (loadDataCallback != null) {
            loadDataCallback.onDataLoaded(list);
        }
    }
}
