package teamh.boostcamp.myapplication.data.repository.firebase;

import android.net.Uri;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public interface FirebaseRepository {

    @NonNull
    Maybe<List<String>> loadAllDiaryId();

    @NonNull
    Maybe<List<DiaryEntity>> loadAllDiaryList();

    @NonNull
    Completable insertDiaries(@NonNull List<DiaryEntity> diaryEntities);

    @NonNull
    Single<List<DiaryEntity>> uploadRecordFile(@NonNull List<DiaryEntity> diaryEntityList);

    @NonNull
    Single<List<DiaryEntity>> downloadRecordFile(@NonNull List<DiaryEntity> diaryEntityList);
}
