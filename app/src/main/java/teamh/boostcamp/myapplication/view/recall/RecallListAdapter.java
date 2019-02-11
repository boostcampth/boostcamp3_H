package teamh.boostcamp.myapplication.view.recall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.data.model.Recall;
import teamh.boostcamp.myapplication.databinding.ItemRecallListBinding;

public class RecallListAdapter extends RecyclerView.Adapter<RecallListAdapter.ViewHolder> {
    @NonNull
    private Context context;
    @NonNull
    private List<Recall> itemList;

    public RecallListAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecallListBinding binding = ItemRecallListBinding.inflate(LayoutInflater.from(context),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvSubTitle.setText(generateRecallTitle(itemList.get(position)));
        holder.binding.rvDiary.setHasFixedSize(true);
        holder.binding.rvDiary.setVerticalScrollbarPosition(0);
        holder.binding.rvDiary.setLayoutManager(new LinearLayoutManager(context));

        DiaryTitleListAdapter adapter = new DiaryTitleListAdapter(context);
        adapter.addItems(itemList.get(position).getDiaryList());
        holder.binding.rvDiary.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateItems(List<Recall> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecallListBinding binding;

        ViewHolder(@NonNull ItemRecallListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    private String generateRecallTitle(@NonNull Recall recall) {
        String startDateString = DateToSimpleFormat(recall.getStartDate());
        String endDateString = DateToSimpleFormat(recall.getEndDate());
        String emotionString = emotionToString(recall.getEmotion().getEmotion());
        return String.format("%s 부터 %s까지의 %s", startDateString, endDateString, emotionString);
    }

    @NonNull
    private String DateToSimpleFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
        return simpleDateFormat.format(date);
    }

    @NonNull
    private String emotionToString(int emotion) {
        switch (emotion) {
            case 0:
                return "불행함들";
            case 1:
                return "슬픔들";
            case 2:
                return "그저그런날들";
            case 3:
                return "즐거움들";
            case 4:
                return "행복들";
            default:
                return "행복들";
        }
    }

}
