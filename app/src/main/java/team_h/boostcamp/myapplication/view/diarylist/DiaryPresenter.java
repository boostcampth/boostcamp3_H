package team_h.boostcamp.myapplication.view.diarylist;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.BuildConfig;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.api.deepaffects.DeepAffectApiClient;
import team_h.boostcamp.myapplication.api.deepaffects.EmotionAnalysisAPI;
import team_h.boostcamp.myapplication.api.deepaffects.EmotionAnalysisResponse;
import team_h.boostcamp.myapplication.api.deepaffects.EmotionAnalyzeRequest;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.utils.AnalyzedEmotionMapper;

public class DiaryPresenter implements DiaryContract.Presenter {

    private static final String TAG = "DiaryPresenter";

    private DiaryContract.View view;

    private AppDatabase appDatabase;
    private MediaRecorderWrapper mediaRecorderWrapper;

    private CompositeDisposable compositeDisposable;

    private int selectedEmotion = -1;

    DiaryPresenter(@NonNull DiaryContract.View view,
                   @NonNull AppDatabase appDatabase,
                   @NonNull MediaRecorderWrapper mediaRecorderWrapper) {
        this.view = view;
        this.appDatabase = appDatabase;
        this.mediaRecorderWrapper = mediaRecorderWrapper;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {
        // 화면 종료시 리소스 해제
        mediaRecorderWrapper.releaseRecord();
        // 만약 구독중인 비동기 작업이 있다면 해제
        compositeDisposable.clear();
    }


    /* Method Reference 를 통해 Emotion Image Click Event binding */
    public void emotionChanged(final int id) {
        switch (id) {
            case R.id.tv_record_item_mad:
                Log.e(TAG, "mad");
                selectedEmotion = 0;
                break;

            case R.id.tv_record_item_bad:
                Log.e(TAG, "bad");
                selectedEmotion = 1;
                break;

            case R.id.tv_record_item_normal:
                Log.e(TAG, "normal");
                selectedEmotion = 2;
                break;

            case R.id.tv_record_item_pgood:
                Log.e(TAG, "pgood");
                selectedEmotion = 3;
                break;

            case R.id.tv_record_item_good:
                Log.e(TAG, "good");
                selectedEmotion = 4;
                break;
        }
    }

    @Override
    public void recordDiaryItem() {
        if (mediaRecorderWrapper.isRecordingNow()) {
            // 녹음중이면 종료
            mediaRecorderWrapper.finishRecord();
        } else {
            // 녹음 시작
            mediaRecorderWrapper.startRecord();
        }
    }


    @Override
    public void saveDiaryItem(List<String> tags) {

        // 현재 녹음중이면 거부
        if (mediaRecorderWrapper.isRecordingNow()) {
            view.showRecordNotFinishedMessage();
            return;
        }

        // 감정을 선택하지 않았다면
        if (selectedEmotion == -1) {
            view.showEmotionNotSelectedMessage();
            return;
        }

        File file = new File(mediaRecorderWrapper.getFilePath());

        // 저장된 Record File 이 없다면
        if (!file.exists()) {
            view.showNoRecordFileMessage();
            return;
        }

        // 키패드 닫기
        view.closeHashTagKeyPad();

        // 분석에 필요한 데이터들
        String encodedRecordFile = getBase64EncodedFile(file);

        // 분석 후 저장
        compositeDisposable.add(analyzeEmotion(encodedRecordFile)
                .subscribe(emotionAnalysisResponses -> {

                    // 여기도 스레드안이니까 바로 DB 에 저장
                    appDatabase.appDao().insertDiary(new Diary(
                            0, // auto generate
                            file.getName().split(".")[0],
                            mediaRecorderWrapper.getFilePath(),
                            tags.toString(),
                            selectedEmotion,
                            AnalyzedEmotionMapper.parseAnalyzedEmotion(emotionAnalysisResponses)
                    ));

                    view.showDiaryItemSaved();

                }, throwable -> view.showEmotionAnalyzeFailMessage()));
    }

    private Single<List<EmotionAnalysisResponse>> analyzeEmotion(String encodedRecordFile) {

        return DeepAffectApiClient.getInstance()
                .create(EmotionAnalysisAPI.class)
                .analyzeRecordEmotion(BuildConfig.apikey, EmotionAnalyzeRequest.request(encodedRecordFile))
                .subscribeOn(Schedulers.io());
    }

    // API 분석을 위해 녹음 파일을 Base64 Encoding 수행
    private String getBase64EncodedFile(final File file) {

        byte[] encodedByteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            int readBytes = fileInputStream.read(encodedByteArray);

            if(readBytes == 0) {
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
