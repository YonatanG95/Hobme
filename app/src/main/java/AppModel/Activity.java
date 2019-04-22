package AppModel;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_table")//,//,
        //foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "creatorId", onDelete = CASCADE)},
       // indices = @Index(name = "creatorId_index", value = "creatorId"))
                //foreignKeys = {@ForeignKey(entity = ActivityType.class, parentColumns = "id", childColumns = "activityTypeId")})

public class Activity{

    @PrimaryKey @NonNull//(autoGenerate = true)
    private String id;
    private int activityTypeId;
    private Date creationTime;
    private int creatorId;
    private int minMembers;
    private int maxMembers;
    private int currMembers;
    private boolean isPrivate;
    private Date activityStartDateTime;
    private Date activityEndDateTime;
    private String activityInfo;
    @Ignore
    private Location activityLocation;
    @Ignore
    private List<Bitmap> activityPhotos;
    @Ignore
    private Bitmap displayedImage;
    @Ignore
    private List<Integer> membersIds;

    @Ignore
    public Activity(int activityTypeId, Date creationTime, List<Integer> membersIds, int creatorId, boolean isPrivate, List<Bitmap> activityPhotos, Bitmap displayedImage, Location activityLocation, Date activityDateTime, String activityInfo) {
        this.activityTypeId = activityTypeId;
        this.creationTime = creationTime;
        this.membersIds = membersIds;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.activityPhotos = activityPhotos;
        this.displayedImage = displayedImage;
        this.activityLocation = activityLocation;
        this.activityStartDateTime = activityDateTime;
        this.activityInfo = activityInfo;
    }

    @Ignore
    public Activity(int activityTypeId, Date creationTime, int creatorId, boolean isPrivate, Date activityDateTime, String activityInfo, Date activityEndDateTime) {
        this.activityTypeId = activityTypeId;
        this.creationTime = creationTime;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.activityStartDateTime = activityDateTime;
        this.activityInfo = activityInfo;
        this.activityEndDateTime = activityEndDateTime;
    }

    //TODO check if legal
    public Activity(){}

    public int getMinMembers() {
        return minMembers;
    }

    public void setMinMembers(int minMembers) {
        this.minMembers = minMembers;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public Date getActivityEndDateTime() {
        return activityEndDateTime;
    }

    public void setActivityEndDateTime(Date activityEndDateTime) {
        this.activityEndDateTime = activityEndDateTime;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public int getCurrMembers() {
        return currMembers;
    }

    public void setCurrMembers(int currMembers) {
        this.currMembers = currMembers;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
    }

    public String getId() {
        return id;
    }

    public Date getActivityStartDateTime() {
        return activityStartDateTime;
    }

    public void setActivityStartDateTime(Date activityStartDateTime) {
        this.activityStartDateTime = activityStartDateTime;
    }

    public void setId(String id) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return getActivityTypeId() == activity.getActivityTypeId() &&
                getCreatorId() == activity.getCreatorId() &&
                getMinMembers() == activity.getMinMembers() &&
                getMaxMembers() == activity.getMaxMembers() &&
                getCurrMembers() == activity.getCurrMembers() &&
                isPrivate() == activity.isPrivate() &&
                getId().equals(activity.getId()) &&
                Objects.equals(getCreationTime(), activity.getCreationTime()) &&
                Objects.equals(getActivityStartDateTime(), activity.getActivityStartDateTime()) &&
                Objects.equals(getActivityEndDateTime(), activity.getActivityEndDateTime()) &&
                Objects.equals(getActivityInfo(), activity.getActivityInfo()) &&
                Objects.equals(getActivityLocation(), activity.getActivityLocation()) &&
                Objects.equals(getActivityPhotos(), activity.getActivityPhotos()) &&
                Objects.equals(getDisplayedImage(), activity.getDisplayedImage()) &&
                Objects.equals(getMembersIds(), activity.getMembersIds());
    }

}
