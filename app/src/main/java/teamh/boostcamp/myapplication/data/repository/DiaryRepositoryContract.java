package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;

public interface DiaryRepositoryContract {

    @NonNull
    Single<List<LegacyDiary>> loadMoreDiaryItems(final long idx);

    @NonNull
    Single<Integer> analyzeVoiceEmotion(@NonNull EmotionAnalyzeRequest request);

    @NonNull
    Completable insertRecordItem(@NonNull LegacyDiary diaryItem);

    @NonNull
    Completable insertRecordItems(@NonNull LegacyDiary... diaries);

    @NonNull
    Completable deleteRecordItem(@NonNull LegacyDiary diary);

    @NonNull
    Completable clearAllData();
}
