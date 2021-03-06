package teamh.boostcamp.myapplication.view.diarylist;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.mapper.DiaryMapper;
import teamh.boostcamp.myapplication.utils.EventBus;
import teamh.boostcamp.myapplication.utils.KakaoLinkHelper;

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
    private DiaryPlayer recordPlayer;
    @NonNull
    private SharedPreferenceManager sharedPreferenceManager;
    @NonNull
    private KakaoLinkHelper kakaoLinkHelper;
    @Nullable
    private Emotion selectedEmotion;

    private boolean isLoading;
    private boolean isRecording;

    private int lastPlayedPosition;

    DiaryListPresenter(@NonNull DiaryListView diaryListView,
                       @NonNull DiaryRepository diaryRepository,
                       @NonNull DiaryRecorder diaryRecorder,
                       @NonNull DiaryPlayer recordPlayer,
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

        this.recordPlayer.setCompletionListener(mediaPlayer -> {
            diaryListView.onPlayFileChanged(lastPlayedPosition, true);
            lastPlayedPosition = NOTHING_PLAYED;
        });
    }

    @SuppressWarnings("SameParameterValue")
    void loadDiaryList(final int pageSize, final boolean clear) {
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
                                diaryListView.addDiaryList(diaries, clear);
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

        if(diaryRecorder.getFilePath() == null) {
            diaryListView.showRecordNotFinished();
            return;
        }

        final List<String> newTags = new ArrayList<>();
        for (String tag : tags) {
            newTags.add(tag);
        }
        final File file = new File(diaryRecorder.getFilePath());

        diaryListView.setIsSaving(true);

        final Date saveTime = new Date();
        final String newItemId = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(saveTime);

        if (isNetworkAvailable) {

            final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest(file.getAbsolutePath());

            compositeDisposable.add(diaryRepository.requestEmotionAnalyze(request).
                    map(emotion -> new DiaryEntity(newItemId,
                            saveTime,
                            file.getAbsolutePath(),
                            newTags,
                            selectedEmotion,
                            emotion))
                    .flatMapCompletable(diaryRepository::insertDiary)
                    .andThen(diaryRepository.loadRecentInsertedDiary())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaryEntity -> {
                        setLastItemSavedTime(saveTime);
                        //diaryListView.setRecordCardVisibilityGone();
                        diaryRecorder.clearFilePath();
                        diaryListView.showAnalyzedEmotion(diaryEntity.getAnalyzedEmotion());
                        diaryListView.insertDiaryList(DiaryMapper.toDiary(diaryEntity));
                        diaryListView.setIsSaving(false);
                    }, Throwable::printStackTrace));
        } else {

            diaryListView.showAnalyzeIgnore();

            DiaryEntity newDiaryEntity = new DiaryEntity(newItemId,
                    saveTime,
                    file.getAbsolutePath(),
                    tags,
                    selectedEmotion,
                    Emotion.fromValue(2));

            compositeDisposable.add(diaryRepository.insertDiary(newDiaryEntity)
                    .andThen(diaryRepository.loadRecentInsertedDiary())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaryEntity -> {
                        setLastItemSavedTime(saveTime);
                        //diaryListView.setRecordCardVisibilityGone();
                        diaryListView.showAnalyzedEmotion(diaryEntity.getAnalyzedEmotion());
                        diaryListView.insertDiaryList(DiaryMapper.toDiary(diaryEntity));
                        diaryListView.setIsSaving(false);
                    }, Throwable::printStackTrace));
        }
    }

    void playDiaryRecord(@NonNull String filePath, final int currentPlayPosition) {
        // 재생중
        if (lastPlayedPosition != NOTHING_PLAYED) {
            recordPlayer.stop();
            diaryListView.onPlayFileChanged(lastPlayedPosition, true);
            if (lastPlayedPosition == currentPlayPosition) {
                lastPlayedPosition = NOTHING_PLAYED;
            } else {
                recordPlayer.play(filePath);
                lastPlayedPosition = currentPlayPosition;
                diaryListView.onPlayFileChanged(lastPlayedPosition, false);
            }
        } else {
            lastPlayedPosition = currentPlayPosition;
            diaryListView.onPlayFileChanged(lastPlayedPosition, false);
            recordPlayer.play(filePath);
        }
    }

    void setSelectedEmotion(@Nullable Emotion emotion) {
        this.selectedEmotion = emotion;
    }

    void startRecording() {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "diary");
        dir.mkdir();

        if (!isRecording) {
            diaryRecorder.startRecord();
            this.isRecording = true;
        }
    }

    void finishRecording() {
        if (isRecording) {
            diaryRecorder.finishRecord();
            this.isRecording = false;
        }
    }

    private void setLastItemSavedTime(@NonNull Date savedTime) {
        sharedPreferenceManager.setLastDiarySaveTime(savedTime);
    }


    void onViewCreated() {
        //String today = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(new Date());

        /*if (sharedPreferenceManager.getLastDiarySaveTime().equals(today)) {
            diaryListView.setRecordCardVisibilityGone();
        }*/

        compositeDisposable.add(EventBus.get()
                .filter(event -> event.equals(Event.BACK_UP_COMPLETE))
                .subscribe(event -> diaryListView.setIsBackup(true),
                        Throwable::printStackTrace));
    }

    void onViewPaused() {
        if (isRecording) {
            diaryRecorder.finishRecord();
            isRecording = false;
        }
        if(lastPlayedPosition != NOTHING_PLAYED) {
            recordPlayer.stop();
            diaryListView.onPlayFileChanged(lastPlayedPosition, true);
        }
        lastItemLoadedTime = new Date();
        isLoading = false;
        compositeDisposable.clear();
        diaryListView.setIsSaving(false);
    }

    void sendDiaryToKakao(Diary diary) {

        compositeDisposable.add(
                diaryRepository
                        .loadShareDiary(diary.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(diaryEntity -> kakaoLinkHelper.sendDiary(diaryEntity))
        );
    }
}
