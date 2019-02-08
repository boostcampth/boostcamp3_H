package team_h.boostcamp.myapplication.view.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityPlayBinding;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements PlayContractor.View {

    private static final String TAG = "PlayActivity";
    private static final String EXTRA_MEMORY = "memory";

    private PlayContractor.Presenter presenter;
    private Memory memory;
    private PlayDiaryAdapter playDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        memory = getIntent().getParcelableExtra(EXTRA_MEMORY);

        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopDiaryList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
        presenter = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void setDiaryList(List<Diary> diaryList) {
        playDiaryAdapter.addItems(diaryList);
    }

    private void initViews() {
        initPresenter();
        initRecyclerView();
        initTitleTextView();
    }

    private void initTitleTextView() {
        binding.tvTitle.setText(memory.getTitle());
    }

    private void initRecyclerView() {
        playDiaryAdapter = new PlayDiaryAdapter(getApplicationContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(playDiaryAdapter);

        presenter.loadData(memory.getId());
    }

    private void initPresenter() {
        presenter = new PlayPresenter(
                AppDatabase.getInstance(getApplicationContext()),
                RecordPlayerImpl.getINSTANCE(),
                this);
    }

    public void onCloseButtonClicked(View view) {
        Log.d(TAG, "onCloseButtonClicked: ");
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onPlayButtonClicked(View view) {
        presenter.playDiaryList();
    }

    @Override
    public void makeToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

}
