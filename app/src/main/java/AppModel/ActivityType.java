package AppModel;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_types_table")
public class ActivityType extends Category{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String typeName;

    @Ignore
    private Bitmap typePicture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Ignore
    public ActivityType(int id, String name, Bitmap categoryPicture, String typeName, Bitmap typePicture) {
        super(name, categoryPicture);
        this.id = id;
        this.typeName = typeName;
        this.typePicture = typePicture;
    }

    @Ignore
    public ActivityType(int id, String name, String typeName) {
        super(name);
        this.id = id;
        this.typeName = typeName;
    }

    public ActivityType(int id, String typeName)
    {
        this.id = id;
        this.typeName = typeName;
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
