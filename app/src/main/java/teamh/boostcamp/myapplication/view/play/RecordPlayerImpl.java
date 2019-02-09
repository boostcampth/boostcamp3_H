package teamh.boostcamp.myapplication.view.play;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public class RecordPlayerImpl implements RecordPlayer{
    private static final String TAG = "RecordPlayerImpl";
    private static RecordPlayerImpl INSTANCE;
    private MediaPlayer mediaPlayer;
    private List<LegacyDiary> playList;
    private boolean playState = false;
    private int count = 0;

    static RecordPlayerImpl getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (RecordPlayerImpl.class) {
                INSTANCE = new RecordPlayerImpl();
            }
        }

        return INSTANCE;
    }

    private RecordPlayerImpl() {
        if (mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();

            initMediaPlayer();
        }
    }

    @Override
    public void setList(List<LegacyDiary> playList) {
        this.playList = playList;
    }

    @Override
    public void playList() {
        if (!playState) {
            try {
                playState = true;
                mediaPlayer.reset();
                mediaPlayer.setDataSource(playList.get(0).getRecordFilePath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                Log.d(TAG, "playList: IOException" + Arrays.toString(e.getStackTrace()));
            }
            mediaPlayer.start();
        } else {
            stopList();
        }

    }

    @Override
    public void stopList() {
        if (playState) {
            count = 0;
            mediaPlayer.stop();
            playState = false;
        }
    }

    @Override
    public boolean isPlaying() {
        return playState;
    }

    private void initMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.setOnCompletionListener(mp -> {
                if (playList.size() - 1 > count) {
                    count++;

                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(playList.get(count).getRecordFilePath());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        Log.d(TAG, "playList: IOException" + Arrays.toString(e.getStackTrace()));
                    }

                    mediaPlayer.start();
                } else {
                    stopList();
                }
            });
        }
    }

}
