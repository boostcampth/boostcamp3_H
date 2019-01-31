package team_h.boostcamp.myapplication.view.diaryList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ItemHashTagBinding;
import team_h.boostcamp.myapplication.view.adapter.BaseRecyclerViewAdapter;

public class HashTagListAdapter extends BaseRecyclerViewAdapter<String, HashTagListAdapter.TagHolder>{

    private static final int MAX_TAG_NUM = 5;

    HashTagListAdapter(Context context) {
        super(context);
        itemList = new ArrayList<>();
    }

    @Override
    protected void onBindView(TagHolder holder, int position) {
        // item 에 Data 설정
        holder.itemHashTagBinding.textViewItemTagTitle.setText(itemList.get(position));
        // item remove event 설정
        holder.itemHashTagBinding.btnItemTagRemove.setOnClickListener(view -> removeItem(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHashTagBinding itemHashTagBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_hash_tag,
                parent,
                false
        );
        return new TagHolder(itemHashTagBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);
        TagHolder tagHolder = (TagHolder) holder;

        tagHolder.itemHashTagBinding.btnItemTagRemove.setOnClickListener(view -> {
            if(onItemClickListener != null) {
                onItemClickListener.onClickItem(position);
            }
        });

        onBindView(tagHolder, position);
    }

    @Override
    public void addItem(String item) {
        if(itemList.size() < MAX_TAG_NUM) {
            super.addItem(item);
        } else {
            itemList.remove(0);
            itemList.add(item);
        }
    }

    class TagHolder extends RecyclerView.ViewHolder {

        public ItemHashTagBinding itemHashTagBinding;

        TagHolder(ItemHashTagBinding itemHashTagBinding) {
            super(itemHashTagBinding.getRoot());
            this.itemHashTagBinding = itemHashTagBinding;
        }
    }
}
