package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ItemHashTagBinding;

public class HashTagListAdapter extends RecyclerView.Adapter<HashTagListAdapter.TagHolder> {

    private static final int MAX_TAG_NUM = 5;

    private List<String> itemList;
    private Context context;
    private OnHashTagItemClickListener onHashTagItemClickListener;


    HashTagListAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
    }

    void setItemClickListener(@NonNull OnHashTagItemClickListener onHashTagItemClickListener) {
        this.onHashTagItemClickListener = onHashTagItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, final int position) {
        holder.itemHashTagBinding.textViewItemTagTitle.setText(itemList.get(position));
        if (onHashTagItemClickListener != null) {
            holder.itemHashTagBinding.btnItemTagRemove.setOnClickListener(view -> onHashTagItemClickListener.onClick(itemList.get(position)));
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
    String getTags() {
        String result = "";
        int size = itemList.size();
        if (size >= 1) {
            result = itemList.get(0).substring(1);
        }
        for (int i = 1; i < size; ++i) {
            result += itemList.get(i);
        }
        return result;
    }


    public void removeItem(String hashTag) {
        Iterator<String> it = itemList.iterator();
        int pos = 0;
        while (it.hasNext()) {
            String temp = it.next();
            if (temp.equals(hashTag)) {
                it.remove();
                notifyDataSetChanged();
                return;
            }
            pos++;
        }
    }

    void clearItems() {
        if (itemList == null) {
            itemList = new ArrayList<>();
        } else {
            itemList.clear();
            notifyDataSetChanged();
        }
    }

    void addItem(@NonNull String item) {
        if (item.equals("#")) {
            return;
        }
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        Iterator<String> it = itemList.iterator();
        while (it.hasNext()) {
            if (it.next().equals(item)) {
                return;
            }
        }
        if (itemList.size() >= MAX_TAG_NUM) {
            removeItem(itemList.get(0));
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
