package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public interface StaticsRepository {

    @NonNull
    Single<List<EmotionHistory>> loadEmotionHistoryList();

    @NonNull
    Single<List<CountedTag>> loadCountedTagList();

}
