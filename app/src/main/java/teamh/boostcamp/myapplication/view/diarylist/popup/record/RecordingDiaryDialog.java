package teamh.boostcamp.myapplication.view.diarylist.popup.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import teamh.boostcamp.myapplication.R;

public class RecordingDiaryDialog extends DialogFragment {

    private static final int LIMIT_TIME = 10;
    private Disposable timerDisposable;
    private OnRecordDialogDismissListener dismissListener;
    private boolean isTimeOut = false;

    public static RecordingDiaryDialog newInstance() {
        return new RecordingDiaryDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_record_diary, null);

        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.law_item_dialog_background);
        lottieAnimationView.playAnimation();

        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(LIMIT_TIME)
                .doOnComplete(() -> {
                    isTimeOut = true;
                    this.dismiss();
                })
                .subscribe(aLong -> Log.d("Test", aLong.toString()), Throwable::printStackTrace);

        builder.setTitle(R.string.recording);
        builder.setView(view);
        builder.setPositiveButton(R.string.popup_dialog_ok, (dialogInterface, i) -> dismiss());

        return builder.create();
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
        dismissListener.onDismiss(isTimeOut);
    }
}
