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

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd", Locale.KOREA);
    private Context context;
    private List<Recall> itemList;
    private ButtonClickListener buttonClickListener;

    RecallListAdapter(Context context) {
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
        holder.binding.tvSubTitleDate.setText(generateRecallTitleDate(itemList.get(position)));
        holder.binding.tvSubTitle.setText(emotionToString(itemList.get(position).getEmotion().getEmotion()));
        holder.binding.rvDiary.setHasFixedSize(true);
        holder.binding.rvDiary.setVerticalScrollbarPosition(0);
        holder.binding.rvDiary.setLayoutManager(new LinearLayoutManager(context));

        DiaryTitleListAdapter adapter = new DiaryTitleListAdapter(context);
        adapter.addItems(itemList.get(position).getDiaryList());
        holder.binding.rvDiary.setAdapter(adapter);

        holder.binding.ivDelete.setOnClickListener(v -> buttonClickListener.onDeleteButtonClicked(position, itemList.get(position).getIndex()));
        holder.binding.ivPlay.setOnClickListener(v -> buttonClickListener.onPlayButtonClicked(itemList.get(position)));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    void updateItems(List<Recall> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    void addItem(Recall recall) {
        this.itemList.add(0, recall);
        notifyItemInserted(0);
    }

    void deleteItem(int position){
        this.itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, itemList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecallListBinding binding;

        ViewHolder(@NonNull ItemRecallListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    private String generateRecallTitleDate(Recall recall) {
        String startDateString = DateToSimpleFormat(recall.getStartDate());
        String endDateString = DateToSimpleFormat(recall.getEndDate());
        return String.format("%s ~ %s", startDateString, endDateString);
    }

    @NonNull
    private String DateToSimpleFormat(@NonNull Date date) {
        return simpleDateFormat.format(date);
    }

    @NonNull
    private String emotionToString(int emotion) {
        switch (emotion) {
            case 0:
                return "불행했던 날들";
            case 1:
                return "슬픈 날들";
            case 2:
                return "그저그런날들";
            case 3:
                return "즐거웠던 날들";
            case 4:
                return "행복했던 날들";
            default:
                return "행복들";
        }
    }

    public interface ButtonClickListener {
        void onPlayButtonClicked(Recall recall);

        void onDeleteButtonClicked(int position, int id);
    }

}
