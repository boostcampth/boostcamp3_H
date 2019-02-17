package teamh.boostcamp.myapplication.view.diarylist;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentDiaryListBinding;
import teamh.boostcamp.myapplication.utils.KeyPadUtil;
import teamh.boostcamp.myapplication.utils.NetworkStateUtil;
import teamh.boostcamp.myapplication.view.diarylist.kakaoLink.KakaoLinkHelperImpl;
import teamh.boostcamp.myapplication.view.diarylist.popup.analyzeResult.AnalyzedEmotionShowingDialog;
import teamh.boostcamp.myapplication.view.diarylist.popup.record.OnRecordDialogDismissListener;
import teamh.boostcamp.myapplication.view.diarylist.popup.record.RecordingDiaryDialog;
import teamh.boostcamp.myapplication.view.play.RecordPlayerImpl;

public class DiaryListFragment extends Fragment implements DiaryListView, OnRecordDialogDismissListener {

    private static final int LOAD_ITEM_NUM = 5;
    public final ObservableBoolean isSaving = new ObservableBoolean(false);

    private Context context;
    private FragmentDiaryListBinding binding;
    private DiaryListPresenter presenter;
    private CompositeDisposable compositeDisposable;
    private DiaryListAdapter diaryListAdapter;
    private HashTagListAdapter hashTagListAdapter;

    public DiaryListFragment() { /*Empty*/}

    @NonNull
    public static DiaryListFragment newInstance() {
        return new DiaryListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_list, container, false);
        binding.setFragment(DiaryListFragment.this);
        initPresenter();
        initAdapter();
        initView();

        presenter.loadDiaryList(LOAD_ITEM_NUM);

        return binding.getRoot();
    }

    @Override
    public void addDiaryList(@NonNull List<Diary> diaryList) {
        diaryListAdapter.addDiaryItems(diaryList);
    }

    @Override
    public void onPlayFileChanged(final int lastPlayedIndex, final boolean isFinished) {
        diaryListAdapter.changePlayItemIcon(lastPlayedIndex, isFinished);
    }

    @Override
    public void showLoadDiaryListFailMsg() {
        showToastMessage(R.string.item_record_load_diary_list_fail);
    }

    @Override
    public void showRecordNotFinished() {
        showToastMessage(R.string.item_record_now_recording);
    }

    @Override
    public void showEmotionNotSelected() {
        showToastMessage(R.string.item_record_emotion_not_selected);
    }

    @Override
    public void showAnalyzedEmotion(Emotion emotion) {
        AnalyzedEmotionShowingDialog dialog = AnalyzedEmotionShowingDialog.getInstance(emotion);
        if (getFragmentManager() != null) {
            dialog.show(getFragmentManager(), getTag());
        }
    }

    @Override
    public void showRecordFileNotFound() {
        showToastMessage(R.string.item_record_file_not_found);
    }

    @Override
    public void showSaveDiaryFail() {
        showToastMessage(R.string.item_record_save_fail);
    }

    @Override
    public void insertDiaryList(@NonNull Diary diary) {
        diaryListAdapter.insertDiaryItem(diary);
    }

    @Override
    public void setRecordCardVisibilityGone() {
        binding.cvFragmentDiaryRecord.setVisibility(View.GONE);
        binding.tvFragmentDiaryToday.setVisibility(View.GONE);
        diaryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setIsSaving(boolean isSaving) {
        this.isSaving.set(isSaving);
    }


    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show();
    }

    private void initPresenter() {
        presenter = new DiaryListPresenter(this,
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(
                        context.getApplicationContext()).diaryDao(),
                        DeepAffectApiClient.getInstance()),
                new DiaryRecorderImpl(),
                RecordPlayerImpl.getINSTANCE(),
                SharedPreferenceManager.getInstance(context.getApplicationContext()),
                new KakaoLinkHelperImpl(context.getApplicationContext()));
    }

    private void initView() {
        presenter.onViewCreated();
        binding.recyclerViewMainList.setNestedScrollingEnabled(false);
        binding.recyclerViewMainList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyclerViewMainList.setAdapter(diaryListAdapter);
        binding.nsvFragmentDiaryContainer.setOnScrollChangeListener(
                (NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
                    if (v.getChildAt(0).getBottom() <= (v.getHeight() + v.getScrollY())) {
                        presenter.loadDiaryList(LOAD_ITEM_NUM);
                    }
                });

        binding.etItemRecordInput.setHashTagListAdapter(hashTagListAdapter);
        binding.rvItemRecordTags.setAdapter(hashTagListAdapter);
        binding.rvItemRecordTags.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));


        binding.buttonRecordItemRecord.setOnClickListener(v ->
                compositeDisposable.add(TedRx2Permission.with(context)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                        .setRationaleTitle(context.getString(R.string.item_record_permission_title))
                        .setRationaleMessage(context.getString(R.string.item_record_permission_msg))
                        .request()
                        .subscribe(tedPermissionResult -> {
                            if (tedPermissionResult.isGranted()) {
                                KeyPadUtil.closeKeyPad(context, binding.etItemRecordInput);
                                presenter.startRecording();
                                final RecordingDiaryDialog recordingDiaryDialog = RecordingDiaryDialog.newInstance();
                                recordingDiaryDialog.setDismissListener(this);
                                recordingDiaryDialog.show(getFragmentManager(), getTag());
                            } else {
                                showToastMessage(R.string.permission_denied);
                            }
                        }, Throwable::printStackTrace))
        );

        binding.buttonItemRecordDone.setOnClickListener(v ->
                presenter.saveDiary(hashTagListAdapter.getTags(), NetworkStateUtil.isNetworkConnected(context))
        );

        binding.tvRecordItemPgood.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.GOOD));
        binding.tvRecordItemGood.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.VERY_GOOD));
        binding.tvRecordItemNormal.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.NEUTRAL));
        binding.tvRecordItemBad.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.BAD));
        binding.tvRecordItemMad.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.VERY_BAD));
    }

    private void initAdapter() {
        diaryListAdapter = new DiaryListAdapter(context);
        diaryListAdapter.setOnRecordItemClickListener(pos ->
                presenter.playDiaryRecord(Arrays.asList(diaryListAdapter.getDiary(pos)), pos)
        );

        diaryListAdapter.setOnKakaoLinkClickListener(pos ->
                presenter.sendDiaryToKakao(diaryListAdapter.getDiary(pos))
        );

        hashTagListAdapter = new HashTagListAdapter(context);
        hashTagListAdapter.setItemClickListener(pos -> hashTagListAdapter.removeItem(pos));
    }

    @Override
    public void onDismiss(boolean isTimeOut) {
        if (isTimeOut) {
            showToastMessage(R.string.item_record_time_out);
        }
        presenter.finishRecording();
    }
}
