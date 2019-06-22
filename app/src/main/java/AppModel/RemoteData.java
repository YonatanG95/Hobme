package AppModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import AppModel.Entity.Activity;
import DataSources.AppRepository;
import DataSources.NetworkDataCallback;

public class RemoteData {

    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private final String TAG = "NetworkDataLog";
    private final String ACTIVITY_COLLECTION_NAME = "activity";
    private final String ACTIVITY_TYPE_COLLECTION_NAME = "activityType";
    private final String CATEGORY_COLLECTION_NAME = "category";
    private final String ORDER_BY_FIELD = "activityStartDateTime";

    public void fetchActivities(Date date, NetworkDataCallback callback){

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

//    public void firebaseEmailPass(String email, String pass){
//
//    }
}
