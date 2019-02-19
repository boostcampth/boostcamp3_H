package teamh.boostcamp.myapplication.view.diarylist;

import androidx.annotation.Nullable;

public interface DiaryRecorder {

    /* 녹음 시작 */
    void startRecord();

    /* 녹음 종료 */
    void finishRecord();

    /* 자원 해제 */
    void releaseRecorder();

    /* 파일 경로 반환*/
    @Nullable
    String getFilePath();

    /* 녹음 준비 */
    void prepareRecord();

    void clearFilePath();
/*
    *//* TimeOutListener 설정*//*
    void setMediaRecorderTimeOutListener(MediaRecorderTimeOutListener mediaRecorderTimeOutListener);*/
}
