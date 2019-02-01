package team_h.boostcamp.myapplication.view.diaryList;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.databinding.ObservableBoolean;

/*
 * MediaRecord 작업에만 관심이 있는 클래스 분리
 * Presenter 에 주입하는 방식으로 수행 */
public class MediaRecorderWrapper {

    // 저장 경로 생성용
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

    // xml 과 연결하여 저장시 조작 가능 여부 변경
    public final ObservableBoolean isRecording = new ObservableBoolean(false);

    private MediaRecorder mediaRecorder;
    private String filePath;

    MediaRecorderWrapper() {
        mediaRecorder = new MediaRecorder();
    }

    // 녹음 시작
    void startRecord() {
        prepareRecord();
        isRecording.set(true);
        mediaRecorder.start();
    }

    // 녹음 종료
    void finishRecord() {
        mediaRecorder.stop();
        isRecording.set(false);
    }

    // 리소스 해제
    void releaseRecord() {
        if (isRecording.get()) {
            mediaRecorder.stop();
            isRecording.set(false);
        }
        mediaRecorder.release();
    }

    // 파일 경로 반환
    String getFilePath() {
        return filePath;
    }

    // 녹음 상태 반환
    boolean isRecordingNow() {
        return isRecording.get();
    }

    // 녹음 사전 준비 (순서 유의)
    private void prepareRecord() {

        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }

        filePath = generateFilePath();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFilePath() {
        return String.format("%s/%s.acc",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                DATE_FORMAT.format(new Date()));
    }
}
