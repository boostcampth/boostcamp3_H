package team_h.boostcamp.myapplication.view.memories;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;

public class MemoriesPresenter implements MemoriesContractor.Presenter {

    private MemoriesContractor.View view;
    private AdapterContract.Model<Memory> mMemoriesCardAdapterModel;
    private AdapterContract.View mMemoriesCardAdapterView;
    List<Memory> memories = new ArrayList<>();

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
    public void onRecommendButtonClicked(View v) {
        view.makeToast("추천하겠습니다.");

        if(!isRecommended()){
            Memory newMemory = generateMemory();
            mMemoriesCardAdapterModel.addItem(newMemory);
        }
    }

    private Memory generateMemory() {
        view.makeToast("선택된 감정" + generateRandomNumber());
        Memory memory = new Memory(1,"Happy of January","1d일",1);
        return memory;
    }

    private boolean isRecommended() {
        // memories 테이블에서 최신추가된 추천 튜플과 오늘 날짜 비교
        return false;
    }

    @Override
    public void onDeleteButtonClicked(int position) {
        view.makeToast("삭제버튼이눌렸습니다.");
        memories.remove(position);
        mMemoriesCardAdapterModel.removeItem(position);
    }

    @Override
    public void onPlayButtonClicked(int position) {
        view.makeToast("플레이하겠습니다.");
        view.navigateToPlayActivity(memories.get(position));
    }

    @Override
    public void loadData() {

        //dummy data

        List<String> diaryList = Arrays.asList("1", "2", "3", "4", "5");
        memories.add(new Memory(1,"Happy of January","1d일",1));
        mMemoriesCardAdapterModel.addItems(memories);
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    public int generateRandomNumber(){
        return (int)(Math.random()*5);
    }
}
