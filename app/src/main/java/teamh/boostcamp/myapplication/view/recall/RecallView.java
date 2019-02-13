package teamh.boostcamp.myapplication.view.recall;

import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Recall;

public interface RecallView {

    void addRecallList(@NonNull List<Recall> recallList);

    void addRecall(@NonNull Recall recall);

    void onGenerateNewRecallButtonClicked(View view);

    void showDeleteSuccessResult();

    void deleteRecall(int position);
}