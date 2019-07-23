package AppModel;

import android.content.Context;

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

@Database(entities = {Activity.class, User.class, UserActivityJoin.class, ActivityType.class, Category.class}, version = 16)
@TypeConverters(DataConverters.class)
public abstract class AppDB extends RoomDatabase {

    private static AppDB instance;

    public abstract ActivityDao activityDao();
    public abstract UserDao userDao();
    //public abstract UserActivityJoinDao userActivityJoinDao();
    //public abstract ActivityTypeDao activityTypeDao();

    public static synchronized AppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDB.class, "app_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
