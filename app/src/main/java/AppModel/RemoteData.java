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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.hobme.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppView.ActivityListFragmentDirections;
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
    private final String ORDER_BY_FIELD = "activityStartDateTime";
    private FirebaseAuth mFirebaseAuth;

    public RemoteData(){
        firestoreDb = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestoreDb.setFirestoreSettings(settings);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
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
                        callback.onActivityCallback(act);

                    }
                    else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    public String insertActivity(Activity activity, User user, View view){

        String id = "";
        DocumentReference ref = firestoreDb.collection(ACTIVITY_COLLECTION_NAME).document();
        id = ref.getId();
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
//            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public void userSignInEmail(String email, String pass, View view, UserLoginFragment fragment){
        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(fragment.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser fbUser = mFirebaseAuth.getCurrentUser();
//                            callback.onUserIdCallback(user.getUid());
                            getUserById(fbUser.getUid(), new NetworkDataCallback.UserCallback() {
                                @Override
                                public void onUserCallback(User user) {
                                    UserLoginFragmentDirections.LoginToActList action = UserLoginFragmentDirections.loginToActList(user);
                                    Navigation.findNavController(view).navigate(action);
                                }
                            });
                            //TODO pass ID to next frag

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(fragment.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logOutUser(){
        mFirebaseAuth.signOut();
    }

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
//                                            String userId = user.getUid();
                                            User createdUser = new User();
                                            createdUser.setEmail(user.getEmail());
                                            createdUser.setFullName(user.getDisplayName());
                                            createdUser.setId(user.getUid());
                                            callback.onUserCallback(createdUser);
                                            //TODO pass ID to next frag
                                            UserRegisterFragmentDirections.RegisterToActList action = UserRegisterFragmentDirections.registerToActList(createdUser);
//                                            Navigation.findNavController(view).navigate(R.id.registerToActList);
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

    public void insertUser(User user){

        //String id = "";
        DocumentReference ref = firestoreDb.collection(USER_COLLECTION_NAME).document();
        //id = ref.getId();
        //user.setId(id);
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
        //return id;
    }

//    public void getUser(String id, NetworkDataCallback.ActivityCallback callback){
//
//        Query query = firestoreDb.collection(ACTIVITY_COLLECTION_NAME)
//                .orderBy(ORDER_BY_FIELD).whereGreaterThan(ORDER_BY_FIELD, date)
//                .limit(AppRepository.PAGE_SIZE);
//
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot result = task.getResult();
//                            List<Activity> act = result.toObjects(Activity.class);
//                            callback.onCallback(act);
//
//                        }
//                        else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }


    public void getUserById(String id, NetworkDataCallback.UserCallback callback) {
        firestoreDb.collection(USER_COLLECTION_NAME)
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            //DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            //Log.d(NETWORK_DATA_LOGS, document.getId() + " => " + document.getData());
                            User user = result.toObjects(User.class).get(0);
                            callback.onUserCallback(user);
                        }
                        else {
                            //Log.d(NETWORK_DATA_LOGS, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

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
}
