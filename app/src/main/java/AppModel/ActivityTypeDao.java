package AppModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ActivityTypeDao {

    @Query("SELECT DISTINCT typeName FROM activity_types_table")
    List<String> getAllTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityType activityType);

    @Query("SELECT typeName FROM activity_types_table WHERE categoryId=:categoryId")
    List<String> getTypesByCategory(String categoryId);
}
