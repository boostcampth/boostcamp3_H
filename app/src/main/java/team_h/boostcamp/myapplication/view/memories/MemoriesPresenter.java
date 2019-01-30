package team_h.boostcamp.myapplication.view.memories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;

public class MemoriesPresenter implements MemoriesContractor.Presenter {

    private MemoriesContractor.View view;
    private AdapterContract.Model<Memory> mMemoriesCardAdapterModel;
    private AdapterContract.View mMemoriesCardAdapterView;

    public MemoriesPresenter(MemoriesContractor.View view) {
        this.view = view;
    }

    @Override
    public void setMemoriesCardAdapterView(MemoriesCardAdapter adapter) {
        this.mMemoriesCardAdapterView = adapter;
    }

    @Override
    public void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter) {
        this.mMemoriesCardAdapterModel = adapter;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onRecommendButtonClicked() {
        view.makeToast("추천하겠습니다.");
    }

    @Override
    public void onDeleteButtonClicked() {
        view.makeToast("삭제버튼이눌렸습니다.");
    }

    @Override
    public void onPlayButtonClicked() {
        view.makeToast("플레이하겠습니다.");
    }

    @Override
    public void loadData() {

        //dummy data
        List<String> diaryList = Arrays.asList("1", "2", "3", "4", "5");
        List<Memory> memories = new ArrayList<>();
        memories.add(new Memory("Happy of January", diaryList));
        memories.add(new Memory("Sad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));
        memories.add(new Memory("Not bad of January", diaryList));

        mMemoriesCardAdapterModel.addItems(memories);
    }

    @Override
    public void onViewDetached() {
        view = null;

    }
}
