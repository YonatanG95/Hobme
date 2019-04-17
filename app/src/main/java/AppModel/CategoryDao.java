package AppModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryDao {

    @Query("SELECT categoryName FROM category_table")
    List<String> getAllCategories();

    @Query("SELECT id FROM category_table WHERE categoryName=:name")
    String getCategoryIdByName(String name);

    @Query("SELECT COUNT(id) FROM category_table")
    int countCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertCategory(Category... categories);
}
