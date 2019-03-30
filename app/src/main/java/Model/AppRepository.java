package Model;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AppRepository {

    private ActivityDao activityDao;

    public AppRepository(Application application){
        AppDB database = AppDB.getInstance(application);
        activityDao = database.activityDao();
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
