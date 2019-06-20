package AppModel.Entity;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
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
    public User(int id, String fullName, Date birthDate, Bitmap profilePicture, List<Bitmap> userPhotos, List<Integer> favoriteTypesIds, List<Integer> myActivitiesIds, List<Integer> activitiesMemberIds, Location userLocation) {
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

    public User(int id, String fullName, Date birthDate){
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
