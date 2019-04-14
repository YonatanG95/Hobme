package DataSources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.firebase.firestore.Blob;
import com.project.hobme.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import AppModel.Activity;
import AppModel.ActivityDao;
import AppModel.ActivityType;
import AppModel.ActivityTypeDao;
import AppModel.Category;
import AppModel.UserActivityJoinDao;
import AppModel.UserDao;
import AppUtils.AppExecutors;
import AppUtils.DataConverters;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

public class AppRepository {

    private static AppRepository sInstance;
    private ActivityDao activityDao;
    private UserDao userDao;
    private UserActivityJoinDao userActivityJoinDao;
    private ActivityTypeDao activityTypeDao;
    private final AppExecutors executors;
    private NetworkData networkData;
    private Context mContext;

    private AppRepository(Context context, AppExecutors executors){
        this.executors = executors;
        AppDB database = AppDB.getInstance(context);
        this.activityDao = database.activityDao();
        this.userDao = database.userDao();
        this.userActivityJoinDao = database.userActivityJoinDao();
        this.activityTypeDao = database.activityTypeDao();
        this.networkData = NetworkData.getInstance();
        this.mContext = context;
        //dbInit();
    }

    public static synchronized AppRepository getInstance(Context context, AppExecutors executors){
        if (sInstance == null) {
            sInstance = new AppRepository(context, executors);
        }
        return sInstance;
    }

    //Create new activity
    public void insertActivity(Activity activity){
        executors.networkIO().execute(()-> {
            String id = networkData.insertActivity(activity);
            activity.setId(id);
        });
        executors.diskIO().execute(() -> {
            activityDao.insert(activity);
            Log.d("Check", "ID rep: " + activity.getId());
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

//    public List<String> getAllCategories()
//    {
//        return activityTypeDao.getAllCategories();
//    }

    public List<String> getAlltypes()
    {
        return activityTypeDao.getAllTypes();
    }


    private void insertCategory(Category category){
        executors.networkIO().execute(() -> {
            networkData.insertCategory(category);
        });
    }

    private void insertActivityType(ActivityType activityType, String categoryName){
        executors.networkIO().execute(() -> {
            networkData.insertActivityType(activityType, categoryName);
        });
    }


    private void dbInit(){

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.category_outdoor);
        Category cat1 = new Category("Outdoor", DataConverters.drawableToBlob(drawable));

        insertCategory(cat1);

        drawable = ContextCompat.getDrawable(mContext, R.drawable.category_sports);
        cat1 = new Category("Sports", DataConverters.drawableToBlob(drawable));

        insertCategory(cat1);

        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_tennis);
        ActivityType act1 = new ActivityType("", "Tennis", DataConverters.drawableToBlob(drawable), "");

        insertActivityType(act1, "Sports");

        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_hiking);
        act1 = new ActivityType("", "Hiking", DataConverters.drawableToBlob(drawable), "");

        insertActivityType(act1, "Outdoor");

        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_fishing);
        act1 = new ActivityType("", "Fishing", DataConverters.drawableToBlob(drawable), "");

        insertActivityType(act1, "Outdoor");

        drawable = ContextCompat.getDrawable(mContext, R.drawable.type_soccer);
        act1 = new ActivityType("", "Soccer", DataConverters.drawableToBlob(drawable), "");

        insertActivityType(act1, "Sports");

    }
}
