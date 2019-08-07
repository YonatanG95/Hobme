package AppModel.Dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import AppModel.Entity.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT categoryName FROM category_table")
    LiveData<List<String>> getAllCategoriesNames();

    @Query("SELECT id FROM category_table WHERE categoryName=:name")
    LiveData<String> getCategoryIdByName(String name);

    @Query("SELECT COUNT(id) FROM category_table")
    int countCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertCategory(List<Category> categories);
}
