package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
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
    Completable insertDiary(@NonNull final DiaryEntity diaryEntity);

    @NonNull
    Single<Emotion> requestEmotionAnalyze(@NonNull final EmotionAnalyzeRequest request);

    @NonNull
    Single<DiaryEntity> loadRecentInsertedDiary();
}
