package teamh.boostcamp.myapplication.view.recall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.databinding.ItemDiarytitleLlistBinding;

public class DiaryTitleListAdapter extends RecyclerView.Adapter<DiaryTitleListAdapter.ViewHolder>{

    @NonNull
    private Context context;
    @NonNull
    private List<Diary> diaryList;

    public DiaryTitleListAdapter(@NonNull Context context) {
        this.context = context;
        this.diaryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDiarytitleLlistBinding binding = ItemDiarytitleLlistBinding.inflate(LayoutInflater.from(context),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        Diary diary = diaryList.get(position);
        holder.binding.tvDiaryTitle.setText(simpleDateFormat.format(diary.getRecordDate()));
        // Fixme Emotion enum 수정후 변경
        holder.binding.tvEmoji.setText(diary.getSelectedEmotion().getEmotion()+"");
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    void addItems(List<Diary> diaryList){
        this.diaryList.clear();
        this.diaryList.addAll(diaryList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemDiarytitleLlistBinding binding;

        ViewHolder(@NonNull ItemDiarytitleLlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
