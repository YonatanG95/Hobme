package AppModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import AppModel.Entity.Activity;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import AppModel.Entity.User;
import AppView.CreateActivityFragmentDirections;
import AppView.UserLoginFragment;
import AppView.UserLoginFragmentDirections;
import AppView.UserRegisterFragment;
import AppView.UserRegisterFragmentDirections;
import DataSources.AppRepository;
import DataSources.NetworkDataCallback;

public class RemoteData {

    private FirebaseFirestore firestoreDb;
    private final String TAG = "NetworkDataLog";
    private final String ACTIVITY_COLLECTION_NAME = "activity";
    private final String USER_COLLECTION_NAME = "user";
    private final String ACTIVITY_TYPE_COLLECTION_NAME = "activityType";
    private final String CATEGORY_COLLECTION_NAME = "category";
    private final String ACTIVITY_ORDER_BY_FIELD = "activityStartDateTime";
    private FirebaseAuth mFirebaseAuth;


    /**
     * Constructor - initializes firebase settings
     */
    public RemoteData() {
        firestoreDb = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDb.setFirestoreSettings(settings);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
    }

    //region Activity model methods

    /**
     * Requests a list of activities from specific start date
     * @param date - minimum date from which should fetch new activities
     * @param callback
     */
    public void fetchActivities(Date date, NetworkDataCallback.ActivityCallback callback){

        Query query = firestoreDb.collection(ACTIVITY_COLLECTION_NAME)
                .orderBy(ACTIVITY_ORDER_BY_FIELD).whereGreaterThan(ACTIVITY_ORDER_BY_FIELD, date)
                .limit(AppRepository.PAGE_SIZE);
        query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        List<Activity> act = result.toObjects(Activity.class);
                        callback.onActivityCallback(act);

                    }
                    else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    /**
     * Inserts newly created activity to remote DB
     * @param activity
     * @param user
     * @param view
     * @return activity's ID
     */
    public String insertActivity(Activity activity, User user, View view){

        String id = "";
        DocumentReference ref = firestoreDb.collection(ACTIVITY_COLLECTION_NAME).document();
        id = ref.getId();

        //Activity first created with null ID. Here it gets its ID from firebase's document ID
        activity.setId(id);
        ref.set(activity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + ref.getId());
                CreateActivityFragmentDirections.ActCreateToList action = CreateActivityFragmentDirections.actCreateToList(user);
                Navigation.findNavController(view).navigate(action);
            }})
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
        return id;
    }


    public void updateActivity(Activity activity){
        DocumentReference ref = firestoreDb.collection(ACTIVITY_COLLECTION_NAME).document(activity.getId());
        ref.set(activity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void deleteActivity(Activity activity){

        DocumentReference ref = firestoreDb.collection(ACTIVITY_COLLECTION_NAME).document(activity.getId());
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    //endregion

    //region User model methods

    /**
     * Checks if firebase user session exists. If so - navigates to main page
     * @param view
     */
    public void currentlyLoggedIn(View view){
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null) {
            String id = currentUser.getUid();
            getUserById(id, new NetworkDataCallback.UserCallback() {
                @Override
                public void onUserCallback(User user) {
                    UserLoginFragmentDirections.LoginToActList action = UserLoginFragmentDirections.loginToActList(user);
                    Navigation.findNavController(view).navigate(action);
                }
            });
        }
    }

    /**
     * Uses firebase email login. Navigates to main page on success
     * @param email
     * @param pass
     * @param view
     * @param fragment
     */
    public void userSignInEmail(String email, String pass, View view, UserLoginFragment fragment){
        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(fragment.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser fbUser = mFirebaseAuth.getCurrentUser();
                            getUserById(fbUser.getUid(), new NetworkDataCallback.UserCallback() {
                                @Override
                                public void onUserCallback(User user) {
                                    UserLoginFragmentDirections.LoginToActList action = UserLoginFragmentDirections.loginToActList(user);
                                    Navigation.findNavController(view).navigate(action);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(fragment.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Ends active firebase user session
     */
    public void logOutUser(){
        mFirebaseAuth.signOut();
    }

    /**
     * Creates user using firebase email registration
     * @param email
     * @param password
     * @param displayName
     * @param view
     * @param fragment
     * @param callback
     */
    public void createUserEmail(String email, String password, String displayName, View view, UserRegisterFragment fragment,
                                NetworkDataCallback.UserCallback callback){
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(fragment.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();
                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            User createdUser = new User();
                                            createdUser.setEmail(user.getEmail());
                                            createdUser.setFullName(user.getDisplayName());
                                            createdUser.setId(user.getUid());
                                            callback.onUserCallback(createdUser);

                                            //Navigate to main page
                                            UserRegisterFragmentDirections.RegisterToActList action = UserRegisterFragmentDirections.registerToActList(createdUser);
                                            Navigation.findNavController(view).navigate(action);
                                        }
                                    }
                                });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(fragment.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Inserts new user to remote DB (not authentication related)
     * @param user
     */
    public void insertUser(User user){

        DocumentReference ref = firestoreDb.collection(USER_COLLECTION_NAME).document();
        user.setFbDocId(ref.getId());
        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }

    /**
     * Gets user by its ID (not authentication related)
     * @param id
     * @param callback
     */
    public void getUserById(String id, NetworkDataCallback.UserCallback callback) {
        firestoreDb.collection(USER_COLLECTION_NAME)
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            User user = result.toObjects(User.class).get(0);
                            callback.onUserCallback(user);
                        }
                        else {
                        }
                    }
                });
    }

    /**
     * Updates user's values (not authentication related)
     * @param user
     */
    public void updateUser(User user){
        DocumentReference ref = firestoreDb.collection(USER_COLLECTION_NAME).document(user.getFbDocId());
        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });
    }
    //endregion


    //region Category model methods
    /**
     * Request all categories entries - as a list
     * @param callback
     */
    public void getCategories(NetworkDataCallback.CategoryCallback callback){
        firestoreDb.collection(CATEGORY_COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Category> categories = new ArrayList<Category>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                categories.add(document.toObject(Category.class));
                            }
                            callback.onCategoryCallback(categories);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    //endregion

    //region ActivityType model methods
    /**
     * Request all activity types of a specific category
     * @param callback
     * @param categoryId
     */
    public void getTypesByCategory(NetworkDataCallback.ActivityTypeCallback callback, String categoryId){
        Log.d(TAG, "Category " + categoryId);
        firestoreDb.collection(ACTIVITY_TYPE_COLLECTION_NAME)
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ActivityType> activityTypes = new ArrayList<ActivityType>();
                            Log.d(TAG, "Docs " + task.getResult().getDocuments().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                activityTypes.add(document.toObject(ActivityType.class));
                            }
                            callback.onActivityTypeCallback(activityTypes);
                            Log.d(TAG, "got types: " + activityTypes.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    //endregion
}
