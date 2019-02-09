package teamh.boostcamp.myapplication.view.memories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.databinding.ItemDiaryBinding;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.view.adapter.BaseRecyclerViewAdapter;

public class MemoriesDiaryAdapter extends BaseRecyclerViewAdapter<Diary, MemoriesDiaryAdapter.ViewHolder> {

    public MemoriesDiaryAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDiaryBinding binding = ItemDiaryBinding
                .inflate(LayoutInflater
                        .from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    protected void onBindView(ViewHolder holder, int position) {
        holder.binding.tvDiaryTitle.setText(itemList.get(position).getRecordDate());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDiaryBinding binding;

        ViewHolder(ItemDiaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
