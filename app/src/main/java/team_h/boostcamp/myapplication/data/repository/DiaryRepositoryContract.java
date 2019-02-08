package team_h.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import team_h.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import team_h.boostcamp.myapplication.model.Diary;

public interface DiaryRepositoryContract {

    @NonNull
    Single<List<Diary>> loadMoreDiaryItems(final long idx);

    @NonNull
    Single<Integer> analyzeVoiceEmotion(@NonNull EmotionAnalyzeRequest request);

    @NonNull
    Completable insertRecordItem(@NonNull Diary diaryItem);

    @NonNull
    Completable deleteRecordItem(@NonNull Diary diary);
}
