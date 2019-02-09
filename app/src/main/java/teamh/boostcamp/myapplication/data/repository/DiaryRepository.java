package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface DiaryRepository {

    @NonNull
    Single<List<Diary>> loadDiaryList(final int lastItemIndex, final int pageSize);
}
