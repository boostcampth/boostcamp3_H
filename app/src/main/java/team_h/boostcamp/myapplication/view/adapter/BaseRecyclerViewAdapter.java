package team_h.boostcamp.myapplication.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
 * 공통적인 부분을  */
public abstract class BaseRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterContract.View, AdapterContract.Model<T> {

    protected List<T> itemList;
    protected OnItemClickListener onItemClickListener;
    protected Context context;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (this.itemList == null) {
            return 0;
        }

        return this.itemList.size();
    }

    /**
     * 해당 position 의 item 반환
     */
    @Override
    public T getItem(int position) {
        if (this.itemList == null) {
            return null;
        }

        return this.itemList.get(position);
    }

    /**
     * 전체 item list 반환
     */
    @Override
    public List<T> getItemList() {
        if (this.itemList == null) {
            return null;
        }

        return this.itemList;
    }

    /**
     * item list 전체 수정
     */
    public void updateItems(List<T> items) {
        if (this.itemList == null) {
            itemList = new ArrayList<>();
        }
        this.itemList.clear();
        this.itemList.addAll(items);

        notifyDataSetChanged();
    }

    /**
     * 해당 position 의 item 수정
     */
    public void updateItem(int position, T item) {
        if (this.itemList == null) {
            return;
        }
        if (position > this.itemList.size()) {
            return;
        }
        this.itemList.remove(position);
        this.itemList.add(position, item);

        notifyItemChanged(position);
    }

    /**
     * 맨 처음 item list 초기화 또는 ,
     * item list 마지막 position 뒤에 추가
     */
    @Override
    public void addItems(List<T> items) {
        if (this.itemList == null) {
            this.itemList = items;
            notifyDataSetChanged();
        } else {
            int position = this.itemList.size();
            this.itemList.addAll(items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    /**
     * position 위치에 items 추가
     */
    public void addItems(int position, List<T> items) {
        if (this.itemList == null) {
            this.itemList = new ArrayList<>();
        }
        if (position > this.itemList.size()) {
            return;
        }
        this.itemList.addAll(position, items);

        notifyItemRangeInserted(position, items.size());
    }

    /**
     * item list 마지막 position 뒤에 item 추가
     */
    @Override
    public void addItem(T item) {
        if (this.itemList == null) {
            this.itemList = new ArrayList<>();
            itemList.add(item);
            notifyDataSetChanged();
        } else {
            int position = this.itemList.size();
            this.itemList.add(item);
            notifyItemInserted(position);
        }
    }

    /**
     * position 위치에 item 추가
     */
    public void addItem(int position, T item) {
        if (this.itemList == null) {
            return;
        }
        if (position > this.itemList.size()) {
            return;
        }
        this.itemList.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * item list 전체 삭제
     */
    @Override
    public void clearItems() {
        if (itemList != null) {
            itemList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * position 위치의 item 삭제
     */
    @Override
    public void removeItem(int position) {
        if (this.itemList != null && position < this.itemList.size()) {
            this.itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.itemList.size());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(view -> {

            // item click listener 등록
            if (onItemClickListener != null) {
                onItemClickListener.onClickItem(position);
            }

        });

        onBindView((H) holder, position);
    }

    // 각 ViewHolder 에 맞게 Item 을 설정해주는 역할
    protected abstract void onBindView(H holder, int position);
}
