package AppModel.Entity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
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

    @Ignore
    private Bitmap profilePicture;
    @Ignore
    private List<Bitmap> userPhotos;
    @Ignore
    private List<Integer> favoriteTypesIds;
    @Ignore
    private List<Integer> myActivitiesIds;
    @Ignore
    private List<Integer> activitiesMemberIds;
    @Ignore
    private Location userLocation;

    @Ignore
    public User(String id, String fullName, Date birthDate, Bitmap profilePicture, List<Bitmap> userPhotos, List<Integer> favoriteTypesIds, List<Integer> myActivitiesIds, List<Integer> activitiesMemberIds, Location userLocation) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.userPhotos = userPhotos;
        this.favoriteTypesIds = favoriteTypesIds;
        this.myActivitiesIds = myActivitiesIds;
        this.activitiesMemberIds = activitiesMemberIds;
        this.userLocation = userLocation;
    }

    @Ignore
    public User(String id, String fullName, Date birthDate){
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public User(){}

    protected User(Parcel in) {
        id = in.readString();
        fullName = in.readString();
        email = in.readString();
        profilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        userPhotos = in.createTypedArrayList(Bitmap.CREATOR);
        userLocation = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeParcelable(profilePicture, flags);
        dest.writeTypedList(userPhotos);
        dest.writeParcelable(userLocation, flags);
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

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
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

    public List<Integer> getFavoriteTypesIds() {
        return favoriteTypesIds;
    }

    public void setFavoriteTypesIds(List<Integer> favoriteTypesIds) {
        this.favoriteTypesIds = favoriteTypesIds;
    }

    public List<Integer> getMyActivitiesIds() {
        return myActivitiesIds;
    }

    public void setMyActivitiesIds(List<Integer> myActivitiesIds) {
        this.myActivitiesIds = myActivitiesIds;
    }

    public List<Integer> getActivitiesMemberIds() {
        return activitiesMemberIds;
    }

    public void setActivitiesMemberIds(List<Integer> activitiesMemberIds) {
        this.activitiesMemberIds = activitiesMemberIds;
    }
}
