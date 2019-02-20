package teamh.boostcamp.myapplication.view.play;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Diary;

public class RecordPlayerImpl implements RecordPlayer {
    private static final String TAG = "RecordPlayerImpl";
    private static RecordPlayerImpl INSTANCE;
    private MediaPlayer mediaPlayer;
    private List<Diary> playList;
    private boolean playState = false;
    private int count = 0;
    private OnStateChangeListener onStateChangeListener;

    interface OnStateChangeListener {
        void onStop();

        void onPlay();
    }


    public static RecordPlayerImpl getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (RecordPlayerImpl.class) {
                INSTANCE = new RecordPlayerImpl();
            }
        }
        INSTANCE.initMediaPlayer();
        return INSTANCE;
    }

    private RecordPlayerImpl() {
        if (mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();

            initMediaPlayer();
        }
    }

    @Override
    public void setList(List<Diary> playList) {
        if (playState) {
            stopList();
        }

        this.playList = playList;
    }

    @Override
    public int getListSize() {
        return playList.size();
    }

    @Override
    public void play() {
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
            if (onStateChangeListener != null) {
                onStateChangeListener.onPlay();
            }
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
            if (onStateChangeListener != null) {
                onStateChangeListener.onStop();
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return playState;
    }

    private void initMediaPlayer() {
        if (mediaPlayer != null) {
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

    @Override
    public void setOnCompletionListener(@NonNull MediaPlayer.OnCompletionListener onCompletionListener) {
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    @Override
    public void setOnChangeStateListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    @Override
    public void releasePlayer() {
        if (playState) {
            mediaPlayer.stop();
        }
        playState = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        INSTANCE = null;
        mediaPlayer = null;
    }
}
