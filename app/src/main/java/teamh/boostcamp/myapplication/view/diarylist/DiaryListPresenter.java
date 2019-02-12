package teamh.boostcamp.myapplication.view.diarylist;

import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.view.play.RecordPlayer;

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
    @NonNull
    private RecordPlayer recordPlayer;
    @Nullable
    private Emotion selectedEmotion;
    private boolean isLoading;
    private boolean isRecording;
    private boolean isRecordPlaying;

    DiaryListPresenter(@NonNull DiaryListView diaryListView,
                       @NonNull DiaryRepository diaryRepository,
                       @NonNull DiaryRecorder diaryRecorder,
                       @NonNull RecordPlayer recordPlayer) {
        this.diaryListView = diaryListView;
        this.diaryRepository = diaryRepository;
        this.diaryRecorder = diaryRecorder;
        this.recordPlayer = recordPlayer;
        this.compositeDisposable = new CompositeDisposable();
        this.selectedEmotion = null;

        this.isLoading = false;
        this.isRecording = false;
        this.isRecordPlaying = false;

        this.lastItemLoadedTime = new Date();

        this.diaryRecorder.setMediaRecorderTimeOutListener(() -> {
            diaryRecorder.finishRecord();
            diaryListView.showRecordTimeOutMsg();
        });
    }

    void loadDiaryList(final int pageSize) {
        // FIXME : 저장 후 로딩하고 있는 경우 예외 처리 필수
        if (!isLoading) {
            isLoading = true;

            Date tempTime = pageSize == 1 ? new Date() : lastItemLoadedTime;

            compositeDisposable.add(diaryRepository.loadDiaryList(tempTime, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaries -> {
                                isLoading = false;
                                if (diaries.size() == 0) {
                                    return;
                                }
                                if (pageSize == 3) {
                                    lastItemLoadedTime = diaries.get(diaries.size() - 1).getRecordDate();
                                    diaryListView.addDiaryList(diaries);
                                } else {
                                    diaryListView.insertDiaryList(diaries.get(0));
                                }
                            }
                            , throwable -> {
                                isLoading = false;
                                diaryListView.showLoadDiaryListFailMsg();
                            }
                    ));
        }
    }

    void saveDiary(@NonNull final String tags, final boolean isNetworkAvailable) {

        if (isRecording) {
            diaryListView.showRecordNotFinished();
            return;
        }

        if (selectedEmotion == null) {
            diaryListView.showEmotionNotSelected();
            return;
        }

        File file = new File(diaryRecorder.getFilePath());

        if (!file.exists()) {
            diaryListView.showRecordFileNotFound();
            return;
        }

        diaryListView.setIsSaving(true);

        // FIXME : DiaryEntity 매개변수 수정하기, 네트워크가 없는 상황에서 나중에 분석할 데이터 지정
        if (isNetworkAvailable) {

            final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest(file.getAbsolutePath());

            compositeDisposable.add(diaryRepository.requestEmotionAnalyze(request).
                    map(emotion -> new DiaryEntity(0,
                            new Date(),
                            file.getAbsolutePath(),
                            Arrays.asList(tags.split("#")),
                            selectedEmotion,
                            emotion))
                    .flatMapCompletable(diaryRepository::insertDiary)
                    .subscribe(() -> {
                                diaryListView.notifyTodayDiarySaved();
                                diaryListView.setIsSaving(false);
                            }
                            , throwable -> {
                                diaryListView.showSaveDiaryFail();
                                diaryListView.setIsSaving(false);
                            }
                    ));
        } else {
            // TODO : 기능 구현
        }
    }

    public void playDiaryRecord(List<Diary> diaries) {
        if(isRecordPlaying) {
            recordPlayer.stopList();
        }

        recordPlayer.setList(diaries);
        recordPlayer.playList();
    }

    public void setSelectedEmotion(@Nullable Emotion emotion) {
        this.selectedEmotion = emotion;
    }

    void setIsRecording(final boolean isRecording) {
        this.isRecording = isRecording;
    }

    void startRecording() {
        diaryRecorder.startRecord();
    }

    void finishRecording() {
        diaryRecorder.finishRecord();
    }

    void onViewDestroyed() {
        compositeDisposable.clear();
        if (isRecording) {
            diaryRecorder.finishRecord();
        }
        diaryRecorder.releaseRecorder();
    }
}
