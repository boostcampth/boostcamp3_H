package team_h.boostcamp.myapplication.view.main;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import team_h.boostcamp.myapplication.view.memories.MemoriesFragment;

public class MainTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private MemoriesFragment memoriesFragment = new MemoriesFragment();

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
        this.mFragments = new ArrayList<>();
        this.mFragments.add(memoriesFragment);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }
}
