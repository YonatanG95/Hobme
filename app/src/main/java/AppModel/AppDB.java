package AppModel;

import android.content.Context;

import AppModel.Dao.ActivityTypeDao;
import AppModel.Dao.CategoryDao;
import AppModel.Dao.UserDao;
import AppModel.Entity.Activity;
import AppModel.Dao.ActivityDao;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import AppModel.Entity.User;
import AppModel.Entity.UserActivityJoin;
import AppUtils.DataConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Activity.class, User.class, UserActivityJoin.class, ActivityType.class, Category.class}, version = 22)
@TypeConverters(DataConverters.class)
public abstract class AppDB extends RoomDatabase {

    private static final String DB_NAME = "app_db";
    private static AppDB instance;


    //region Enable access to models Dao through DB instance
    public abstract ActivityDao activityDao();
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ActivityTypeDao activityTypeDao();
    //endregion

    /**
     * Returns a singular instance of application's local (Room) DB
     * @param context
     * @return
     */
    public static synchronized AppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDB.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
