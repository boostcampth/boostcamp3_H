package teamh.boostcamp.myapplication.data.repository.backup;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public interface BackUpRepository {

    @NonNull
    Single<List<String>> loadAllDiaryId();

    @NonNull
    Single<List<DiaryEntity>> loadAllDiaryList();

    @NonNull
    Completable insertDiary(@NonNull DiaryEntity diaryEntities);

    @NonNull
    Observable<DiaryEntity> downloadSingleRecordFile(@NonNull DiaryEntity diaryEntityList);

    @NonNull
    Observable<DiaryEntity> uploadSingleRecordFile(@NonNull DiaryEntity diaryEntity);

    @NonNull
    Single<DiaryEntity> loadDiaryById(String id);
}
