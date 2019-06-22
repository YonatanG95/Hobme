package DataSources;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import AppModel.Entity.Activity;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import androidx.annotation.NonNull;

import java.util.List;


public class NetworkData{

    private static NetworkData instance;
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    private final String TAG = "NetworkDataLog";
    private final String ACTIVITY_COLLECTION_NAME = "activity";
    private final String ACTIVITY_TYPE_COLLECTION_NAME = "activityType";
    private final String CATEGORY_COLLECTION_NAME = "category";
    private final String ORDER_BY_FIELD = "activityStartDateTime";

    public static synchronized NetworkData getInstance(){
        if(instance == null){
            instance = new NetworkData();
        }
        return instance;
    }

    private NetworkData(){ }

    String insertActivity(Activity activity){

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

    String insertActivityType(ActivityType activityType, String categoryName){
        String id = "";

        DocumentReference ref = firestoreDb.collection(ACTIVITY_TYPE_COLLECTION_NAME).document();
        id = ref.getId();
        activityType.setId(id);

        //Get category ID by name and insert to activityType
        firestoreDb.collection(CATEGORY_COLLECTION_NAME)
                .whereEqualTo("categoryName", categoryName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            activityType.setCategoryId(document.getString("id"));

                            //Create new activityType with category ID
                            ref.set(activityType).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot activityType added with ID: " + ref.getId());
                                }})
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document activityType", e);
                                        }
                                    });

                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return id;
    }

    String insertCategory(Category category){
        String id = "";

        DocumentReference ref = firestoreDb.collection(CATEGORY_COLLECTION_NAME).document();
        id = ref.getId();
        category.setId(id);
        ref.set(category).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot activityType added with ID: " + ref.getId());
            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document activityType", e);
                    }
                });
        return id;
    }

//    void getCategoryIdByName(String name) {
//        firestoreDb.collection(CATEGORY_COLLECTION_NAME)
//                .whereEqualTo("categoryName", name)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
//                            Log.d(NETWORK_DATA_LOGS, document.getId() + " => " + document.getData());
//                        }
//                        else {
//                            Log.d(NETWORK_DATA_LOGS, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

    void getActivities(DocumentSnapshot lastPage, NetworkDataCallback callback){

        Query query = firestoreDb.collection(ACTIVITY_COLLECTION_NAME)
                .orderBy(ORDER_BY_FIELD)
                .limit(AppRepository.PAGE_SIZE);
        if(lastPage!=null)
            if(lastPage.exists())
                query.startAfter(lastPage);

        query.get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                //for (QueryDocumentSnapshot document : task.getResult()) {
                Log.d(TAG, "Firebase got activities");
                QuerySnapshot result = task.getResult();
                Log.d(TAG, "size: " + result.getDocuments().size());
                DocumentSnapshot last = result.getDocuments().get(result.getDocuments().size()-1);
                List<Activity> act = result.toObjects(Activity.class);
                callback.onCallback(act);//, last);
                    //Log.d(NETWORK_DATA_LOGS, document.getId() + " => " + document.getData());
                //}
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
            }
        });
    }
}
