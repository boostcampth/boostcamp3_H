package teamh.boostcamp.myapplication.data.repository.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public class FirebaseRepositoryImpl implements FirebaseRepository {

    private static FirebaseRepository INSTANCE;

    private FirebaseRepositoryImpl() {
    }

    @NonNull
    public static FirebaseRepository getInstance() {
        if(INSTANCE == null) {
            synchronized (FirebaseRepositoryImpl.class) {
                if(INSTANCE == null) {
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
            final String key = FirebaseAuth.getInstance().getUid();

            FirebaseDatabase.getInstance().getReference("user")
                    .child(key)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                List<String> ids = new ArrayList<>();
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()) {
                                    ids.add(iterator.next().getValue(DiaryEntity.class).getId());
                                }
                                emitter.onSuccess(ids);
                            } else {
                                emitter.onComplete();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            if(!emitter.isDisposed()) {
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
            final String key = FirebaseAuth.getInstance().getUid();

            FirebaseDatabase.getInstance().getReference("user")
                    .child(key)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                List<DiaryEntity> diaryEntityList = new ArrayList<>();
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()) {
                                    diaryEntityList.add(iterator.next().getValue(DiaryEntity.class));
                                }
                                emitter.onSuccess(diaryEntityList);
                            } else {
                                emitter.onComplete();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            if(!emitter.isDisposed()) {
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

            final String key = FirebaseAuth.getInstance().getUid();

            FirebaseDatabase.getInstance().getReference("user")
                    .child(key)
                    .setValue(diaryEntities)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else if(!emitter.isDisposed()){
                            emitter.onError(new FirebaseException("InsertDiaries Error"));
                        }
                    });
        });
    }
}
