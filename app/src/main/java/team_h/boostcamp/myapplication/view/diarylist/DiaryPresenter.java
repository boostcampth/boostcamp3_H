package team_h.boostcamp.myapplication.view.diarylist;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.data.remote.deepaffects.request.EmotionAnalyzeRequest;
import team_h.boostcamp.myapplication.data.repository.DiaryRepository;
import team_h.boostcamp.myapplication.model.Diary;

public class DiaryPresenter implements DiaryContract.Presenter {

    private static final String TAG = "DiaryPresenter";

    public final ObservableBoolean isRecording = new ObservableBoolean(false);

    private DiaryContract.View view;

    private DiaryRepository diaryRepository;
    private DiaryRecorder diaryRecorder;

    private CompositeDisposable compositeDisposable;

    private int selectedEmotion = -1;

    DiaryPresenter(@NonNull DiaryContract.View view,
                   @NonNull DiaryRepository diaryRepository,
                   @NonNull DiaryRecorder diaryRecorder) {
        this.view = view;
        this.diaryRepository = diaryRepository;
        this.diaryRecorder = diaryRecorder;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewDetached() {
        // 화면 종료시 리소스 해제
        diaryRecorder.releaseRecorder();
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
        if (isRecording.get()) {
            // 녹음중이면 종료
            isRecording.set(false);
            diaryRecorder.finishRecord();
        } else {
            // 녹음 시작
            isRecording.set(true);
            diaryRecorder.startRecord();
        }
    }

    @Override
    public void saveDiaryItem(final List<String> tags) {

        // 현재 녹음중이면 거부
        if (isRecording.get()) {
            view.showRecordNotFinishedMessage();
            return;
        }

        // 감정을 선택하지 않았다면
        if (selectedEmotion == -1) {
            view.showEmotionNotSelectedMessage();
            return;
        }

        final File file = new File(diaryRecorder.getFilePath());

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

        // 저장 후
        compositeDisposable.add(diaryRepository.analyzeVoiceEmotion(request)
                .map(analyzedEmotion -> new Diary(0,
                        file.getName().split(".")[0],
                        diaryRecorder.getFilePath(),
                        tags.toString(),
                        selectedEmotion,
                        analyzedEmotion))
                .flatMapCompletable(diaryRepository::insertRecordItem)
                .subscribe(() -> view.showDiaryItemSaved()
                        , throwable -> view.showDiaryItemSaveFail()
                ));
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
