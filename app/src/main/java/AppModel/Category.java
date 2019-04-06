package AppModel;

import android.graphics.Bitmap;

import androidx.room.Ignore;

public class Category {

    protected String categoryName;

    @Ignore
    protected Bitmap categoryPicture;

    public Category(String name, Bitmap categoryPicture) {
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

    public Bitmap getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(Bitmap categoryPicture) {
        this.categoryPicture = categoryPicture;
    }
}
