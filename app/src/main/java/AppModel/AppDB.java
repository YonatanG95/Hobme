package AppModel;

import android.content.Context;

import AppUtils.DataConverters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Activity.class}, version = 1)
@TypeConverters(DataConverters.class)
public abstract class AppDB extends RoomDatabase {

    private static AppDB instance;

    public abstract ActivityDao activityDao();

    public static synchronized AppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDB.class, "app_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
