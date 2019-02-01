package team_h.boostcamp.myapplication.view.memories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentMemoriesBinding;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.BaseFragment;
import team_h.boostcamp.myapplication.view.play.PlayActivity;


public class MemoriesFragment extends BaseFragment<FragmentMemoriesBinding> implements MemoriesContractor.View, MemoriesCardAdapter.ViewClickListener{

    private static final String EXTRA_MEMORY = "memory";
    private MemoriesPresenter mPresenter;
    private MemoriesCardAdapter mMemoriesCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mPresenter = generatePresenter();
        mPresenter.onViewAttached();
        binding.setPresenter(mPresenter);

        binding.rvCard.setHasFixedSize(true);
        binding.rvCard.setVerticalScrollbarPosition(0);
        mMemoriesCardAdapter = new MemoriesCardAdapter(getContext());
        mMemoriesCardAdapter.setOnClickListener(this);
        binding.rvCard.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCard.setAdapter(mMemoriesCardAdapter);

        mPresenter.setMemoriesCardAdapterModel(mMemoriesCardAdapter);
        mPresenter.setMemoriesCardAdapterView(mMemoriesCardAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDetached();
        mPresenter = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memories;
    }

    @Override
    public MemoriesPresenter generatePresenter() {
        if(mPresenter == null){
            mPresenter = new MemoriesPresenter(MemoriesFragment.this);
        }
        return mPresenter;
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
        mPresenter.onPlayButtonClicked(position);
    }

    @Override
    public void onCloseButtonLicked(int position) {
        mPresenter.onDeleteButtonClicked(position);
    }
}
