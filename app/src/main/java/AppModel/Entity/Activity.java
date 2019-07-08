package AppModel.Entity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Blob;

import AppUtils.DataConverters;

@Entity(tableName = "activity_table")//,//,
        //foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "creatorId", onDelete = CASCADE)},
       // indices = @Index(name = "creatorId_index", value = "creatorId"))
                //foreignKeys = {@ForeignKey(entity = ActivityType.class, parentColumns = "id", childColumns = "activityTypeId")})
@BindingMethods({
        @BindingMethod(type = ImageView.class,
                attribute = "android:src",
                method = "setImageSrc")})

public class Activity implements Parcelable {

    @PrimaryKey @NonNull//(autoGenerate = true)
    private String id;
    private int activityTypeId;
    private Date creationTime;
    private String creatorId;
    private int minMembers;
    private int maxMembers;
    private int currMembers;
    private boolean isPrivate;
    private Date activityStartDateTime;
    private Date activityEndDateTime;
    private String activityInfo;

   // @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private Blob displayedImage;
    @Ignore
    private Location activityLocation;
    @Ignore
    private List<Bitmap> activityPhotos;
    @Ignore
    private List<String> membersIds;

    @Ignore
    public Activity(int activityTypeId, Date creationTime, List<String> membersIds, String creatorId, boolean isPrivate, List<Bitmap> activityPhotos, Blob displayedImage, Location activityLocation, Date activityDateTime, String activityInfo) {
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
    public Activity(int activityTypeId, Date creationTime, String creatorId, boolean isPrivate, Date activityDateTime, String activityInfo, Date activityEndDateTime) {
        this.activityTypeId = activityTypeId;
        this.creationTime = creationTime;
        this.creatorId = creatorId;
        this.isPrivate = isPrivate;
        this.activityStartDateTime = activityDateTime;
        this.activityInfo = activityInfo;
        this.activityEndDateTime = activityEndDateTime;
    }

    public Activity(){
        this.membersIds = new ArrayList<String>();
        this.currMembers = 0;
        this.maxMembers = 0;
        this.minMembers = 0;
    }

    protected Activity(Parcel in) {
        id = in.readString();
        activityTypeId = in.readInt();
        creatorId = in.readString();
        minMembers = in.readInt();
        maxMembers = in.readInt();
        currMembers = in.readInt();
        isPrivate = in.readByte() != 0;
        activityInfo = in.readString();
        activityLocation = in.readParcelable(Location.class.getClassLoader());
        activityPhotos = in.createTypedArrayList(Bitmap.CREATOR);
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

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

    public List<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<String> membersIds) {
        this.membersIds = membersIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
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

    public Blob getDisplayedImage() {
        return displayedImage;
    }


    public void setDisplayedImage(Blob displayedImage) {
        this.displayedImage = displayedImage;
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView, Blob bitmap){
        if(bitmap != null)
            imageView.setImageBitmap(DataConverters.blobToBitmap(bitmap));
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(activityTypeId);
        dest.writeString(creatorId);
        dest.writeInt(minMembers);
        dest.writeInt(maxMembers);
        dest.writeInt(currMembers);
        dest.writeByte((byte) (isPrivate ? 1 : 0));
        dest.writeString(activityInfo);
        dest.writeParcelable(activityLocation, flags);
        dest.writeTypedList(activityPhotos);
    }
}
