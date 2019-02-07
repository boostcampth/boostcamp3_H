package team_h.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ItemHashTagBinding;
import team_h.boostcamp.myapplication.view.adapter.OnItemClickListener;

public class HashTagListAdapter extends RecyclerView.Adapter<HashTagListAdapter.TagHolder> {

    private static final int MAX_TAG_NUM = 5;

    private List<String> itemList;
    private Context context;
    private OnItemClickListener itemClickListener;


    HashTagListAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
    }

    void setItemClickListener(@NonNull OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, final int position) {
        holder.itemHashTagBinding.textViewItemTagTitle.setText(itemList.get(position));
        if (itemClickListener != null) {
            holder.itemHashTagBinding.btnItemTagRemove.setOnClickListener(view -> itemClickListener.onClickItem(position));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        ItemHashTagBinding itemHashTagBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_hash_tag,
                parent,
                false
        );
        return new TagHolder(itemHashTagBinding);
    }

    @NonNull
    List<String> getItemList() {
        return itemList;
    }


    void removeItem(int position) {
        if (itemList != null && itemList.size() < position) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    void addItem(@NonNull String item) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        // 태그는 최대 5개까지
        if (itemList.size() == MAX_TAG_NUM) {
            removeItem(0);
        }
        // 아이템 뒤에 추가하기
        int size = this.itemList.size();
        this.itemList.add(item);
        notifyItemInserted(size);
    }

    class TagHolder extends RecyclerView.ViewHolder {

        ItemHashTagBinding itemHashTagBinding;

        TagHolder(@NonNull ItemHashTagBinding itemHashTagBinding) {
            super(itemHashTagBinding.getRoot());
            this.itemHashTagBinding = itemHashTagBinding;
        }
    }
}
