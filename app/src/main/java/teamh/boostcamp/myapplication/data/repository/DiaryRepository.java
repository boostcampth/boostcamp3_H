package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface DiaryRepository {

    @NonNull
    Single<List<Diary>> loadDiaryList(@NonNull final Date lastItemSavedTime,
                                      final int pageSize);
}
