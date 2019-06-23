package AppModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import AppModel.Entity.Activity;
import DataSources.AppRepository;
import DataSources.NetworkDataCallback;

public class RemoteData {

    private FirebaseFirestore firestoreDb;
    private final String TAG = "NetworkDataLog";
    private final String ACTIVITY_COLLECTION_NAME = "activity";
    private final String ACTIVITY_TYPE_COLLECTION_NAME = "activityType";
    private final String CATEGORY_COLLECTION_NAME = "category";
    private final String ORDER_BY_FIELD = "activityStartDateTime";
    private FirebaseAuth mAuth;

    public RemoteData(){
        firestoreDb = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDb.setFirestoreSettings(settings);
//        FirebaseFirestore.setLoggingEnabled(true);
    }

    public void fetchActivities(Date date, NetworkDataCallback.ActivityCallback callback){

        Query query = firestoreDb.collection(ACTIVITY_COLLECTION_NAME)
                .orderBy(ORDER_BY_FIELD).whereGreaterThan(ORDER_BY_FIELD, date)
                .limit(AppRepository.PAGE_SIZE);

        query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        List<Activity> act = result.toObjects(Activity.class);
                        callback.onCallback(act);

                    }
                    else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    public String insertActivity(Activity activity){

        String id = "";
        DocumentReference ref = firestoreDb.collection(ACTIVITY_COLLECTION_NAME).document();
        id = ref.getId();
        activity.setId(id);
        ref.set(activity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + ref.getId());
            }})
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
        return id;
    }

//    public void currentlyLoggedIn(){
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        currentUser.get
//        updateUI(currentUser);
//    }

}
