package AppModel.Dao;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import AppModel.Entity.Activity;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);

//    @Query("SELECT * FROM activity_table WHERE creatorId = :creatorId")
//    LiveData<List<Activity>> getActivitiesByCreator(int creatorId);
//
//    @Query("SELECT * FROM activity_table WHERE activityTypeId = :activityTypeId")
//    LiveData<List<Activity>> getActivitiesByType(int activityTypeId);


    @Query("SELECT * FROM activity_table ORDER BY activityStartDateTime ASC")
    DataSource.Factory<Integer, Activity> getActivitiesSortStart();

    @Query("SELECT * FROM activity_table ORDER BY activityDurationMin ASC")
    DataSource.Factory<Integer, Activity> getActivitiesSortDuration();

//    @Query("SELECT * FROM activity_table WHERE activityCategory = :category ORDER BY activityStartDateTime ASC")
//    DataSource.Factory<Integer, Activity> getActivitiesFilterCategory(String category);

    @Query("SELECT * FROM activity_table")
    LiveData<Activity> getActivities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertActivities(List<Activity> activities);

    @Query("DELETE FROM activity_table WHERE activityStartDateTime < :startDate")
    void deletePrevious(Date startDate);


//    @Query("DELETE FROM activity_table")
//    void deleteAllActivities();
}
