package com.project.hobme.Data.Database;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "activity")
public class ActivityEntry
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private Date creationDate;
    private Date activityDate;
    private double latitude;
    private double longitude;
    private int minParticipants;
    private int maxParticipants;
    private String description;

    @Ignore
    private Bitmap image;
    private UserEntry creator;
    private List<UserEntry> participants;

    public ActivityEntry(int id, String type, Date creationDate, Date activityDate, double latitude, double longitude, int minParticipants, int maxParticipants, String description) {
        this.id = id;
        this.type = type;
        this.creationDate = creationDate;
        this.activityDate = activityDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.description = description;
    }

    @Ignore
    public ActivityEntry(String type, Date creationDate, Date activityDate, double latitude, double longitude, int minParticipants, int maxParticipants, UserEntry creator, List<UserEntry> participants, Bitmap image, String description) {
        this.type = type;
        this.creationDate = creationDate;
        this.activityDate = activityDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.creator = creator;
        this.participants = participants;
        this.image = image;
        this.description = description;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setParticipants(List<UserEntry> participants) {
        this.participants = participants;
    }

    public void setCreator(UserEntry creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public UserEntry getCreator() {
        return creator;
    }

    public List<UserEntry> getParticipants() {
        return participants;
    }

}
