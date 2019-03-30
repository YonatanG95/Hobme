package Model;

import android.graphics.Bitmap;

public class Category {

    private String categoryName;
    private Bitmap categoryPicture;

    public Category(String name, Bitmap categoryPicture) {
        this.categoryName = name;
        this.categoryPicture = categoryPicture;
    }

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
