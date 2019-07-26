package AppModel.Entity;

import com.google.firebase.firestore.Blob;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_types_table",
        foreignKeys = {@ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "categoryId")})
public class ActivityType {

    @PrimaryKey
    @NonNull
    private String id;
    private String typeName;
    private String categoryId;

    @Ignore
    private Blob typePicture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Ignore
    public ActivityType(String id, String name, String typeName, Blob typePicture) {
        this.id = id;
        this.typeName = typeName;
        this.typePicture = typePicture;
    }

    @Ignore
    public ActivityType(String id, String name, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public ActivityType(String id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Blob getTypePicture() {
        return typePicture;
    }

    public void setTypePicture(Blob typePicture) {
        this.typePicture = typePicture;
    }
}