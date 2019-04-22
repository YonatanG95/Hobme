package DataSources;

import android.content.Context;

import AppModel.Activity;
import AppModel.ActivityDao;
import AppModel.ActivityType;
import AppModel.ActivityTypeDao;
import AppModel.Category;
import AppModel.User;
import AppModel.UserActivityJoin;
import AppModel.UserActivityJoinDao;
import AppModel.UserDao;
import AppUtils.DataConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Activity.class, User.class, UserActivityJoin.class, ActivityType.class, Category.class}, version = 11)
@TypeConverters(DataConverters.class)
public abstract class AppDB extends RoomDatabase {

    private static AppDB instance;

    public abstract ActivityDao activityDao();
    //public abstract UserDao userDao();
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
