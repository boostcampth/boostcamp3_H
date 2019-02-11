package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.databinding.ItemRecordDiaryBinding;

public class DiaryListAdapter extends ListAdapter<Diary, DiaryListAdapter.DiaryViewHolder> {

    @NonNull
    private Context context;
    @NonNull
    private List<Diary> diaryList;
    @NonNull
    private OnRecordItemClickListener onRecordItemClickListener;

    DiaryListAdapter(@NonNull Context context) {
        super(DiaryListAdapter.DIARY_ITEM_CALLBACK);
        this.context = context;
        this.diaryList = new ArrayList<>();
    }

    public void setOnRecordItemClickListener(@NonNull OnRecordItemClickListener onRecordItemClickListener){
        this.onRecordItemClickListener = onRecordItemClickListener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecordDiaryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_record_diary,
                parent,
                false
        );

        return new DiaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {

        final Diary diary = diaryList.get(position);

        if(onRecordItemClickListener != null) {
            holder.itemRecordDiaryBinding.ivItemDiaryPlay.setOnClickListener(v -> {
                onRecordItemClickListener.onDiaryItemClicked(diary.getRecordFilePath());
            });
        }

        holder.itemRecordDiaryBinding.setDiary(diary);
    }

    void addDiaryList(@NonNull List<Diary> diaryList) {
        // FIXME 데이터 집어넣는 방식 수정하기
        this.diaryList.addAll(diaryList);
        submitList(this.diaryList);
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {

        ItemRecordDiaryBinding itemRecordDiaryBinding;

        DiaryViewHolder(@NonNull ItemRecordDiaryBinding itemRecordDiaryBinding) {
            super(itemRecordDiaryBinding.getRoot());
            this.itemRecordDiaryBinding = itemRecordDiaryBinding;
        }
    }

    @NonNull
    private static final DiffUtil.ItemCallback<Diary> DIARY_ITEM_CALLBACK = new DiffUtil.ItemCallback<Diary>() {
        @Override
        public boolean areItemsTheSame(@NonNull Diary oldItem, @NonNull Diary newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Diary oldItem, @NonNull Diary newItem) {
            return oldItem.equals(newItem);
        }
    };
}
