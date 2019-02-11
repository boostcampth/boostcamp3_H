package teamh.boostcamp.myapplication.view.graph;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.databinding.ObservableField;
import teamh.boostcamp.myapplication.data.repository.DataRepository;

public class LegacyGraphPresenter implements GraphContractor.Presenter {
    public static final ObservableField<String> OBSERVER = new ObservableField<>("Statics");
    public static final ObservableField<String> HASHTAG_OBSERVER = new ObservableField<>("#HashTags");
    private List<Entry> thisWeekEntries = new ArrayList<>();
    private List<Entry> lastWeekEntries = new ArrayList<>();
    private GraphContractor.View view;
    private DataRepository dataRepository;

    LegacyGraphPresenter(GraphContractor.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void onViewAttached() {
        initEntry();
        view.updateThisWeekEntries(thisWeekEntries);
        view.updateLastWeekEntries(lastWeekEntries);
    }

    private void initEntry() {
        // 이번주 감정 데이터
        thisWeekEntries.add(new Entry(2, 1.0f));
        thisWeekEntries.add(new Entry(1, 2.0f));
        //thisWeekEntries.add(new Entry(2, 3.0f));
        thisWeekEntries.add(new Entry(3, 2.0f));
        thisWeekEntries.add(new Entry(4, 2.0f));
        thisWeekEntries.add(new Entry(5, 4.0f));
        thisWeekEntries.add(new Entry(6, 0.0f));
        // 지난주 감정 데이터
        lastWeekEntries.add(new Entry(0,0.0f));
        lastWeekEntries.add(new Entry(1,1.0f));
        lastWeekEntries.add(new Entry(2,3.0f));
        lastWeekEntries.add(new Entry(3,4.0f));
        lastWeekEntries.add(new Entry(4,2.0f));
        lastWeekEntries.add(new Entry(5,1.0f));
        lastWeekEntries.add(new Entry(6,2.0f));
    }

    @Override
    public void onViewDetached() {
        // 리소스 해제
        dataRepository = null;
        view = null;
        thisWeekEntries = null;
        lastWeekEntries = null;
    }

    @Override
    public void loadHashTagWord(int size) {
        dataRepository.getHashTag(size, list -> {
            if (list != null) {
                view.updateHashTagWord(list);
            }
        });
    }
}
