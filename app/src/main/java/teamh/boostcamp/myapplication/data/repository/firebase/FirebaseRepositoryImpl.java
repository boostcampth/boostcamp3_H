package teamh.boostcamp.myapplication.data.repository.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;

public class FirebaseRepositoryImpl implements FirebaseRepository {

    private static FirebaseRepository INSTANCE;

    private FirebaseRepositoryImpl() {
    }

    @NonNull
    public static FirebaseRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (FirebaseRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Maybe<List<String>> loadAllDiaryId() {

        return Maybe.create(emitter -> {
            final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("user");

            final String key = FirebaseAuth.getInstance().getUid();

            firebaseDatabase.child(key)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> ids = new ArrayList<>();
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            while (iterator.hasNext()) {
                                Iterator<DataSnapshot> backupList = iterator.next().getChildren().iterator();
                                while (backupList.hasNext()) {
                                    ids.add(backupList.next().getValue(DiaryEntity.class).getId());
                                }
                            }
                            emitter.onSuccess(ids);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new FirebaseException("LoadAllDiaryListError"));
                            }
                        }
                    });
        });
    }

    @NonNull
    @Override
    public Maybe<List<DiaryEntity>> loadAllDiaryList() {

        return Maybe.create(emitter -> {

            final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("user");

            final String key = FirebaseAuth.getInstance().getUid();

            firebaseDatabase.child(key)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<DiaryEntity> diaryEntityList = new ArrayList<>();
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            while (iterator.hasNext()) {
                                Iterator<DataSnapshot> loadList = iterator.next().getChildren().iterator();
                                while (loadList.hasNext()) {
                                    diaryEntityList.add(loadList.next().getValue(DiaryEntity.class));
                                }
                            }
                            emitter.onSuccess(diaryEntityList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new FirebaseException("LoadAllDiaryListError"));
                            }
                        }
                    });
        });
    }

    @NonNull
    @Override
    public Completable insertDiaries(@NonNull List<DiaryEntity> diaryEntities) {

        return Completable.create(emitter -> {

            if(diaryEntities.size() == 0) {
                emitter.onComplete();
            }

            final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("user");

            final String key = FirebaseAuth.getInstance().getUid();
            final String newPushKey = FirebaseDatabase.getInstance().getReference().child(key).push().getKey();

            firebaseDatabase.child(key)
                    .child(newPushKey)
                    .setValue(diaryEntities)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else if (!emitter.isDisposed()) {
                            emitter.onError(new FirebaseException("InsertDiaries Error"));
                        }
                    });
        });
    }

    @NonNull
    @Override
    public Single<List<DiaryEntity>> uploadRecordFile(@NonNull List<DiaryEntity> diaryEntityList) {
        return Single.create(emitter -> {

            if(diaryEntityList.size() == 0) {
                emitter.onSuccess(diaryEntityList);
            }

            final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            final String key = FirebaseAuth.getInstance().getUid();
            final int size = diaryEntityList.size();
            final List<String> downloadUrlList = new ArrayList<>();

            for (int i = 0; i < size; ++i) {
                final Uri uri = Uri.fromFile(new File(diaryEntityList.get(i).getRecordFilePath()));
                final StorageReference storageRef = firebaseStorage.getReference().child((key + "/" + diaryEntityList.get(i).getId()));
                final int currentPos = i;

                storageRef.putFile(uri)
                        .continueWithTask(task -> task.isSuccessful() ? storageRef.getDownloadUrl() : null)
                        .addOnSuccessListener(downloadUri -> {
                            Log.d("Test", currentPos + "");
                            if(downloadUri != null) {
                                downloadUrlList.add(downloadUri.toString());
                                diaryEntityList.get(currentPos).setRecordFilePath(downloadUri.toString());
                            } else {
                                diaryEntityList.remove(currentPos);
                            }
                            if(downloadUrlList.size() == diaryEntityList.size()) {
                                emitter.onSuccess(diaryEntityList);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if(!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

    @NonNull
    @Override
    public Single<List<Uri>> downloadRecordFile() {
        return null;
    }
}
