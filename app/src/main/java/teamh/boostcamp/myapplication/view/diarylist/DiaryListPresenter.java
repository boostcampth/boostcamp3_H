package teamh.boostcamp.myapplication.view.diarylist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.mapper.DiaryMapper;
import teamh.boostcamp.myapplication.view.diarylist.kakaoLink.KakaoLinkHelper;
import teamh.boostcamp.myapplication.view.play.RecordPlayer;

class DiaryListPresenter {

    private static final int NOTHING_PLAYED = -1;

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
    @NonNull
    private SharedPreferenceManager sharedPreferenceManager;
    private KakaoLinkHelper kakaoLinkHelper;

    @Nullable
    private Emotion selectedEmotion;
    private boolean isLoading;
    private boolean isRecording;
    private int lastPlayedPosition;

    DiaryListPresenter(@NonNull DiaryListView diaryListView,
                       @NonNull DiaryRepository diaryRepository,
                       @NonNull DiaryRecorder diaryRecorder,
                       @NonNull RecordPlayer recordPlayer,
                       @NonNull SharedPreferenceManager sharedPreferenceManager,
                       @NonNull KakaoLinkHelper kakaoLinkHelper) {
        this.diaryListView = diaryListView;
        this.diaryRepository = diaryRepository;
        this.diaryRecorder = diaryRecorder;
        this.recordPlayer = recordPlayer;
        this.sharedPreferenceManager = sharedPreferenceManager;

        this.compositeDisposable = new CompositeDisposable();
        this.selectedEmotion = null;

        this.isLoading = false;
        this.isRecording = false;
        this.lastPlayedPosition = NOTHING_PLAYED;
        this.lastItemLoadedTime = new Date();
        this.kakaoLinkHelper = kakaoLinkHelper;

        initMediaListener();
    }

    void loadDiaryList(final int pageSize) {
        // FIXME : 저장 후 로딩하고 있는 경우 예외 처리 필수
        if (!isLoading) {
            isLoading = true;

            compositeDisposable.add(diaryRepository.loadDiaryList(lastItemLoadedTime, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaries -> {
                                isLoading = false;
                                if (diaries.size() == 0) {
                                    return;
                                }
                                lastItemLoadedTime = diaries.get(diaries.size() - 1).getRecordDate();
                                diaryListView.addDiaryList(diaries);
                            }, throwable -> {
                                isLoading = false;
                                diaryListView.showLoadDiaryListFailMsg();
                            }
                    ));
        }
    }

    void saveDiary(@NonNull final List<String> tags, final boolean isNetworkAvailable) {

        if (isRecording) {
            diaryListView.showRecordNotFinished();
            return;
        }

        if (selectedEmotion == null) {
            diaryListView.showEmotionNotSelected();
            return;
        }

        final File file = new File(diaryRecorder.getFilePath());

        if (file != null && !file.exists()) {
            diaryListView.showRecordFileNotFound();
            return;
        }

        diaryListView.setIsSaving(true);

        // FIXME : DiaryEntity 매개변수 수정하기, 네트워크가 없는 상황에서 나중에 분석할 데이터 지정
        if (isNetworkAvailable) {

            final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest(file.getAbsolutePath());

            final Date saveTime = new Date();
            final String newItemId = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(saveTime);

            compositeDisposable.add(diaryRepository.requestEmotionAnalyze(request).
                    map(emotion -> new DiaryEntity(newItemId,
                            saveTime,
                            file.getAbsolutePath(),
                            tags,
                            selectedEmotion,
                            emotion))
                    .flatMapCompletable(diaryRepository::insertDiary)
                    .andThen(diaryRepository.loadRecentInsertedDiary())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaryEntity -> {
                        setLastItemSavedTime(saveTime);
                        diaryListView.setRecordCardVisibilityGone();
                        diaryListView.showAnalyzedEmotion(diaryEntity.getAnalyzedEmotion());
                        diaryListView.insertDiaryList(DiaryMapper.toDiary(diaryEntity));
                        diaryListView.setIsSaving(false);
                    }, Throwable::printStackTrace));
        } else {
            // TODO : 기능 구현
        }
    }

    void playDiaryRecord(@NonNull List<Diary> diaries, final int currentPlayPosition) {
        // 재생중
        if (lastPlayedPosition != NOTHING_PLAYED) {
            recordPlayer.stopList();
            diaryListView.onPlayFileChanged(lastPlayedPosition, true);
            if (lastPlayedPosition == currentPlayPosition) {
                lastPlayedPosition = NOTHING_PLAYED;
            } else {
                recordPlayer.setList(diaries);
                recordPlayer.playList();
                lastPlayedPosition = currentPlayPosition;
                diaryListView.onPlayFileChanged(lastPlayedPosition, false);
            }
        } else {
            lastPlayedPosition = currentPlayPosition;
            diaryListView.onPlayFileChanged(lastPlayedPosition, false);
            recordPlayer.setList(diaries);
            recordPlayer.playList();
        }
    }

    void setSelectedEmotion(@Nullable Emotion emotion) {
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

    private void setLastItemSavedTime(@NonNull Date savedTime) {
        sharedPreferenceManager.setLastDiarySaveTime(savedTime);
    }

    private void initMediaListener() {
        recordPlayer.setOnCompletionListener(mediaPlayer -> {
            diaryListView.onPlayFileChanged(lastPlayedPosition, true);
            lastPlayedPosition = NOTHING_PLAYED;
        });
    }

    void onViewCreated() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(new Date());

        if (sharedPreferenceManager.getLastDiarySaveTime().equals(today)) {
            diaryListView.setRecordCardVisibilityGone();
        }
    }

    void onViewDestroyed() {
        compositeDisposable.clear();
        if (isRecording) {
            diaryRecorder.finishRecord();
        }
        diaryRecorder.releaseRecorder();
    }

    void sendDiaryToKakao(Diary diary){
        kakaoLinkHelper.sendDiary(diary);
    }
}
