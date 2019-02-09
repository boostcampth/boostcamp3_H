package teamh.boostcamp.myapplication.view.diarylist;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;

public class DiaryPresenter implements DiaryContract.Presenter {

    private static final String TAG = "DiaryPresenter";

    private DiaryContract.View view;

    private DiaryRepository diaryRepository;
    private DiaryRecorder diaryRecorderImpl;

    private CompositeDisposable compositeDisposable;

    private int selectedEmotion = -1;
    private int currentIdx = Integer.MAX_VALUE;
    private boolean isRecording = false;
    private boolean isLoadingItem = false;

    DiaryPresenter(@NonNull DiaryContract.View view,
                   @NonNull DiaryRepository diaryRepository,
                   @NonNull DiaryRecorder diaryRecorderImpl) {
        this.view = view;
        this.diaryRepository = diaryRepository;
        this.diaryRecorderImpl = diaryRecorderImpl;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewAttached() {
        // 타임 아웃 리스너 달기
        diaryRecorderImpl.setMediaRecorderTimeOutListener(() -> {
            diaryRecorderImpl.finishRecord();
            view.showTimeOutMessage();
            Log.d(TAG, "타임 아웃 !");
        });
    }

    @Override
    public void onViewDetached() {
        finishRecording();
        // 화면 종료시 리소스 해제
        diaryRecorderImpl.releaseRecorder();
        // 만약 구독중인 비동기 작업이 있다면 해제
        compositeDisposable.clear();
    }

    @Override
    public void recordDiaryItem() {
        if (isRecording) {
            // 녹음중이면 종료
            Log.d(TAG, "녹음 종료");
            diaryRecorderImpl.finishRecord();
            isRecording = false;
        } else {
            // 녹음 시작
            Log.d(TAG, "녹음 시작");
            diaryRecorderImpl.startRecord();
            isRecording = true;
        }
        // isRecording = !isRecording;
    }

    @Override
    public void saveDiaryItem(@NonNull final String tags) {

        // 현재 녹음중이면 거부
        if (isRecording) {
            view.showRecordNotFinishedMessage();
            return;
        }

        // 감정을 선택하지 않았다면
        if (selectedEmotion == -1) {
            view.showEmotionNotSelectedMessage();
            return;
        }

        final File file = new File(diaryRecorderImpl.getFilePath());

        // 저장된 Record File 이 없다면
        if (!file.exists()) {
            view.showNoRecordFileMessage();
            return;
        }

        // 키패드 닫기
        view.closeHashTagKeyPad();

        // 분석에 필요한 데이터들
        final String encodedRecordFile = getBase64EncodedFile(file);
        final EmotionAnalyzeRequest request = new EmotionAnalyzeRequest(encodedRecordFile);

        // 인터넷 상태에 따라 감정 분석 할지안할지 결정 -> 추가
        // 분석 후 저장
        compositeDisposable.add(diaryRepository.analyzeVoiceEmotion(request)
                .doOnError(throwable -> view.showEmotionAnalyzeFailMessage())
                .map(analyzedEmotion -> new DiaryEntity(0,
                        file.getName().split("\\.")[0],
                        diaryRecorderImpl.getFilePath(),
                        tags,
                        selectedEmotion,
                        analyzedEmotion,
                        new Date().getTime() / 1000))
                .flatMapCompletable(diaryRepository::insertRecordItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            Log.d(TAG, "저장 성공");
                            view.showDiaryItemSaved();
                            view.clearTagEditText();
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            Log.d(TAG, "저장 실패");
                            view.showDiaryItemSaveFail();
                        }
                ));
    }

    @Override
    public void loadMoreDiaryItems() {

        if (!isLoadingItem) {
            isLoadingItem = true;
            compositeDisposable.add(diaryRepository.loadMoreDiaryItems(currentIdx)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diaryList -> {

                                isLoadingItem = false;
                                if (diaryList.size() != 0) {
                                    currentIdx = diaryList.get(diaryList.size() - 1).getId();
                                } else {
                                    return;
                                }
                                // 받아온 데이터 넘겨주기
                                view.showMoreDiaryItems(diaryList);
                            }
                            , throwable -> {
                                Log.e(TAG, "LegacyDiary Fragment Load 에서 발생");
                                throwable.printStackTrace();
                                isLoadingItem = false;
                            }));
        }
    }


    @Override
    public void setSelectedEmotion(int emotion) {
        this.selectedEmotion = emotion;
        Log.e(TAG, emotion + "");
    }

    private void finishRecording() {
        if (diaryRecorderImpl != null && isRecording) {
            diaryRecorderImpl.finishRecord();
            isRecording = false;
        }
    }

    // API 분석을 위해 녹음 파일을 Base64 Encoding 수행
    @NonNull
    private String getBase64EncodedFile(@NonNull final File file) {

        final byte[] encodedByteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            final int readBytes = fileInputStream.read(encodedByteArray);

            if (readBytes == 0) {
                Log.d(TAG, "0바이트 읽음");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Base64.encodeToString(encodedByteArray, Base64.DEFAULT);
    }
}
