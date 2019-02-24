package teamh.boostcamp.myapplication.view.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Recall;
import teamh.boostcamp.myapplication.databinding.ActivityPlayBinding;
import teamh.boostcamp.myapplication.view.recall.DiaryTitleListAdapter;

public class PlayActivity extends AppCompatActivity implements PlayView {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
    private static final String TAG = "PlayActivity";
    private static final String EXTRA = "recall";

    private ActivityPlayBinding binding;
    private PlayPresenter presenter;
    private Recall recall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            recall = (Recall) savedInstanceState.getSerializable(EXTRA);
        }else{
            recall = (Recall) getIntent().getSerializableExtra(EXTRA);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        binding.setActivity(this);

        initPresenter();
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopPlaying();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
        presenter = null;
    }

    private void initViews() {
        initRecyclerView();
        initTitleTextView();
    }

    private void initTitleTextView() {
        binding.tvTitle.setText(generateRecallTitle(recall));
    }

    private void initRecyclerView() {
        binding.rvDiaryList.setHasFixedSize(true);
        binding.rvDiaryList.setVerticalScrollbarPosition(0);
        binding.rvDiaryList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DiaryTitleListAdapter adapter = new DiaryTitleListAdapter(this);
        binding.rvDiaryList.setAdapter(adapter);
        adapter.addItems(recall.getDiaryList());
    }

    private void initPresenter() {
        RecordPlayer recordPlayer = RecordPlayerImpl.getINSTANCE();
        recordPlayer.setList(recall.getDiaryList());
        presenter = new PlayPresenter(
                recordPlayer,
                this);
    }

    public void onCloseButtonClicked(View view) {
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onPlayButtonClicked(View view) {
        presenter.playRecalls();
    }

    @Override
    public void showPlayingState(boolean playingState) {
        if (playingState) {
            showToastMessage(R.string.playing_state_play_message);
        } else {
            showToastMessage(R.string.playing_state_stop_message);
        }
    }

    @Override
    public void setButtonText(boolean playState) {
        if(playState){
            binding.btnPlay.setText("STOP");
        }else{
            binding.btnPlay.setText("PLAY");
        }
    }

    @Override
    public void showListSizeError() {
        showToastMessage(R.string.no_playlist_message);
    }

    @NonNull
    private String generateRecallTitle(@NonNull Recall recall) {
        String startDateString = DateToSimpleFormat(recall.getStartDate());
        String endDateString = DateToSimpleFormat(recall.getEndDate());
        String emotionString = emotionToString(recall.getEmotion().getEmotion());
        return String.format("%s 부터 %s까지의 %s", startDateString, endDateString, emotionString);
    }

    @NonNull
    private String DateToSimpleFormat(@NonNull Date date) {
        return simpleDateFormat.format(date);
    }

    @NonNull
    private String emotionToString(int emotion) {
        switch (emotion) {
            case 0:
                return "불행함들";
            case 1:
                return "슬픔들";
            case 2:
                return "그저그런날들";
            case 3:
                return "즐거움들";
            case 4:
                return "행복들";
            default:
                return "행복들";

        }
    }

    private void showToastMessage(@StringRes int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_LONG).show();
    }
}
