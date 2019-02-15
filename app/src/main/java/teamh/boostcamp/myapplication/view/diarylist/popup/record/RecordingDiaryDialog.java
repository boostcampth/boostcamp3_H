package teamh.boostcamp.myapplication.view.diarylist.popup.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.R;

public class RecordingDiaryDialog extends DialogFragment {

    private Disposable timerDisposable;

    public static RecordingDiaryDialog newInstance() {
        RecordingDiaryDialog recordingDiaryDialog = new RecordingDiaryDialog();

        return recordingDiaryDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_record_diary, null);

        final TextView restTime = view.findViewById(R.id.tv_record_dialog_time);
        restTime.setText("60초");

        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.lav_record_dialog_anim);
        lottieAnimationView.playAnimation();

        timerDisposable = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .repeat(1000 * 60)
                .doOnDispose(() -> {
                    lottieAnimationView.cancelAnimation();
                }).doOnComplete(() -> {
                    lottieAnimationView.cancelAnimation();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> restTime.setText(aLong.toString())
                        , Throwable::printStackTrace);

        builder.setView(view);
        builder.setPositiveButton(R.string.popup_dialog_ok, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timerDisposable.dispose();
    }
}
