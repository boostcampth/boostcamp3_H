package team_h.boostcamp.myapplication.view.diaryList;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.api_service.APIClient;
import team_h.boostcamp.myapplication.api_service.emotion.EmotionAnalysisAPI;
import team_h.boostcamp.myapplication.api_service.emotion.EmotionAnalysisResponse;
import team_h.boostcamp.myapplication.api_service.emotion.EmotionAnalyzeRequest;
import team_h.boostcamp.myapplication.api_service.emotion.mapper.AnalyzedEmotionMapper;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;

public class DiaryPresenter implements DiaryContract.Presenter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private static final String TAG = DiaryPresenter.class.getSimpleName();
    public final ObservableBoolean isSaving = new ObservableBoolean(false); // 로딩 중 flag 바인딩

    private DiaryContract.View view;
    private ResourceSendUtil mResourceSendUtil;

    private MediaRecorder mMediaRecorder;

    // HashTagAdapter
    private AdapterContract.Model<String> mHashTagListModelAdapter;
    private AdapterContract.View mHashTagListViewAdapter;

    // DB
    private AppDatabase db;

    private int selectedEmotion = -1;
    private boolean isRecording = false;

    private String mFilePath;

    DiaryPresenter(@NonNull DiaryContract.View view,
                   @NonNull AppDatabase db,
                   @NonNull ResourceSendUtil resourceSendUtil) {
        this.view = view;
        this.mResourceSendUtil = resourceSendUtil;
        this.db = db;
    }

    @Override
    public void onViewAttached() {
        // record 객체 생성
        mMediaRecorder = new MediaRecorder();
    }

    @Override
    public void onViewDetached() {
        // 화면이 사라질 때 release 해주기
        if (mMediaRecorder != null) {
            if (isRecording) {
                mMediaRecorder.stop();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /*
     * 관심사의 분리의 관점에서 왜 Adapter 의 View, Model 을 나눴는지 생각  */
    @Override
    public void setHashTagListModelAdapter(HashTagListAdapter adapter) {
        mHashTagListModelAdapter = adapter;
    }

    @Override
    public void setHashTagListViewAdapter(HashTagListAdapter adapter) {
        mHashTagListViewAdapter = adapter;
    }

    /* Method Reference 를 통해 Emotion Image Click Event binding */
    public void onEmotionButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_record_item_mad:
                Log.e("Test", "mad");
                selectedEmotion = 0;
                break;

            case R.id.tv_record_item_bad:
                Log.e("Test", "bad");
                selectedEmotion = 1;
                break;

            case R.id.tv_record_item_normal:
                Log.e("Test", "normal");
                selectedEmotion = 2;
                break;

            case R.id.tv_record_item_pgood:
                Log.e("Test", "pgood");
                selectedEmotion = 3;
                break;

            case R.id.tv_record_item_good:
                Log.e("Test", "good");
                selectedEmotion = 4;
                break;
        }
    }


    /*
     * 녹음 버튼 클릭 이벤트 처리 */
    @Override
    public void onRecordButtonClicked() {
        // 이미 녹음중인 경우
        if (isRecording) {
            // stop 을 해버리면 MediaRecorder 의 셋팅이 사라짐
            mMediaRecorder.stop();
            Log.e(TAG, "Recording Stop");
        } else {
            // 시작할 땐 셋팅을 다시 해줘야함
            initMediaRecorder();
            mMediaRecorder.start();
            Log.e(TAG, "Recording Start");
        }
        // 녹음 상태 변경
        isRecording = !isRecording;
        // 저장될 파일 경로
        mFilePath = getTodayRecordFilePath();
    }

    /*
     * 저장 버튼 클릭 이벤트 */
    @Override
    public void onDoneButtonClicked() {
        // 현재 녹음중이면 거부
        if (isRecording) {
            view.showToastMessage(mResourceSendUtil.getString(R.string.item_record_now_recording), Toast.LENGTH_SHORT);
            return;
        }

        // 감정을 선택하지 않았다면
        if (selectedEmotion == -1) {
            // 감정 선택 Toast
            view.showToastMessage(mResourceSendUtil.getString(R.string.item_record_no_emotion_selected), Toast.LENGTH_SHORT);
            return;
        }

        File file = new File(getTodayRecordFilePath());

        // 저장된 Record File 이 없다면
        if (!file.exists()) {
            view.showToastMessage(mResourceSendUtil.getString(R.string.item_record_no_voice_file), Toast.LENGTH_SHORT);
            return;
        }

        // 분석용 EncodedString
        String encodedRecordFile = getBase64EncodedFile(file);

        // API 호출 및 Room 저장
        view.closeHashTagKeyPad();
        isSaving.set(true);
        analyzeRecordEmotion(encodedRecordFile);
    }

    // MediaRecord Finite State Machine 꼭 참고하기 !!!
    private void initMediaRecorder() {
        // 순서 유의
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOutputFile(getTodayRecordFilePath());

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 오늘 등록할 녹음 파일의 경로 생성( 년도-월-일.acc )
    private String getTodayRecordFilePath() {
        return String.format("%s/%s.acc",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                DATE_FORMAT.format(new Date()));
    }

    // API 분석을 위해 녹음 파일을 Base64 Encoding 수행
    private String getBase64EncodedFile(final File file) {

        byte[] encodedByteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(encodedByteArray);
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

    private void analyzeRecordEmotion(final String encodedRecord) {

        
        String apiKey = mResourceSendUtil.getString(R.string.deep_affects_key);

        // RxJava 로 감정 분석 및 저장까지 수행
        APIClient.getInstance().getClient()
                .create(EmotionAnalysisAPI.class)
                .analyzeRecordEmotion(apiKey, EmotionAnalyzeRequest.request(encodedRecord))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        emotionAnalysisResponses -> {
                            Log.e("Test", "onResponse");

                            int analyzedEmotion = AnalyzedEmotionMapper.parseAnalyzedEmotion(emotionAnalysisResponses);

                            //다이어리 객체 생성
                            Diary todayDiaryItem = new Diary(0,
                                    DATE_FORMAT.format(new Date()),
                                    mFilePath,
                                    mHashTagListModelAdapter.getItemList().toString(),
                                    selectedEmotion,
                                    analyzedEmotion);

                            Completable.fromAction(() -> db.appDao().insertDiary(todayDiaryItem))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        isSaving.set(false);
                                        Log.e("Test", "저장되었어여~~");
                                    }, throwable -> {
                                        isSaving.set(false);
                                        Log.e("Test", "저장안도미...");

                                    });
                        }, throwable -> {
                            isSaving.set(false);
                            // 에러처리는 어떤식으로 ?
                            Log.e("Test", "onError");
                        }
                );
    }
}
