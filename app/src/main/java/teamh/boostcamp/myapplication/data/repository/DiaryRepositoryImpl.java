package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

public class DiaryRepositoryImpl implements DiaryRepository {

    private static DiaryRepositoryImpl INSTANCE;

    private DiaryRepositoryImpl() {

    }


    @NonNull
    @Override
    public Single<List<Diary>> loadDiaryList(@NonNull Date lastItemSavedTime, int pageSize) {
        return null;
    }
}
