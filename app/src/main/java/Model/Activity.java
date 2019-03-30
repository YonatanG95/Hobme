package Model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_table")
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int activityTypeId;
    private Date creationTime;
    private int creatorId;
    private boolean isPrivate;
    private Date activityDateTime;
    @Ignore
    private Location activityLocation;
    @Ignore
    private List<Bitmap> activityPhotos;
    @Ignore
    private Bitmap displayedImage;
    @Ignore
    private List<Integer> membersIds;

    @Ignore
    public Activity(int activityTypeId, Date creationTime, List<Integer> membersIds, int creatorId, boolean isPrivate, List<Bitmap> activityPhotos, Bitmap displayedImage, Location activityLocation, Date activityDateTime) {
        this.activityTypeId = activityTypeId;
        this.creationTime = creationTime;
        this.membersIds = membersIds;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.activityPhotos = activityPhotos;
        this.displayedImage = displayedImage;
        this.activityLocation = activityLocation;
        this.activityDateTime = activityDateTime;
    }

    public Activity(int activityTypeId, Date creationTime, int creatorId, boolean isPrivate, Date activityDateTime) {
        this.activityTypeId = activityTypeId;
        this.creationTime = creationTime;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.activityDateTime = activityDateTime;
    }

    public int getId() {
        return id;
    }

    public Date getActivityDateTime() {
        return activityDateTime;
    }

    public void setActivityDateTime(Date activityDateTime) {
        this.activityDateTime = activityDateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public List<Integer> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<Integer> membersIds) {
        this.membersIds = membersIds;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Bitmap> getActivityPhotos() {
        return activityPhotos;
    }

    public void setActivityPhotos(List<Bitmap> activityPhotos) {
        this.activityPhotos = activityPhotos;
    }

    public Bitmap getDisplayedImage() {
        return displayedImage;
    }

    public void setDisplayedImage(Bitmap displayedImage) {
        this.displayedImage = displayedImage;
    }

    public Location getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(Location activityLocation) {
        this.activityLocation = activityLocation;
    }
}
