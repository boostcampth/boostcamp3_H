package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public interface DiaryRepositoryContract {

    @NonNull
    Single<List<LegacyDiary>> loadMoreDiaryItems(final int idx);

    @NonNull
    Single<Integer> analyzeVoiceEmotion(@NonNull EmotionAnalyzeRequest request);

    @NonNull
    Completable insertRecordItem(@NonNull DiaryEntity diaryItem);
/*
    @NonNull
    Completable insertRecordItems(@NonNull DiaryEntity... diaries);

    @NonNull
    Completable deleteRecordItem(@NonNull DiaryEntity diary);

    @NonNull
    Completable clearAllData();*/
}
