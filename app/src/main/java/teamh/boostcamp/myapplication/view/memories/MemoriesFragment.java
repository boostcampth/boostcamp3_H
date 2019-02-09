package teamh.boostcamp.myapplication.view.memories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.FragmentMemoriesBinding;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.model.source.local.AppDatabase;
import teamh.boostcamp.myapplication.utils.ResourceSendUtil;
import teamh.boostcamp.myapplication.view.BaseFragment;
import teamh.boostcamp.myapplication.view.play.PlayActivity;


public class MemoriesFragment extends BaseFragment<FragmentMemoriesBinding> implements MemoriesContractor.View, MemoriesCardAdapter.ViewClickListener {

    private static final String EXTRA_MEMORY = "memory";
    private MemoriesPresenter presenter;
    private MemoriesCardAdapter mMemoriesCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        presenter = new MemoriesPresenter(this, new ResourceSendUtil(getContext()));
        presenter.onViewAttached();
        binding.setView(this);

        binding.rvCard.setHasFixedSize(true);
        binding.rvCard.setVerticalScrollbarPosition(0);
        mMemoriesCardAdapter = new MemoriesCardAdapter(getContext());
        mMemoriesCardAdapter.setOnClickListener(this);
        binding.rvCard.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCard.setAdapter(mMemoriesCardAdapter);

        presenter.setMemoriesCardAdapterModel(mMemoriesCardAdapter);
        presenter.setMemoriesCardAdapterView(mMemoriesCardAdapter);
        presenter.setDatabase(AppDatabase.getInstance(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
        presenter = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memories;
    }

    @Override
    public void makeToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToPlayActivity(Memory memory) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        intent.putExtra(EXTRA_MEMORY, memory);
        startActivity(intent);
        this.getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_stop);
    }

    @Override
    public void onPlayButtonClicked(int position) {
        presenter.onPlayButtonClicked(position);
    }

    @Override
    public void onCloseButtonLicked(int position) {
        presenter.onDeleteButtonClicked(position);
    }

    public void onRecommendButtonClicked(View view) {
        presenter.onRecommendMemory();
    }
}
