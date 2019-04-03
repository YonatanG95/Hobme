package AppModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);

    @Query("SELECT * FROM activity_table WHERE creatorId = :creatorId")
    LiveData<List<Activity>> getActivitiesByCreator(int creatorId);

    @Query("SELECT * FROM activity_table WHERE activityTypeId = :activityTypeId")
    LiveData<List<Activity>> getActivitiesByType(int activityTypeId);

    //TODO delete when type ready
    @Query("SELECT * FROM activity_table")
    LiveData<List<Activity>> getActivities();
}
