package team_h.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.data.remote.deepaffects.AnalyzedEmotionMapper;
import team_h.boostcamp.myapplication.data.remote.deepaffects.DeepAffectApiClient;
import team_h.boostcamp.myapplication.data.remote.deepaffects.request.EmotionAnalyzeRequest;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.source.local.DiaryDao;


/*
 * Repository 를 data/repository 로 하는 것도  */
public class DiaryRepository implements DiaryRepositoryContract {

    private static DiaryRepository INSTANCE;
    private DeepAffectApiClient deepAffectApiClient;
    private DiaryDao diaryDao;

    private DiaryRepository(@NonNull DeepAffectApiClient deepAffectApiClient,
                            @NonNull DiaryDao diaryDao) {
        this.deepAffectApiClient = deepAffectApiClient;
        this.diaryDao = diaryDao;
    }

    public static DiaryRepository getInstance(@NonNull DeepAffectApiClient deepAffectApiClient,
                                              @NonNull DiaryDao diaryDao) {
        if (INSTANCE == null) {
            synchronized (DiaryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiaryRepository(deepAffectApiClient, diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<Diary>> loadMoreDiaryItems() {
        return diaryDao.loadMoreDiary().subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteRecordItem(@NonNull Diary diary) {
        return diaryDao.deleteDiary(diary).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable insertRecordItem(@NonNull Diary diaryItem) {
        return diaryDao.insertDiary(diaryItem).subscribeOn(Schedulers.io());
    }

    /* 사실 이 작업을 Repository 에서 해야하는지 잘 모르겠습니다.
    *  Repository 에서는 데이터 저장 삭제 불러오기 등.. 과 관련한 연산만 수행해야한다고 생각하는데
    *  감정 분석까지 Repository 에서 수행하는게 맞는건지.. */
    @NonNull
    @Override
    public Single<Integer> analyzeVoiceEmotion(@NonNull EmotionAnalyzeRequest request) {
        // repository 에서 map 을 통해 데이터 변경
        return deepAffectApiClient.analyzeVoiceEmotion(request)
                .map(AnalyzedEmotionMapper::parseAnalyzedEmotion)
                .subscribeOn(Schedulers.io());
    }
}
