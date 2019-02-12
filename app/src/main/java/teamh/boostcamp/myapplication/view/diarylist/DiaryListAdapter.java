package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.databinding.ItemRecordDiaryBinding;


public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.DiaryHolder> {

    private List<Diary> diaryList;
    private Context context;
    private OnRecordItemClickListener onRecordItemClickListener;

    DiaryListAdapter(@NonNull Context context) {
        this.context = context;
        this.diaryList = new ArrayList<>();
    }

    void setOnRecordItemClickListener(@NonNull OnRecordItemClickListener onRecordItemClickListener) {
        this.onRecordItemClickListener = onRecordItemClickListener;
    }

    @NonNull
    @Override
    public DiaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemRecordDiaryBinding itemRecordDiaryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_record_diary,
                parent,
                false
        );

        return new DiaryHolder(itemRecordDiaryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryHolder holder, int position) {

        final Diary diary = diaryList.get(position);

        if(onRecordItemClickListener != null) {
            holder.itemRecordDiaryBinding.ivItemDiaryPlay.setOnClickListener(v ->
                    onRecordItemClickListener.onDiaryItemClicked(position)
            );
        }

        holder.itemRecordDiaryBinding.tvItemDiaryEmotion.setText(diary.getSelectedEmotion().getEmoji());
        holder.itemRecordDiaryBinding.tvItemDiaryTags.setText(diary.getTags().toString());

        holder.itemRecordDiaryBinding.setDate(diary.getRecordDate());
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    void addDiaryItems(@NonNull List<Diary> diaries) {
        int from = diaryList.size();
        diaryList.addAll(diaries);
        notifyItemMoved(from, diaryList.size());
    }

    void insertDiaryItem(@NonNull Diary diary) {
        diaryList.add(0,diary);
        notifyItemInserted(0);
    }

    Diary getDiary(final int pos) {
        return diaryList.get(pos);
    }

    class DiaryHolder extends RecyclerView.ViewHolder {

        ItemRecordDiaryBinding itemRecordDiaryBinding;

        DiaryHolder(@NonNull ItemRecordDiaryBinding itemRecordDiaryBinding) {
            super(itemRecordDiaryBinding.getRoot());
            this.itemRecordDiaryBinding = itemRecordDiaryBinding;
        }
    }
}

