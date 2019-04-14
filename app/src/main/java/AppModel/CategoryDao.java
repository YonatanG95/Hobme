package AppModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface CategoryDao {

    @Query("SELECT categoryName FROM category_table")
    List<Category> getAllCategories();

    @Query("SELECT id FROM category_table WHERE categoryName=:name")
    String getCategoryIdByName(String name);
}
