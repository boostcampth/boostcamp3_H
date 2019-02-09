package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;

/* Diary Repository 구현 */
public interface DiaryRepository {

    /* 일기 목록을 n 개 가져오기
     * 일기의 인덱스 번호를 통해 load 시작점을 설정 */
    @NonNull
    Single<List<Diary>> loadMoreDiaries(final int idx);

    /* 일기 집어넣기
     * Completable Issue 때문에 Completable.fromAction 으로 처리 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiaries(Diary... diaries);

    /* 초기화 작업
     * Completable Issue 떄문에 Completable.fromAction 으로 처리 */
    @NonNull
    void truncateDiaries();

    /* 음성 분석 결과 가져오기
     * 분석된 List<String> 결과를 Mapper 를  통해 Integer 변경*/
    @NonNull
    Single<Integer> anlayzeVoiceEmotion(@NonNull EmotionAnalyzeRequest emotionAnalyzeRequest);
}
