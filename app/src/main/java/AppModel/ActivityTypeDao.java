package AppModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ActivityTypeDao {

    @Query("SELECT DISTINCT categoryName FROM activity_types_table")
    List<String> getAllCategories();

    @Query("SELECT DISTINCT typeName FROM activity_types_table")
    List<String> getAllTypes();
}
