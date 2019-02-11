package teamh.boostcamp.myapplication.view.diarylist;

import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;

public class DiaryListPresenter {
    @NonNull
    final private DiaryRepository diaryRepository;
    @NonNull
    final private CompositeDisposable compositeDisposable;
    @NonNull
    final private DiaryListView diaryListView;
    @NonNull
    private DiaryRecorder diaryRecorder;
    @NonNull
    private Date lastItemLoadedTime;

    private Emotion selectedEmotion;
    private boolean isLoading;
    private boolean isNetworkAvailable;

    DiaryListPresenter(@NonNull DiaryListView diaryListView,
                       @NonNull DiaryRepository diaryRepository,
                       @NonNull DiaryRecorder diaryRecorder,
                       final boolean isNetworkAvailable) {
        this.diaryListView = diaryListView;
        this.diaryRepository = diaryRepository;
        this.diaryRecorder = diaryRecorder;
        this.compositeDisposable = new CompositeDisposable();
        this.selectedEmotion = null;
        this.isLoading = false;
        this.isNetworkAvailable = isNetworkAvailable;
        this.lastItemLoadedTime = new Date();
    }

    void loadDiaryList(final int pageSize) {
        // FIXME : 저장 후 로딩하고 있는 경우 예외 처리 필수
        if (!isLoading) {
            isLoading = true;

            compositeDisposable.add(diaryRepository.loadDiaryList(lastItemLoadedTime, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaries -> {

                                if (diaries.size() != 0) {
                                    lastItemLoadedTime = diaries.get(diaries.size() - 1).getRecordDate();
                                }

                                diaryListView.addDiaryList(diaries);
                                isLoading = false;
                            }
                            , throwable -> {
                                diaryListView.showLoadDiaryListFailMsg();
                                isLoading = false;
                            }
                    ));
        }
    }

    void insertDiary(@NonNull final String tags) {

        // FIXME : DiaryEntity 매개변수 수정하기
        if (isNetworkAvailable) {

            final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest("encodedFile");

            compositeDisposable.add(diaryRepository.requestEmotionAnalyze(request).
                    map(analyzedEmotion -> new DiaryEntity(0,
                            new Date(),
                            "/storage/emulated/0/2019-02-08.acc",
                            Arrays.asList(tags.split("#")),
                            Emotion.fromValue(3),
                            Emotion.fromValue(analyzedEmotion)))
                    .flatMapCompletable(diaryRepository::insertDiary)
                    .subscribe(diaryListView::notifyTodayDiarySaved
                            , throwable -> diaryListView.showSaveDiaryFail()
                    ));
        }
        // FIXME : else 구현하기

    }

    public void setSelectedEmotion(@NonNull Emotion emotion) {
        this.selectedEmotion = emotion;
    }

    public void setIsNetworkAvailable(final boolean isNetworkAvailable) {
        this.isNetworkAvailable = isNetworkAvailable;
    }

    public void startRecord() {
        diaryRecorder.prepareRecord();
        diaryRecorder.startRecord();
    }

    public void stopRecord() {
        diaryRecorder.finishRecord();
    }

    void onViewDestroyed() {
        compositeDisposable.clear();
        diaryRecorder.releaseRecorder();
    }
}
