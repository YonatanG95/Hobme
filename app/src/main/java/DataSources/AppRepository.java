package DataSources;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import java.util.Date;
import java.util.List;
import AppModel.Entity.Activity;
import AppModel.Entity.Category;
import AppModel.Entity.User;
import AppModel.LocalData;
import AppModel.RemoteData;
import AppUtils.AppExecutors;
import AppView.UserLoginFragment;
import AppView.UserRegisterFragment;

/**
 * Local and remote storage synchronization. Single access point to both network and local DB
 */
public class AppRepository {

    public static final int PAGE_SIZE = 10;
    public static final int INIT_SIZE = 15;
    private final int PREFETCH_DISTANCE = 4;
    private static AppRepository sInstance;
    private final String TAG = "AppRepository";
    private AppExecutors appExecutors;
    private LocalData localData;
    private RemoteData remoteData;

    private AppRepository(Context context, AppExecutors executors){
        this.appExecutors = executors;
        this.localData = new LocalData(context);
        this.remoteData = new RemoteData();
    }

    public static synchronized AppRepository getInstance(Context context, AppExecutors executors){
        if (sInstance == null) {
            sInstance = new AppRepository(context, executors);
        }
        return sInstance;
    }


    //region Activity model methods
    public LiveData<PagedList<Activity>> getActivities(){
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(this);
        return localData.getActivities(boundaryCallback);
    }

    public void insertActivity(Activity activity, User user, View view){
        appExecutors.networkIO().execute(() -> {
            activity.setCreatorId(user.getId());
            activity.getMembersIds().add(user.getId());
            activity.setCurrMembers(activity.getCurrMembers() + 1);
            String id = remoteData.insertActivity(activity, user, view);
            activity.setId(id);
            user.getMyActivitiesIds().add(activity.getId());
            user.getActivitiesMemberIds().add(activity.getId());
            updateUser(user);
            appExecutors.diskIO().execute(() -> {
                localData.insertActivity(activity);
                Log.d(TAG, "Activity inserted with ID: " + activity.getId());
            });
        });
    }

    public void fetchMoreActivities(Date date){
        appExecutors.networkIO().execute(()-> {
            remoteData.fetchActivities(date, new NetworkDataCallback.ActivityCallback() {
                @Override
                public void onActivityCallback(List<Activity> activities) {
                    if(activities != null) {
                        appExecutors.diskIO().execute(() -> {
                            localData.insertActivities(activities);
                        });
                    }
                }
            });
        }
        );
    }

    public void updateActivity(Activity activity){
        appExecutors.networkIO().execute(() -> {
            remoteData.updateActivity(activity);
            appExecutors.diskIO().execute(() -> {
                localData.updateActivity(activity);
            });
        });
    }

    public void deleteActivity(Activity activity){
        appExecutors.networkIO().execute(() -> {
            remoteData.deleteActivity(activity);
            appExecutors.diskIO().execute(() -> {
                localData.deleteActivity(activity);
            });
        });
    }
    //endregion

    //region User model methods
    public void currentlyLoggedIn(View view){
        appExecutors.networkIO().execute(()-> {
            remoteData.currentlyLoggedIn(view);
        });
    }

    //TODO think of keeping this user special way in DB
    public void signInUserEmail(String email, String password, View view, UserLoginFragment loginFragment){
        appExecutors.networkIO().execute(()-> {
            remoteData.userSignInEmail(email, password, view, loginFragment);
        });
    }

    public void logOutUser(){
        appExecutors.networkIO().execute(()-> {
            remoteData.logOutUser();
        });
    }

    public void createUserEmail(String email, String password, String displayName, View view, UserRegisterFragment fragment){
        appExecutors.networkIO().execute(() -> {
            remoteData.createUserEmail(email, password, displayName, view, fragment, new NetworkDataCallback.UserCallback() {
                @Override
                public void onUserCallback(User newUser) {
                    remoteData.insertUser(newUser);
                    appExecutors.diskIO().execute(() -> {
                        localData.insertUser(newUser);
                        Log.d(TAG, "User inserted with ID: " + newUser.getId());
                    });
                }
            });
        });
    }

    public void fetchUser(String id){
        appExecutors.networkIO().execute(() -> {
            remoteData.getUserById(id, new NetworkDataCallback.UserCallback() {
                @Override
                public void onUserCallback(User user) {
                    appExecutors.diskIO().execute(() -> {
                        localData.insertUser(user);
                    });
                }
            });
        });
    }

    public LiveData<User> getUserById(String id){
        LiveData<User> user = localData.getUserById(id);
        if(user == null){
            fetchUser(id);
            return localData.getUserById(id);
        }
        else {
            return user;
        }
    }

    public void updateUser(User user){
        appExecutors.networkIO().execute(() -> {
            remoteData.updateUser(user);
            appExecutors.diskIO().execute(() -> {
                localData.updateUser(user);
            });
        });
    }
    //endregion


    //region Category model methods
    public void fetchCategories(){
        appExecutors.networkIO().execute(() -> {
           remoteData.getCategories(new NetworkDataCallback.CategoryCallback() {
               @Override
               public void onCategoryCallback(List<Category> categories) {
                   appExecutors.diskIO().execute(() -> {
                       localData.insertCategories(categories);
                   });
               }
           });
        });
    }

    public List<String> getCategoriesNames(){
        if(localData.getCategoriesNames() == null){
            fetchCategories();
        }
        return localData.getCategoriesNames();
    }
    //endregion
}
