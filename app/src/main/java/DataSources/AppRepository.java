package DataSources;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import java.util.Date;
import java.util.List;
import AppModel.Entity.Activity;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import AppModel.Entity.SimplePlace;
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

    /**
     * Constructor
     * @param context
     * @param executors - Background jobs executors for disk IO & network
     */
    private AppRepository(Context context, AppExecutors executors){
        this.appExecutors = executors;
        this.localData = new LocalData(context);
        this.remoteData = new RemoteData();
    }

    /**
     * Returns an instance of repository which is a single point of access to whole app DB (local & remote)
     * @param context
     * @param executors
     * @return
     */
    public static synchronized AppRepository getInstance(Context context, AppExecutors executors){
        if (sInstance == null) {
            sInstance = new AppRepository(context, executors);
        }
        return sInstance;
    }


    //region Activity model methods

    /**
     * Creates boundary callback and sends it to the local-DB implementation of this method
     * @return liveData of type paged list of activities to display in recycler views
     */
    public LiveData<PagedList<Activity>> getActivities(){
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(this);
        return localData.getActivities(boundaryCallback);
    }

    /**
     * Inserts a new activity to local and remote DB
     * @param activity - the new activity
     * @param user - creator of the activity
     * @param view
     */
    public void insertActivity(Activity activity, User user, View view){
        appExecutors.networkIO().execute(() -> {
            //Set activity fields
            activity.setCreatorId(user.getId());
            activity.getMembersIds().add(user.getId());
            activity.setCurrMembers(activity.getCurrMembers() + 1);
            String id = remoteData.insertActivity(activity, user, view);
            activity.setId(id);

            //Update user fields
            user.getMyActivitiesIds().add(activity.getId());
            user.getActivitiesMemberIds().add(activity.getId());
            updateUser(user);
            appExecutors.diskIO().execute(() -> {
                localData.insertActivity(activity);
                Log.d(TAG, "Activity inserted with ID: " + activity.getId());
            });
        });
    }

    /**
     * Receives activities from the network and inserts them to Room DB
     * @param date - minimum date from which should fetch new activities
     */
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

    /**
     * Updates activity both local & remote
     * @param activity
     */
    public void updateActivity(Activity activity){
        appExecutors.networkIO().execute(() -> {
            remoteData.updateActivity(activity);
            appExecutors.diskIO().execute(() -> {
                localData.updateActivity(activity);
            });
        });
    }

    /**
     * Deletes activity both local & remote
     * @param activity
     */
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

    /**
     * Checks if user's session to firebase is already open and if so - navigates to main page
     * @param view
     */
    public void currentlyLoggedIn(View view, SimplePlace currLocation){
        appExecutors.networkIO().execute(()-> {
            remoteData.currentlyLoggedIn(view, currLocation);
        });
    }

    /**
     * Authenticates user using email
     * @param email
     * @param password
     * @param view
     * @param loginFragment
     */
    //TODO think of keeping this user special way in DB
    public void signInUserEmail(String email, String password, View view, UserLoginFragment loginFragment, SimplePlace currLocation){
        appExecutors.networkIO().execute(()-> {
            remoteData.userSignInEmail(email, password, view, loginFragment, currLocation);
        });
    }

    /**
     * Ends user's firebase session
     */
    public void logOutUser(){
        appExecutors.networkIO().execute(()-> {
            remoteData.logOutUser();
        });
    }

    /**
     * Creates a user using email
     * @param email
     * @param password
     * @param displayName
     * @param view
     * @param fragment
     */
    public void createUserEmail(String email, String password, String displayName, View view, UserRegisterFragment fragment){
        appExecutors.networkIO().execute(() -> {
            remoteData.createUserEmail(email, password, displayName, view, fragment, new NetworkDataCallback.UserCallback() {
                @Override
                public void onUserCallback(User newUser) {
                    //Insert the newly created user to local & remote DB
                    remoteData.insertUser(newUser);
                    appExecutors.diskIO().execute(() -> {
                        localData.insertUser(newUser);
                        Log.d(TAG, "User inserted with ID: " + newUser.getId());
                    });
                }
            });
        });
    }

    /**
     * Recieves user from remote DB by its ID and inserts it to Room DB
     * @param id
     */
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


    /**
     * Updates user in both local & remote DB
     * @param user
     */
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

    /**
     * Gets categories from remote DB and inserts them to Room DB
     */
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

    /**
     * Returns categories names if those exist. Else - fetches them
     * @return
     */
    public LiveData<List<String>> getCategoriesNames(){
        if(localData.getCategoriesNames().getValue() == null){
            fetchCategories();
        }
        //Log.d(TAG, "Cat size: " + localData.getCategoriesNames().getValue().size());
        return localData.getCategoriesNames();
    }

    public LiveData<String> getCategoryIdByName(String categoryName){
        return localData.getCategoryIdByName(categoryName);
    }
    //endregion

    //region ActivityType model methods

    /**
     * Gets activity types of a specific category from remote DB and inserts them into Room DB
     * @param categoryId
     */
    public void fetchActivityTypesByCategory(String categoryId){
        appExecutors.networkIO().execute(() -> {
            remoteData.getTypesByCategory(new NetworkDataCallback.ActivityTypeCallback() {
                @Override
                public void onActivityTypeCallback(List<ActivityType> activityTypes) {
                    appExecutors.diskIO().execute(() -> {
                        localData.insertTypes(activityTypes);
                        Log.d(TAG, "type: " + activityTypes.size());
                    });
                }
            }, categoryId);
        });
    }

    /**
     * Returns activity type names if those exist. Else - fetches them
     * @param categoryId
     * @return
     */
    public LiveData<List<String>> getTypeNamesByCategory(String categoryId){
        if(localData.getTypeNamesByCategoryId(categoryId).getValue() == null){
            Log.d(TAG, "Types not found");
            fetchActivityTypesByCategory(categoryId);
        }
        return localData.getTypeNamesByCategoryId(categoryId);
    }
    //endregion
}
