package AppModel;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AppRepository {

    private static AppRepository sInstance;
    private ActivityDao activityDao;

    private AppRepository(Context context){
        AppDB database = AppDB.getInstance(context);
        activityDao = database.activityDao();
    }

    public static synchronized AppRepository getInstance(Context context){
        if (sInstance == null) {
            sInstance = new AppRepository(context);
        }
        return sInstance;
    }

    public void insertActivity(Activity activity){
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

}
