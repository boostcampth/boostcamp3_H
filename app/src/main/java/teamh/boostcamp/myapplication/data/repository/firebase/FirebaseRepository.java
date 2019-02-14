package teamh.boostcamp.myapplication.data.repository.firebase;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface FirebaseRepository {

    @NonNull
    Maybe<List<String>> loadAllDiaryId();

    @NonNull
    Maybe<List<DiaryEntity>> loadAllDiaryList();

    @NonNull
    Completable insertDiaries(@NonNull List<DiaryEntity> diaryEntities);
}
