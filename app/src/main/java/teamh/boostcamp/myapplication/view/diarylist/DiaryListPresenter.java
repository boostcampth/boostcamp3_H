package teamh.boostcamp.myapplication.view.diarylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;

public class DiaryListPresenter {
    @NonNull
    final private DiaryRepository diaryRepository;
    @NonNull
    final private CompositeDisposable compositeDisposable;
    @NonNull
    final private DiaryListView diaryListView;

    private Emotion selectedEmotion;


    public DiaryListPresenter(@NonNull DiaryListView diaryListView,
                              @NonNull DiaryRepository diaryRepository) {
        this.diaryListView = diaryListView;
        this.diaryRepository = diaryRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void loadDiaryList(@NonNull final Date recordDate,
                       final int pageSize) {

        compositeDisposable.add(diaryRepository.loadDiaryList(recordDate, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diaryListView::addDiaryList
                        , throwable -> diaryListView.showLoadDiaryListFailMsg()
                ));
    }

    void insertDiary(@NonNull final String tags) {

        // FIXME : 인코딩 함수 추가
        final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest("encodedFile");

        compositeDisposable.add(diaryRepository.requestEmotionAnalyze(request).
                map(analyzedEmotion -> new DiaryEntity(0,
                        new Date(),
                        "/storage/emulated/0/2019-02-08.acc",
                        Arrays.asList(tags.split("#")),
                        Emotion.fromValue(3),
                        Emotion.fromValue(analyzedEmotion)))
                .flatMapCompletable(diaryRepository::insertDiary)
                .subscribe(() -> {
                    // TODO : 저장 후 처리
                }, throwable -> {
                    // TODO : 에러 처리
                }));
    }


    void onViewDestroyed() {
        compositeDisposable.clear();
    }
}
