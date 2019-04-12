package DataSources;

import android.content.Context;
import android.util.Log;

import java.util.List;

import AppModel.Activity;
import AppModel.ActivityDao;
import AppModel.ActivityTypeDao;
import AppModel.UserActivityJoinDao;
import AppModel.UserDao;
import AppUtils.AppExecutors;
import androidx.lifecycle.LiveData;

public class AppRepository {

    private static AppRepository sInstance;
    private ActivityDao activityDao;
    private UserDao userDao;
    private UserActivityJoinDao userActivityJoinDao;
    private ActivityTypeDao activityTypeDao;
    private final AppExecutors executors;
    private NetworkData networkData;

    private AppRepository(Context context, AppExecutors executors){
        this.executors = executors;
        AppDB database = AppDB.getInstance(context);
        this.activityDao = database.activityDao();
        this.userDao = database.userDao();
        this.userActivityJoinDao = database.userActivityJoinDao();
        this.activityTypeDao = database.activityTypeDao();
        this.networkData = NetworkData.getInstance();
    }

    public static synchronized AppRepository getInstance(Context context, AppExecutors executors){
        if (sInstance == null) {
            sInstance = new AppRepository(context, executors);
        }
        return sInstance;
    }

    public void insertActivity(Activity activity){
        executors.diskIO().execute(() -> {
            Log.d("Check", "Repository - insert");
            activityDao.insert(activity);
        });
    }

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

    public LiveData<List<Activity>> getActivitiesByType(int activityTypeId){
        return activityDao.getActivitiesByType(activityTypeId);
    }

    //TODO delete when type ready
    public LiveData<List<Activity>> getActivities(){
        return activityDao.getActivities();
    }

    //TODO delete this
    public void deleteAllActivities(){
        executors.diskIO().execute(() -> {
            activityDao.deleteAllActivities();
        });
    }

    public List<String> getAllCategories()
    {
        return activityTypeDao.getAllCategories();
    }

    public List<String> getAlltypes()
    {
        return activityTypeDao.getAllTypes();
    }

}
