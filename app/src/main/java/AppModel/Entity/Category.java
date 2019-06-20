package AppModel.Entity;

import android.graphics.Bitmap;


import com.google.firebase.firestore.Blob;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey @NonNull
    private String id;
    private String categoryName;

    @Ignore
    private Blob categoryPicture;

    @Ignore
    public Category(String name, Blob categoryPicture) {
        this.categoryName = name;
        this.categoryPicture = categoryPicture;
    }

    public Category(String name) {
        this.categoryName = name;
    }

    public Category(){}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Blob getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(Blob categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
