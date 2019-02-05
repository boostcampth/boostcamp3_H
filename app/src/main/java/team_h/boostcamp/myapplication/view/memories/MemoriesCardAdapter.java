package team_h.boostcamp.myapplication.view.memories;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.databinding.ItemMemoryCardBinding;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.adapter.BaseRecyclerViewAdapter;

public class MemoriesCardAdapter extends BaseRecyclerViewAdapter<Memory, MemoriesCardAdapter.ViewHolder> {
    private ViewClickListener mListener;
    private static final String TAG = "MemoriesCardAdapter";

    public interface ViewClickListener {

        void onPlayButtonClicked(int position);

        void onCloseButtonLicked(int position);
    }

    MemoriesCardAdapter(Context context) {
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

    void setOnClickListener(ViewClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onBindView(ViewHolder holder, int position) {
        holder.binding.tvSubTitle.setText(itemList.get(position).getTitle());
        holder.binding.rvDiary.setHasFixedSize(true);
        MemoriesDiaryAdapter adapter = new MemoriesDiaryAdapter(getContext());
        holder.binding.rvDiary.setLayoutManager(new LinearLayoutManager(getContext()));
        holder.binding.rvDiary.setAdapter(adapter);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());

        appDatabase.appDao().loadSelectedDiaryList(itemList.get(position).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendations -> {

                    List<Diary> list = new ArrayList<>();
                    for (int i = 0; i < recommendations.size(); i++) {
                        appDatabase.appDao().loadSelectedDiary(recommendations.get(i).getDiaryId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diary -> {
                                    Log.d(TAG, "onBindView: " + diary.getId());
                                    adapter.addItem(diary);
                                });
                    }
                    holder.binding.rvDiary.setAdapter(adapter);
                });

        if (mListener != null) {
            holder.binding.ivPlay.setOnClickListener(v -> mListener.onPlayButtonClicked(position));

            holder.binding.ivClose.setOnClickListener(v -> mListener.onCloseButtonLicked(position));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemMemoryCardBinding binding;

        ViewHolder(ItemMemoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
