package teamh.boostcamp.myapplication.view.diarylist.popup.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import teamh.boostcamp.myapplication.R;

public class RecordingDiaryDialog extends DialogFragment {

    private static final int LIMIT_TIME = 60;
    private Disposable timerDisposable;
    private OnRecordDialogDismissListener dismissListener;
    private boolean isTimeOut = false;
    private boolean isViewPopUp = false;

    public static RecordingDiaryDialog newInstance() {
        return new RecordingDiaryDialog();
    }

    @NonNull
    @Override
    @SuppressWarnings("cast")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.PopUpDialogTheme);

        isViewPopUp = true;

        if(getActivity() != null) {
            final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_record_diary, null);

            final ProgressBar progressBar = view.findViewById(R.id.pb_item_dialog_progress);
            final TextView timerTextView = view.findViewById(R.id.tv_item_dialog_timer);

            timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(LIMIT_TIME + 1)
                    .doOnComplete(() -> {
                        isTimeOut = true;
                        this.dismiss();
                    })
                    .subscribe(aLong -> {
                        progressBar.setProgress(aLong.intValue());
                        timerTextView.setText(String.format(Locale.getDefault(),"%d초",LIMIT_TIME - aLong.intValue()));
                    }, Throwable::printStackTrace);

            setCancelable(false);
            builder.setTitle(R.string.recording);
            builder.setView(view);
            builder.setPositiveButton(R.string.popup_dialog_record_finish, (dialogInterface, i) -> dismiss());
        }

        Dialog dialog = builder.create();
        if(dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnalyzedEmotionShowingDialogAnimation;
        }

        return dialog;
    }

    public void setDismissListener(OnRecordDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (!timerDisposable.isDisposed()) {
            timerDisposable.dispose();
            timerDisposable = null;
        }
        isViewPopUp = false;
        dismissListener.onDismiss(isTimeOut);
    }

    public boolean isViewPopUp() {
        return isViewPopUp;
    }
}
