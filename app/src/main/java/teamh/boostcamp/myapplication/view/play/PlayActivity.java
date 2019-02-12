package teamh.boostcamp.myapplication.view.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Recall;
import teamh.boostcamp.myapplication.databinding.ActivityPlayBinding;
import teamh.boostcamp.myapplication.view.BaseActivity;
import teamh.boostcamp.myapplication.view.recall.DiaryTitleListAdapter;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements PlayView {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
    private static final String TAG = "PlayActivity";
    private static final String EXTRA = "recall";

    private PlayPresenter presenter;
    private DiaryTitleListAdapter adapter;
    private Recall recall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        recall = (Recall) getIntent().getSerializableExtra(EXTRA);

        initPresenter();
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
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
        adapter = new DiaryTitleListAdapter(this);
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
        Log.d(TAG, "onCloseButtonClicked: ");
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onPlayButtonClicked(View view) {
        presenter.playMemory();
    }

    @Override
    public void makeToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
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
}
