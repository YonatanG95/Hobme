package com.project.hobme;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(ActivityEntry...activityEntries);

    @Query("SELECT * FROM activity WHERE id = :id")
    LiveData<ActivityEntry> getActivityByID(int id);

}
