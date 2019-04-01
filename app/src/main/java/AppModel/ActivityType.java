package AppModel;

import android.graphics.Bitmap;

public class ActivityType extends Category{

    private int id;
    private String typeName;
    private Bitmap typePicture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActivityType(int id, String name, Bitmap categoryPicture, String typeName, Bitmap typePicture) {
        super(name, categoryPicture);
        this.id = id;
        this.typeName = typeName;
        this.typePicture = typePicture;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Bitmap getTypePicture() {
        return typePicture;
    }

    public void setTypePicture(Bitmap typePicture) {
        this.typePicture = typePicture;
    }
}
