package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
<<<<<<< HEAD
import io.reactivex.Observable;
=======
import io.reactivex.Maybe;
>>>>>>> issue-186/firebase-인터페이스구현
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;

public interface DiaryRepository {

    @NonNull
    Single<List<Diary>> loadDiaryList(@NonNull final Date startAfter,
                                      final int pageSize);
    @NonNull
    Completable insertDiary(@NonNull final DiaryEntity ...diaryEntities);

    @NonNull
    Single<Emotion> requestEmotionAnalyze(@NonNull final EmotionAnalyzeRequest request);

    @NonNull
    Single<DiaryEntity> loadRecentInsertedDiary();

    @NonNull
<<<<<<< HEAD
    Completable deleteDiary(@NonNull String id);

    @NonNull
    Observable<Diary> loadAll();
=======
    Maybe<List<DiaryEntity>> loadNotBackupDiaryList(@NonNull List<String> diaryIdList);
>>>>>>> issue-186/firebase-인터페이스구현
}
