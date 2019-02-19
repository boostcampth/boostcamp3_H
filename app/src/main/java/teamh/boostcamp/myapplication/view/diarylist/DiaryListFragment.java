package teamh.boostcamp.myapplication.view.diarylist;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentDiaryListBinding;
import teamh.boostcamp.myapplication.utils.EventBus;
import teamh.boostcamp.myapplication.utils.KakaoLinkHelperImpl;
import teamh.boostcamp.myapplication.utils.KeyPadUtil;
import teamh.boostcamp.myapplication.utils.NetworkStateUtil;
import teamh.boostcamp.myapplication.view.diarylist.popup.analyzeResult.AnalyzedEmotionShowingDialog;
import teamh.boostcamp.myapplication.view.diarylist.popup.record.OnRecordDialogDismissListener;
import teamh.boostcamp.myapplication.view.diarylist.popup.record.RecordingDiaryDialog;
import teamh.boostcamp.myapplication.view.play.RecordPlayerImpl;

public class DiaryListFragment extends Fragment implements DiaryListView, OnRecordDialogDismissListener {

    private static final int LOAD_ITEM_NUM = 5;
    public final ObservableBoolean isSaving = new ObservableBoolean(false);

    private FragmentDiaryListBinding binding;
    private DiaryListPresenter presenter;
    private CompositeDisposable compositeDisposable;
    private DiaryListAdapter diaryListAdapter;
    private HashTagListAdapter hashTagListAdapter;

    private boolean isDownloaded = false;
    private RecordingDiaryDialog recordingDiaryDialog;

    private FragmentManager fragmentManager;

    public DiaryListFragment() { /*Empty*/}

