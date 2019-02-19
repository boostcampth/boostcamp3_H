package teamh.boostcamp.myapplication.view.setting;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DeleteDiaryFileWorker extends Worker {
    public DeleteDiaryFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        File diaryDirectory = new File(String.format("%s/diary", Environment.getExternalStorageDirectory().getAbsolutePath()));
        File[] diaryFileList = diaryDirectory.listFiles();

        if (diaryDirectory.exists()) {
            for (File diaryFile : diaryFileList) {
                diaryFile.delete();
            }

            if (diaryDirectory.isDirectory() && diaryDirectory.listFiles().length == 0) {
                diaryDirectory.delete();
            }
        }

        return Result.success();
    }
}
