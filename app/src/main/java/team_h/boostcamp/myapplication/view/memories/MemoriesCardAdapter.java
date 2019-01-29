package team_h.boostcamp.myapplication.view.memories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import team_h.boostcamp.myapplication.databinding.ItemMemoryCardBinding;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.adapter.BaseRecyclerViewAdapter;

public class MemoriesCardAdapter extends BaseRecyclerViewAdapter<Memory, MemoriesCardAdapter.ViewHolder> {

    public MemoriesCardAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemMemoryCardBinding binding = ItemMemoryCardBinding
                .inflate(LayoutInflater
                        .from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    protected void onBindView(ViewHolder holder, int position) {;
        holder.binding.tvSubTitle.setText(itemList.get(position).getTitle());
        holder.binding.rvDiary.setHasFixedSize(true);
        MemoriesDiaryAdapter adapter = new MemoriesDiaryAdapter(getContext());
        holder.binding.rvDiary.setAdapter(adapter);
        holder.binding.rvDiary.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.addItems(itemList.get(position).getMemories());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMemoryCardBinding binding;

        ViewHolder(ItemMemoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
