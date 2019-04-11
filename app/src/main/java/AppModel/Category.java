package AppModel;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//@Entity(tableName = "category_table")
public class Category {

    //@PrimaryKey(autoGenerate = true)
    //private int id;
    private String categoryName;

    @Ignore
    private Bitmap categoryPicture;

    //@Ignore
    public Category(String name, Bitmap categoryPicture) {
        this.categoryName = name;
        this.categoryPicture = categoryPicture;
    }

    public Category(String name) {
        this.categoryName = name;
    }

    //@Ignore
    public Category(){}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Bitmap getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(Bitmap categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
}
