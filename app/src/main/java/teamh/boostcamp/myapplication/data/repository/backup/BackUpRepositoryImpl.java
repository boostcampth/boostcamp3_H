package teamh.boostcamp.myapplication.data.repository.backup;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposables;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public class BackUpRepositoryImpl implements BackUpRepository {

    private static volatile BackUpRepository INSTANCE;

    @NonNull
    private final FirebaseDatabase firebaseDatabase;
    @NonNull
    private final FirebaseStorage firebaseStorage;
    @NonNull
    private final FirebaseAuth firebaseAuth;
    @NonNull
    private final DiaryDao diaryDao;

    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "diary";

    private BackUpRepositoryImpl(@NonNull FirebaseDatabase firebaseDatabase,
                                 @NonNull FirebaseStorage firebaseStorage,
                                 @NonNull FirebaseAuth firebaseAuth,
                                 @NonNull DiaryDao diaryDao) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseStorage = firebaseStorage;
        this.firebaseAuth = firebaseAuth;
        this.diaryDao = diaryDao;
    }

    @NonNull
    public static BackUpRepository getInstance(@NonNull FirebaseDatabase firebaseDatabase,
                                               @NonNull FirebaseStorage firebaseStorage,
                                               @NonNull FirebaseAuth firebaseAuth,
                                               @NonNull DiaryDao diaryDao) {
        if (INSTANCE == null) {
            synchronized (BackUpRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BackUpRepositoryImpl(firebaseDatabase,
                            firebaseStorage,
                            firebaseAuth,
                            diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<String>> loadAllDiaryId() {

        return loadAllDiaryList().map(diaryEntityList -> {
            List<String> idList = new ArrayList<>();
            for (DiaryEntity diaryEntity : diaryEntityList) {
                idList.add(diaryEntity.getId());
            }
            return idList;
        });
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public Single<List<DiaryEntity>> loadAllDiaryList() {
        return Single.create(emitter -> {

            final DatabaseReference dbRef = firebaseDatabase.getReference("user");
            final String key = firebaseAuth.getUid();

            dbRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<DiaryEntity> diaryEntityList = new ArrayList<>();
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        diaryEntityList.add(iterator.next().getValue(DiaryEntity.class));
                    }
                    emitter.onSuccess(diaryEntityList);
                    /*
                    List<DiaryEntity> idList = Observable.fromIterable(dataSnapshot.getChildren())
                            .flatMap(diaryListSnapShot -> Observable.fromIterable(diaryListSnapShot.getChildren()))
                            .filter(diarySnapShot -> diarySnapShot != null)
                            .map(diaryData -> diaryData.getValue(DiaryEntity.class))
                            .toList()
                            .doOnError(emitter::onError)
                            .blockingGet();

                    emitter.onSuccess(idList);*/
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
    public Completable insertDiary(@NonNull DiaryEntity diaryEntity) {

        return Completable.create(emitter -> {

            final DatabaseReference dbRef = firebaseDatabase.getReference("user");

            final String key = firebaseAuth.getUid();
            final String newPushKey = dbRef.child(key).push().getKey();

            dbRef.child(key).child(newPushKey)
                    .setValue(diaryEntity)
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
    public Observable<DiaryEntity> uploadSingleRecordFile(@NonNull DiaryEntity diaryEntity) {
        return Observable.create(emitter -> {

            final String key = firebaseAuth.getUid();

            final Uri uri = Uri.fromFile(new File(diaryEntity.getRecordFilePath()));
            final StorageReference storeRef = firebaseStorage.getReference()
                    .child(key + "/" + diaryEntity.getId());

            final OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener = taskSnapshot -> {
                diaryEntity.setRecordFilePath(taskSnapshot.getStorage().getDownloadUrl().toString());
                emitter.onNext(diaryEntity);
            };
            final OnFailureListener onFailureListener = emitter::onError;

            final UploadTask uploadTask = storeRef.putFile(uri);

            uploadTask.addOnSuccessListener(onSuccessListener);
            uploadTask.addOnFailureListener(onFailureListener);

            emitter.setDisposable(Disposables.fromAction(() -> {
                uploadTask.addOnSuccessListener(onSuccessListener);
                uploadTask.removeOnFailureListener(onFailureListener);
            }));
        });
    }

    @NonNull
    @Override
    public Observable<DiaryEntity> downloadSingleRecordFile(@NonNull DiaryEntity diaryEntity) {
        return Observable.create(emitter -> {

            final OnSuccessListener<FileDownloadTask.TaskSnapshot> onSuccessListener = taskSnapshot -> {
                Log.d("Test", "DownloadSuccess" + diaryEntity.getId());
                emitter.onNext(diaryEntity);
            };
            final OnFailureListener onFailureListener = e -> {
                Log.d("Test", "DownloadFail" + diaryEntity.getId());
                e.printStackTrace();
                emitter.onComplete();
            };

            final StorageReference recordRef = firebaseStorage.getReference()
                    .child(firebaseAuth.getUid())
                    .child(diaryEntity.getId());

            diaryEntity.setRecordFilePath(DOWNLOAD_PATH + File.separator + diaryEntity.getId() + ".acc");

            final File recordFile = new File(diaryEntity.getRecordFilePath());
            recordFile.getParentFile().mkdirs();
            recordFile.createNewFile();

            FileDownloadTask downloadTask = recordRef.getFile(recordFile);
            downloadTask.addOnSuccessListener(onSuccessListener);
            downloadTask.addOnFailureListener(onFailureListener);

            emitter.setDisposable(Disposables.fromAction(() -> {
                downloadTask.removeOnSuccessListener(onSuccessListener);
                downloadTask.removeOnFailureListener(onFailureListener);
            }));
        });
    }

    @NonNull
    @Override
    public Single<DiaryEntity> loadDiaryById(String id) {

        return Single.create(emitter -> {
            final DatabaseReference dbRef = firebaseDatabase.getReference("user");

            final String key = firebaseAuth.getUid();

            dbRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot it = iterator.next();
                        it.getRef().orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DiaryEntity first = null;
                                Iterator<DataSnapshot> date = dataSnapshot.getChildren().iterator();
                                if (date.hasNext()) {
                                    first = date.next().getValue(DiaryEntity.class);
                                }
                                //DiaryEntity diaryEntity = dataSnapshot.getValue(DiaryEntity.class);
                                emitter.onSuccess(first);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(new FirebaseException("find_id"));
                    }
                }
            });

        });
    }
}
