package teamh.boostcamp.myapplication.view.recall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.LogEvent;
import teamh.boostcamp.myapplication.data.model.Recall;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentRecallBinding;
import teamh.boostcamp.myapplication.utils.FirebaseEventLogger;
import teamh.boostcamp.myapplication.view.play.PlayActivity;

public class RecallFragment extends Fragment implements RecallView {

    @NonNull
    private static final String EXTRA = "recall";
    private RecallPresenter presenter;
    private FragmentRecallBinding binding;
    private RecallListAdapter recallListAdapter;

    public RecallFragment() {
    }

    public static RecallFragment newInstance() {
        return new RecallFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recall, container, false);
        binding.setView(this);
        initPresenter();
        initViews();
        presenter.loadRecallList();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onViewDestroyed();
    }

    @Override
    public void addRecallList(@NonNull List<Recall> recallList) {
        recallListAdapter.updateItems(recallList);
    }

    @Override
    public void addRecall(@NonNull Recall recall) {
        recallListAdapter.addItem(recall);
        binding.rvCard.scrollToPosition(0);
    }

    @Override
    public void deleteRecall(int position) {
        recallListAdapter.deleteItem(position);
    }

    @Override
    public void onGenerateNewRecallButtonClicked(View view) {
        presenter.generateRecall();
    }

    @Override
    public void showDeleteSuccessResult() {
        Toast.makeText(getContext(), getString(R.string.delete_recall_message), Toast.LENGTH_LONG).show();
    }

    private void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.rvCard.setHasFixedSize(true);
        binding.rvCard.setVerticalScrollbarPosition(0);
        recallListAdapter = new RecallListAdapter(getContext());
        binding.rvCard.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCard.setAdapter(recallListAdapter);
        recallListAdapter.setButtonClickListener(new RecallListAdapter.ButtonClickListener() {
            @Override
            public void onPlayButtonClicked(Recall recall) {
                FirebaseEventLogger.getInstance(getContext()).addLogEvent(LogEvent.PLAY_RECALL_BUTTON_CLICK);
                Intent intent = new Intent(getContext(), PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA, recall);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_stop);
            }

            @Override
            public void onDeleteButtonClicked(int position, int id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);
                alertDialogBuilder.setCancelable(false)
                        .setTitle(getString(R.string.delete_recall_dialog_title))
                        .setMessage(getString(R.string.delete_recall_dialog_message))
                        .setPositiveButton(getString(R.string.delete_recall_dialog_button), (dialogInterface, i) -> {
                            presenter.deleteRecall(position, id);
                        }).setNegativeButton(getString(R.string.delete_recall_dialog_cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
            }
        });
    }

    private void initPresenter() {
        presenter = new RecallPresenter(this,
                RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getContext())));
    }
}
