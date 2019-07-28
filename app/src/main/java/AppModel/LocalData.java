package AppModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import AppModel.Dao.ActivityDao;
import AppModel.Dao.CategoryDao;
import AppModel.Dao.UserDao;
import AppModel.Entity.Activity;
import AppModel.Entity.Category;
import AppModel.Entity.User;
import AppUtils.AppExecutors;
import DataSources.RepoBoundaryCallback;

public class LocalData {

    private ActivityDao activityDao;
    private UserDao userDao;
    private CategoryDao categoryDao;
    //private RepoBoundaryCallback boundaryCallback;
    private final AppExecutors appExecutors;


    public LocalData(Context context, AppExecutors appExecutors) {

        AppDB appDB = AppDB.getInstance(context);
        this.activityDao = appDB.activityDao();
        this.userDao = appDB.userDao();
        this.categoryDao = appDB.categoryDao();
        this.appExecutors = appExecutors;
        //this.boundaryCallback = new RepoBoundaryCallback();

    }

    public LiveData<PagedList<Activity>> getActivities(RepoBoundaryCallback boundaryCallback){
        DataSource.Factory factory = activityDao.getDataSourcefactory();
        return new LivePagedListBuilder(factory, RepoBoundaryCallback.DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback).build();
    }

    public void insertActivities(List<Activity> activityList){
//        appExecutors.diskIO().execute(() -> {
            activityDao.insertActivities(activityList);
//        });
    }

    public void insertActivity(Activity activity){
//        appExecutors.diskIO().execute(() -> {
            activityDao.insert(activity);
//        });
    }

    public void insertUser(User user){
//        appExecutors.diskIO().execute(() -> {
            userDao.insert(user);
//        });
    }

    public LiveData<User> getUserById(String id){
        return userDao.getUserById(id);
    }

    public void updateUser(User user){
        userDao.update(user);
    }

    public void updateActivity(Activity activity){
        activityDao.update(activity);
    }

    public void deleteActivity(Activity activity) {activityDao.delete(activity);}

    public List<String> getCategoriesNames(){
        return categoryDao.getAllCategoriesNames();
    }

    public void insertCategories(List<Category> categories){
        categoryDao.bulkInsertCategory(categories);
    }
}
