package teamh.boostcamp.myapplication.view.diarylist;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

/*
 * MediaRecord 작업에만 관심이 있는 클래스 분리
 * Presenter 에 주입하는 방식으로 수행 */
class DiaryRecorderImpl implements DiaryRecorder {

    // 저장 경로 생성용
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    // 최대 녹음 시간
    private static final int MAX_DURATION = 1000 * 60;

    private MediaRecorder mediaRecorder;
    private String filePath;

    private MediaRecorderTimeOutListener mediaRecorderTimeOutListener;

    DiaryRecorderImpl() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setMaxDuration(MAX_DURATION);
    }

    @Override
    public void startRecord() {
        prepareRecord();
        mediaRecorder.start();
    }

    @Override
    public void finishRecord() {
        mediaRecorder.stop();
    }


    @Override
    public void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @NonNull
    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void prepareRecord() {
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

    @Override
    public void setMediaRecorderTimeOutListener(@NonNull MediaRecorderTimeOutListener mediaRecorderTimeOutListener) {
        this.mediaRecorderTimeOutListener = mediaRecorderTimeOutListener;
        // 다른 함수로 빼기
        this.mediaRecorder.setOnInfoListener((mediaRecorder1, i, i1) ->
                this.mediaRecorderTimeOutListener.onTimeOut()
        );
    }

    private String generateFilePath() {
        return String.format("%s/%s.acc",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                DATE_FORMAT.format(new Date()));
    }
}
