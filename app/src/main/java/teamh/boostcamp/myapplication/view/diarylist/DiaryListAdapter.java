package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.databinding.ItemRecordDiaryBinding;


public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.DiaryHolder> {

    private static final int NOTHING_PLAYED = -1;

    private List<Diary> diaryList;
    private Context context;
    private OnRecordItemClickListener onRecordItemClickListener;
    private int lastPlayedIndex = NOTHING_PLAYED;

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
                onRecordItemClickListener.onDiaryItemClicked(position));
        }

        if(lastPlayedIndex == position) {
            holder.itemRecordDiaryBinding.lawItemDiaryPercent.playAnimation();
            holder.itemRecordDiaryBinding.ivItemDiaryPlay.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_pause_circle_filled_black_24dp));
        } else {
            holder.itemRecordDiaryBinding.lawItemDiaryPercent.cancelAnimation();
            holder.itemRecordDiaryBinding.lawItemDiaryPercent.setProgress(0);
            holder.itemRecordDiaryBinding.ivItemDiaryPlay.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_play_circle_filled_black_24dp));
        }

        holder.itemRecordDiaryBinding.tvItemDiaryEmotion.setText(diary.getSelectedEmotion().getEmoji());
        holder.itemRecordDiaryBinding.tvItemDiaryTags.setText(diary.toString());

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

    void changePlayItemIcon(final int lastPlayedIndex, final boolean isFinished) {
        if(isFinished) {
            this.lastPlayedIndex = NOTHING_PLAYED;
        } else {
            this.lastPlayedIndex = lastPlayedIndex;
        }
        notifyItemChanged(lastPlayedIndex);
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

