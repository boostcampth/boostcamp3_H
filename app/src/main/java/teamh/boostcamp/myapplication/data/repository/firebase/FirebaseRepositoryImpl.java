package teamh.boostcamp.myapplication.data.repository.firebase;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public class FirebaseRepositoryImpl implements FirebaseRepository {

    private static volatile FirebaseRepository INSTANCE;

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
    public Single<List<String>> loadAllDiaryId() {

        return Single.create(emitter -> {
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
    public Single<List<DiaryEntity>> loadAllDiaryList() {

        return Single.create(emitter -> {

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

            if (diaryEntities.size() == 0) {
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

            if (diaryEntityList.size() == 0) {
                emitter.onSuccess(diaryEntityList);
            }

            final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            final String key = FirebaseAuth.getInstance().getUid();
            final List<String> downloadUrlList = new ArrayList<>();

            Iterator<DiaryEntity> iterator = diaryEntityList.iterator();

            while (iterator.hasNext()) {

                final DiaryEntity currentDiaryEntity = iterator.next();

                File file = new File(currentDiaryEntity.getRecordFilePath());

                if (!file.exists()) {
                    iterator.remove();
                    continue;
                }

                final Uri uri = Uri.fromFile(file);

                final StorageReference storageRef = firebaseStorage.getReference().child((key + "/" + currentDiaryEntity.getId()));

                storageRef.putFile(uri)
                        .continueWithTask(task -> task.isSuccessful() ? storageRef.getDownloadUrl() : null)
                        .addOnSuccessListener(downloadUri -> {
                            if (downloadUri != null) {
                                downloadUrlList.add(downloadUri.toString());
                                currentDiaryEntity.setRecordFilePath(downloadUri.toString());
                            } else {
                                diaryEntityList.remove(currentDiaryEntity);
                            }
                            if (downloadUrlList.size() == diaryEntityList.size()) {
                                emitter.onSuccess(diaryEntityList);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

    @NonNull
    @Override
    public Single<List<DiaryEntity>> downloadRecordFile(@NonNull List<DiaryEntity> diaryEntityList) {
        return Single.create(emitter -> {

            if (diaryEntityList.size() == 0) {
                emitter.onSuccess(diaryEntityList);
            }

            final ObservableInt numOfDownloadFile = new ObservableInt(diaryEntityList.size());
            final int size = diaryEntityList.size();
            final FirebaseStorage storageRef = FirebaseStorage.getInstance();

            File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "diary");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (int i = 0; i < size; ++i) {

                final StorageReference recordRef = storageRef.getReferenceFromUrl(diaryEntityList.get(i).getRecordFilePath());

                diaryEntityList.get(i).setRecordFilePath(dir.getAbsolutePath() + File.separator + diaryEntityList.get(i).getId() + ".acc");

                final File recordFile = new File(diaryEntityList.get(i).getRecordFilePath());

                recordFile.getParentFile().mkdirs();
                recordFile.createNewFile();

                recordRef.getFile(recordFile).addOnSuccessListener(taskSnapshot -> {
                    // 저장이 완료되었으면 저장 count 추가해주기
                    numOfDownloadFile.set(numOfDownloadFile.get() - 1);
                    if (numOfDownloadFile.get() == 0) {
                        emitter.onSuccess(diaryEntityList);
                    }
                }).addOnFailureListener(e -> {
                    Log.d("Test", "Error");
                    e.printStackTrace();
                });
            }
        });
    }

    @NonNull
    @Override
    public Single<DiaryEntity> loadDiaryById(String id) {

        return Single.create(emitter -> {
            final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("user");

            final String key = FirebaseAuth.getInstance().getUid();

            firebaseDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> iterator= dataSnapshot.getChildren().iterator();
                    while(iterator.hasNext()){
                        DataSnapshot it = iterator.next();
                        it.getRef().orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DiaryEntity first=null;
                                Iterator<DataSnapshot> date = dataSnapshot.getChildren().iterator();
                                if(date.hasNext()){
                                    first = date.next().getValue(DiaryEntity.class);
                                }
                                //DiaryEntity diaryEntity = dataSnapshot.getValue(DiaryEntity.class);
                                emitter.onSuccess(first);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if(!emitter.isDisposed()) {
                        emitter.onError(new FirebaseException("find_id"));
                    }
                }
            });

        });
    }
}
