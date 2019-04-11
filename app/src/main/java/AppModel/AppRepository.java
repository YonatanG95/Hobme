package AppModel;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AppRepository {

    private static AppRepository sInstance;
    private ActivityDao activityDao;
    private UserDao userDao;
    private UserActivityJoinDao userActivityJoinDao;
    private ActivityTypeDao activityTypeDao;

    private AppRepository(Context context){
        AppDB database = AppDB.getInstance(context);
        activityDao = database.activityDao();
        userDao = database.userDao();
        userActivityJoinDao = database.userActivityJoinDao();
        activityTypeDao = database.activityTypeDao();
    }

    public static synchronized AppRepository getInstance(Context context){
        if (sInstance == null) {
            sInstance = new AppRepository(context);
        }
        return sInstance;
    }

    public void insertActivity(Activity activity){
        Log.d("Check", "Repository - insert");
        activityDao.insert(activity);
    }

    public void updateActivity(Activity activity){
        activityDao.update(activity);
    }

    public void deleteActivity(Activity activity){
        activityDao.delete(activity);
    }

    public LiveData<List<Activity>> getActivitiesByCreator(int creatorId){
        return activityDao.getActivitiesByCreator(creatorId);
    }

    public LiveData<List<Activity>> getActivitiesByType(int activityTypeId){
        return activityDao.getActivitiesByType(activityTypeId);
    }

    //TODO delete when type ready
    public LiveData<List<Activity>> getActivities(){
        return activityDao.getActivities();
    }

    //TODO delete this
    public void deleteAllActivities(){activityDao.deleteAllActivities();}

    public List<String> getAllCategories()
    {
        return activityTypeDao.getAllCategories();
    }

    public List<String> getAlltypes()
    {
        return activityTypeDao.getAllTypes();
    }

}
