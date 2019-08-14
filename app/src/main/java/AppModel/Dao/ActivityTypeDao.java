package AppModel.Dao;

import java.sql.Blob;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import AppModel.Entity.ActivityType;

@Dao
public interface ActivityTypeDao {

    @Query("SELECT DISTINCT typeName FROM activity_types_table")
    List<String> getAllTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityType activityType);

    @Query("SELECT typeName FROM activity_types_table WHERE categoryId=:categoryId")
    LiveData<List<String>> getTypesNamesByCategory(String categoryId);

//    @Query("SELECT typePicture FROM activity_types_table WHERE typeName=:name")
//    LiveData<Blob> getTypePicture(String name);

    @Query("SELECT COUNT(id) FROM activity_types_table")
    int countActivityTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertActivityType(List<ActivityType> activityTypes);
}
