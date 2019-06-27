package DataSources;

import android.content.Context;
import android.util.Log;
import android.view.View;

import AppModel.AppDB;
import AppModel.Entity.Activity;
import AppModel.Dao.ActivityDao;
import AppModel.LocalData;
import AppModel.RemoteData;
import AppUtils.AppExecutors;
import AppView.UserLoginFragment;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.Date;
import java.util.List;

public class AppRepository {

    public static final int PAGE_SIZE = 10;
    public static final int INIT_SIZE = 15;
    private final int PREFETCH_DISTANCE = 4;
    private static AppRepository sInstance;
    //private ActivityDao activityDao;
    //private UserDao userDao;
    //private UserActivityJoinDao userActivityJoinDao;
    //private ActivityTypeDao activityTypeDao;
    //private CategoryDao categoryDao;
//    private final AppExecutors executors;
    //private NetworkData networkData;
//    private Context mContext;
    private final String TAG = "AppRepository";
    //private boolean mInitialized = false;
    //MutableLiveData<Category> mt = new MutableLiveData<Category>();
    private AppExecutors appExecutors;
    private LocalData localData;
    private RemoteData remoteData;

    private AppRepository(Context context, AppExecutors executors){
        this.appExecutors = executors;
        this.localData = new LocalData(context, executors);
        this.remoteData = new RemoteData();
        AppDB database = AppDB.getInstance(context);
        //this.activityDao = database.activityDao();
//        //this.userDao = database.userDao();
//        //this.userActivityJoinDao = database.userActivityJoinDao();
//        //this.activityTypeDao = database.activityTypeDao();
        //this.networkData = NetworkData.getInstance();
//        this.mContext = context;
        //dbInit();
        //deleteAllActivities();
    }

    public static synchronized AppRepository getInstance(Context context, AppExecutors executors){
        if (sInstance == null) {
            sInstance = new AppRepository(context, executors);
        }
        return sInstance;
    }

    public LiveData<PagedList<Activity>> getActivities(){
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(this);
        return localData.getActivities(boundaryCallback);
    }

    public void insertActivity(Activity activity){
        appExecutors.networkIO().execute(() -> {
          String id = remoteData.insertActivity(activity);
          activity.setId(id);
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
                public void onCallback(List<Activity> activities) {
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

    public boolean currentlyLoggedIn(){
        return remoteData.currentlyLoggedIn();
    }

    public void signInUserEmail(String email, String password, View view, UserLoginFragment loginFragment){
        remoteData.userSignInEmail(email, password, view, loginFragment);
    }

    public void logOutUser(){
        remoteData.logOutUser();
    }


//    //Create new activity
//    public void insertActivity(Activity activity){
//        appExecutors.diskIO().execute(()-> {
//            String id = networkData.insertActivity(activity);
//            activity.setId(id);
//            activityDao.insert(activity);
//            Log.d("Check", "ID rep: " + activity.getId());
//        });
//
//    }

//    public List<String> getAllCategories(){
//        return categoryDao.getAllCategories();
//    }

//    public void updateActivity(Activity activity){
//        activityDao.update(activity);
//    }
//
//    public void deleteActivity(Activity activity){
//        activityDao.delete(activity);
//    }
//
//    public LiveData<List<Activity>> getActivitiesByCreator(int creatorId){
//        return activityDao.getActivitiesByCreator(creatorId);
//    }

//    public LiveData<List<Activity>> getActivitiesByType(int activityTypeId){
//        return activityDao.getActivitiesByType(activityTypeId);
//    }

//    //TODO delete when type ready
//    public LiveData<PagedList<Activity>> getActivities() {
//        Log.d(TAG, "appRepo get activities");
//        DataSource.Factory factory = activityDao.getDataSourcefactory();
//        RepoBoundaryCallback repoBoundaryCallback = new RepoBoundaryCallback();
//        return new LivePagedListBuilder(factory, new PagedList.Config.Builder()
//                .setEnablePlaceholders(false).setInitialLoadSizeHint(INIT_SIZE)
//                .setPageSize(PAGE_SIZE).setPrefetchDistance(PREFETCH_DISTANCE).build())
//                .setBoundaryCallback(repoBoundaryCallback).build();
//    }
//
//    //TODO delete this
//    public void deleteAllActivities(){
//        Log.d(TAG, "appRepo delete all activities");
//        executors.diskIO().execute(() -> {
//            activityDao.deleteAllActivities();
//        });
//    }

//    public List<String> getAllCategories()
//    {
//        return activityTypeDao.getAllCategories();
//    }

//    public List<String> getAlltypes()
//    {
//        return activityTypeDao.getAllTypes();
//    }


//    private void insertCategory(Category category){
//        executors.networkIO().execute(() -> {
//            networkData.insertCategory(category);
//        });
//    }
//
//    private void insertActivityType(ActivityType activityType, String categoryName){
//        executors.networkIO().execute(() -> {
//            networkData.insertActivityType(activityType, categoryName);
//        });
//    }

//    private synchronized void initializeData() {
//
//        if(mInitialized) return;
//        mInitialized = true;
//
//
//    }
//

//    private void dbInit(){
//
//        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.category_outdoor);
//        Category cat1 = new Category("Outdoor", DataConverters.drawableToBlob(drawable));
//
//        insertCategory(cat1);
//
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.category_sports);
//        cat1 = new Category("Sports", DataConverters.drawableToBlob(drawable));
//
//        insertCategory(cat1);
//
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_tennis);
//        ActivityType act1 = new ActivityType("", "Tennis", DataConverters.drawableToBlob(drawable), "");
//
//        insertActivityType(act1, "Sports");
//
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_hiking);
//        act1 = new ActivityType("", "Hiking", DataConverters.drawableToBlob(drawable), "");
//
//        insertActivityType(act1, "Outdoor");
//
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_fishing);
//        act1 = new ActivityType("", "Fishing", DataConverters.drawableToBlob(drawable), "");
//
//        insertActivityType(act1, "Outdoor");
//
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_soccer);
//        act1 = new ActivityType("", "Soccer", DataConverters.drawableToBlob(drawable), "");
//
//        insertActivityType(act1, "Sports");
//
//    }
}
