package teamh.boostcamp.myapplication.view.diarylist.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Emotion;

public class AnalyzedEmotionShowingDialog extends DialogFragment{

    private static final String EXTRA_ANALYZED_EMOTION = "ANALYZED_EMOTION";

    private Emotion analyzedEmotion;

    @NonNull
    public static AnalyzedEmotionShowingDialog getInstance(@NonNull Emotion analyzedEmotion) {
        AnalyzedEmotionShowingDialog dialog = new AnalyzedEmotionShowingDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ANALYZED_EMOTION, analyzedEmotion);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            analyzedEmotion = (Emotion) getArguments().getSerializable(EXTRA_ANALYZED_EMOTION);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_analyzed_emotion_showing,null);

        TextView emotionView = view.findViewById(R.id.tv_emotion_dialog_analyzed);
        emotionView.setText(analyzedEmotion.getEmoji());

        TextView relationView = view.findViewById(R.id.tv_emotion_dialog_relation);
        relationView.setText(analyzedEmotion.getRelation());

        builder.setView(view);
        builder.setTitle(getString(R.string.popup_dialog_title));
        builder.setPositiveButton(R.string.popup_dialog_ok, (dialogInterface, which) -> {
            dismiss();
        });

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.AnalyzedEmotionShowingDialogAnimation;

        return dialog;
    }
}
