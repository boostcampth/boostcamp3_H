package team_h.boostcamp.myapplication.view.memories;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentMemoriesBinding;
import team_h.boostcamp.myapplication.view.BaseFragment;


public class MemoriesFragment extends BaseFragment<FragmentMemoriesBinding, MemoriesPresenter> implements MemoriesContractor.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memories;
    }

    @Override
    protected MemoriesPresenter getPresenter() {
        return new MemoriesPresenter(this);
    }

    @Override
    public void showToastMessage(String message, int exposedTime) {

    }
}
