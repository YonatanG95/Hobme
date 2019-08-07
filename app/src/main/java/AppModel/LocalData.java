package AppModel;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import java.util.List;
import AppModel.Dao.ActivityDao;
import AppModel.Dao.ActivityTypeDao;
import AppModel.Dao.CategoryDao;
import AppModel.Dao.UserDao;
import AppModel.Entity.Activity;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import AppModel.Entity.User;
import DataSources.RepoBoundaryCallback;

/**
 * Room DB local storage interactions. Uses the application local DB to communicate with the models
 */
public class LocalData {

    private ActivityDao activityDao;
    private UserDao userDao;
    private CategoryDao categoryDao;
    private ActivityTypeDao activityTypeDao;

    /**
     * Gets DB instance and connects to models (Dao) through it
     * @param context
     */
    public LocalData(Context context) {
        AppDB appDB = AppDB.getInstance(context);
        this.activityDao = appDB.activityDao();
        this.userDao = appDB.userDao();
        this.categoryDao = appDB.categoryDao();
        this.activityTypeDao = appDB.activityTypeDao();
    }

    //region Activity model methods
    /**
     * @param boundaryCallback - boundary callback to control activity fetching
     * according to paged list state
     * @return activities as paged list. During this procedure, local data is syncing with remote data
     */
    public LiveData<PagedList<Activity>> getActivities(RepoBoundaryCallback boundaryCallback){
        DataSource.Factory factory = activityDao.getDataSourceFactory();
        return new LivePagedListBuilder(factory, RepoBoundaryCallback.DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback).build();
    }

    /**
     * Inserts array of activities into Room DB
     * @param activityList
     */
    public void insertActivities(List<Activity> activityList){
            activityDao.insertActivities(activityList);
    }

    /**
     * Inserts single activity into Room DB
     * @param activity
     */
    public void insertActivity(Activity activity){
            activityDao.insert(activity);
    }

    /**
     * Updates single activity in Room DB with new values
     * @param activity
     */
    public void updateActivity(Activity activity){
        activityDao.update(activity);
    }


    /**
     * Deletes single activity from Room DB
     * @param activity
     */
    public void deleteActivity(Activity activity) {activityDao.delete(activity);}
    //endregion

    //region User model methods

    /**
     * Inserts single user into Room DB
     * @param user
     */
    public void insertUser(User user){
            userDao.insert(user);
    }

    /**
     * @param id
     * @return a user from Room DB by its ID (Authentication ID)
     */
    public LiveData<User> getUserById(String id){
        return userDao.getUserById(id);
    }


    /**
     * Updates single user in Room DB with new values
     * @param user
     */
    public void updateUser(User user){
        userDao.update(user);
    }

    //endregion

    //region Category model methods

    /**
     * @return a list of the names of all categories
     */
    public LiveData<List<String>> getCategoriesNames(){
        return categoryDao.getAllCategoriesNames();
    }

    /**
     * Inserts a list of categories into Room DB
     * @param categories
     */
    public void insertCategories(List<Category> categories){
        categoryDao.bulkInsertCategory(categories);
    }

    /**
     *
     * @param categoryName
     * @return category ID by its name
     */
    public LiveData<String> getCategoryIdByName(String categoryName){
        return categoryDao.getCategoryIdByName(categoryName);
    }
    //endregion

    //region ActivityType model methods
    /**
     *
     * @param categoryId
     * @return list of all activity types of a specific category
     */
    public LiveData<List<String>> getTypeNamesByCategoryId(String categoryId){
        return activityTypeDao.getTypesNamesByCategory(categoryId);
    }

    /**
     * Inserts activity types into Room DB
     * @param activityTypes
     */
    public void insertTypes(List<ActivityType> activityTypes){
        activityTypeDao.bulkInsertActivityType(activityTypes);
    }
    //endregion
}
