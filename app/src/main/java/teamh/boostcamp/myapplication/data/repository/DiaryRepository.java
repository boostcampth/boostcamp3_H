package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface DiaryRepository {

    @NonNull
    Single<List<Diary>> loadMoreDiaries(final int idx);

    @NonNull
    Completable insertDiaries(@NonNull Diary ...diaries);

    @NonNull
    Completable truncateDiaries();
}