    @NonNull
    public static DiaryListFragment newInstance() {
        return new DiaryListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getFragmentManager() != null) {
            this.fragmentManager = getFragmentManager();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Test", "DiaryListFragment onDestroy");
        presenter.onViewDestroyed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recordingDiaryDialog.isViewPopUp()) {
            recordingDiaryDialog.dismiss();
        }
        presenter.onViewPaused();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDownloaded) {
            diaryListAdapter.clear();
            presenter.loadDiaryList(LOAD_ITEM_NUM);
            isDownloaded = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_list, container, false);
            binding.setFragment(DiaryListFragment.this);
            initPresenter();
            initAdapter();
            initView();
            initDialog();
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(EventBus.get().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    switch (event) {
                        case CLEAR_COMPLETE:
                            diaryListAdapter.clear();
                            break;
                        case DOWNLOAD_COMPLETE:
                            diaryListAdapter.clear();
                            presenter.loadDiaryList(LOAD_ITEM_NUM);
                            break;
                        default:
                            break;
                    }
                }));

        presenter.loadDiaryList(LOAD_ITEM_NUM);

        return binding.getRoot();
    }

    @Override
    public void addDiaryList(@NonNull List<Diary> diaryList) {
        diaryListAdapter.addDiaryItems(diaryList);
    }

    @Override
    public void setIsBackup(boolean isBackup) {
        this.isDownloaded = isBackup;
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
    public void showAnalyzeIgnore() {
        showToastMessage(R.string.analyze_ignore);
    }

    @Override
    public void setIsSaving(boolean isSaving) {
        this.isSaving.set(isSaving);
    }


    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(getContext(), getString(stringId), Toast.LENGTH_SHORT).show();
    }

    private void initPresenter() {
        if (getContext() != null) {
            presenter = new DiaryListPresenter(this,
                    DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(
                            getContext().getApplicationContext()).diaryDao(),
                            DeepAffectApiClient.getInstance()),
                    new DiaryRecorderImpl(),
                    RecordPlayerImpl.getINSTANCE(),
                    SharedPreferenceManager.getInstance(getContext().getApplicationContext()),
                    new KakaoLinkHelperImpl(getContext().getApplicationContext()));
        }
    }

    private void initView() {
        presenter.onViewCreated();
        if (binding.recyclerViewMainList.getItemAnimator() != null) {
            binding.recyclerViewMainList.getItemAnimator().setChangeDuration(0);
        }
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
        binding.rvItemRecordTags.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


        binding.buttonRecordItemRecord.setOnClickListener(v -> {
                    if (getContext() != null) {
                        compositeDisposable.add(TedRx2Permission.with(getContext())
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                                .setRationaleTitle(getContext().getString(R.string.item_record_permission_title))
                                .setRationaleMessage(getContext().getString(R.string.item_record_permission_msg))
                                .request()
                                .subscribe(tedPermissionResult -> {
                                    if (tedPermissionResult.isGranted()) {
                                        KeyPadUtil.closeKeyPad(getContext(), binding.etItemRecordInput);
                                        presenter.startRecording();
                                        recordingDiaryDialog.show(fragmentManager, getTag());
                                    } else {
                                        showToastMessage(R.string.permission_denied);
                                    }
                                }, Throwable::printStackTrace));
                    }
                }
        );

        binding.buttonItemRecordDone.setOnClickListener(v ->
                presenter.saveDiary(hashTagListAdapter.getTags(), NetworkStateUtil.isNetworkConnected(getContext()))
        );

        binding.tvRecordItemGood.setOnClickListener(v -> setSelectedEmotion(Emotion.fromValue(4)));
        binding.tvRecordItemPgood.setOnClickListener(v -> setSelectedEmotion(Emotion.fromValue(3)));
        binding.tvRecordItemNormal.setOnClickListener(v -> setSelectedEmotion(Emotion.fromValue(2)));
        binding.tvRecordItemBad.setOnClickListener(v -> setSelectedEmotion(Emotion.fromValue(1)));
        binding.tvRecordItemMad.setOnClickListener(v -> setSelectedEmotion(Emotion.fromValue(0)));
    }

    private void setSelectedEmotion(Emotion e) {
        binding.tvRecordItemMad.setTextColor(getResources().getColor(R.color.emoji_color));
        binding.tvRecordItemBad.setTextColor(getResources().getColor(R.color.emoji_color));
        binding.tvRecordItemNormal.setTextColor(getResources().getColor(R.color.emoji_color));
        binding.tvRecordItemPgood.setTextColor(getResources().getColor(R.color.emoji_color));
        binding.tvRecordItemGood.setTextColor(getResources().getColor(R.color.emoji_color));
        switch (e) {
            case VERY_BAD:
                binding.tvRecordItemMad.setTextColor(getResources().getColor(R.color.selected_emoji_color));
                break;
            case BAD:
                binding.tvRecordItemBad.setTextColor(getResources().getColor(R.color.selected_emoji_color));
                break;
            case NEUTRAL:
                binding.tvRecordItemNormal.setTextColor(getResources().getColor(R.color.selected_emoji_color));
                break;
            case GOOD:
                binding.tvRecordItemPgood.setTextColor(getResources().getColor(R.color.selected_emoji_color));
                break;
            case VERY_GOOD:
                binding.tvRecordItemGood.setTextColor(getResources().getColor(R.color.selected_emoji_color));
                break;
        }
        presenter.setSelectedEmotion(e);
    }

    private void initAdapter() {
        diaryListAdapter = new DiaryListAdapter();
        diaryListAdapter.setOnRecordItemClickListener(pos ->
                presenter.playDiaryRecord(Collections.singletonList(diaryListAdapter.getDiary(pos)), pos));

        diaryListAdapter.setOnKakaoLinkClickListener(pos ->
                presenter.sendDiaryToKakao(diaryListAdapter.getDiary(pos))
        );

        hashTagListAdapter = new HashTagListAdapter(getContext());
        hashTagListAdapter.setItemClickListener(pos -> hashTagListAdapter.removeItem(pos));
    }

    private void initDialog() {
        recordingDiaryDialog = RecordingDiaryDialog.newInstance();
        recordingDiaryDialog.setDismissListener(this);
    }

    @Override
    public void onDismiss(boolean isTimeOut) {
        if (isTimeOut) {
            showToastMessage(R.string.item_record_time_out);
        }
        presenter.finishRecording();
    }
}
