package AppModel.Entity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User implements Parcelable {

    @PrimaryKey @NonNull
    private String id;
    private String fullName;
    private String email;
    private Date birthDate;
    private String fbDocId;

    @Embedded (prefix = "currPlace_")
    private SimplePlace currLocation;
    @Ignore
    private Bitmap profilePicture;
    @Ignore
    private List<Bitmap> userPhotos;
    @Ignore
    private List<String> favoriteTypesIds;
    @Ignore
    private List<String> myActivitiesIds;
    @Ignore
    private List<String> activitiesMemberIds;

    @Ignore
    public User(String id, String fullName, Date birthDate, Bitmap profilePicture, List<Bitmap> userPhotos, List<String> favoriteTypesIds, List<String> myActivitiesIds, List<String> activitiesMemberIds) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.userPhotos = userPhotos;
        this.favoriteTypesIds = favoriteTypesIds;
        this.myActivitiesIds = myActivitiesIds;
        this.activitiesMemberIds = activitiesMemberIds;
    }

    @Ignore
    public User(String id, String fullName, Date birthDate){
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public User(){
        this.favoriteTypesIds = new ArrayList<String>();
        this.myActivitiesIds = new ArrayList<String>();
        this.activitiesMemberIds = new ArrayList<String>();
    }

    protected User(Parcel in) {
        id = in.readString();
        fullName = in.readString();
        email = in.readString();
        profilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        userPhotos = in.createTypedArrayList(Bitmap.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeParcelable(profilePicture, flags);
        dest.writeTypedList(userPhotos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Bitmap> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(List<Bitmap> userPhotos) {
        this.userPhotos = userPhotos;
    }

    public List<String> getFavoriteTypesIds() {
        return favoriteTypesIds;
    }

    public void setFavoriteTypesIds(List<String> favoriteTypesIds) {
        this.favoriteTypesIds = favoriteTypesIds;
    }

    public List<String> getMyActivitiesIds()
    {
        return myActivitiesIds;
    }

    public void setMyActivitiesIds(List<String> myActivitiesIds) {
        this.myActivitiesIds = myActivitiesIds;
    }

    public List<String> getActivitiesMemberIds() {
        return activitiesMemberIds;
    }

    public void setActivitiesMemberIds(List<String> activitiesMemberIds) {
        this.activitiesMemberIds = activitiesMemberIds;
    }

    public String getFbDocId() {
        return fbDocId;
    }

    public void setFbDocId(String fbDocId) {
        this.fbDocId = fbDocId;
    }

    public SimplePlace getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(SimplePlace currLocation) {
        this.currLocation = currLocation;
    }
}
