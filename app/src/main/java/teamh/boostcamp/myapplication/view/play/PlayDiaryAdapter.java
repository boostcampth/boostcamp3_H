package teamh.boostcamp.myapplication.view.play;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.databinding.ItemDiarytitleLlistBinding;
import teamh.boostcamp.myapplication.utils.BindingUtil;

public class PlayDiaryAdapter extends RecyclerView.Adapter<PlayDiaryAdapter.ViewHolder> {

    private Context context;
    private List<LegacyDiary> itemList;

    PlayDiaryAdapter(Context context) {
        this.context = context;
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
        LegacyDiary diary = itemList.get(position);
    }

    @Override
    public int getItemCount() {
        if (this.itemList == null){
            this.itemList = new ArrayList<>();
        }

        return this.itemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemDiarytitleLlistBinding binding;

        ViewHolder(ItemDiarytitleLlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    void addItems(List<LegacyDiary> items) {
        if( this.itemList == null){
            this.itemList = items;
            notifyDataSetChanged();
        } else{
            int position = this.itemList.size();
            this.itemList.addAll(items);
            notifyItemRangeInserted(position, items.size());
        }
    }
}
