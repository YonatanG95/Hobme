package com.project.hobme;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserEntry
{
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private double rank;
    private double latitude;
    private double longitude;
    private String aboutMe;
    private List<String> favoriteHobieTypes;

    @Ignore
    private Bitmap picture;

    public UserEntry(int id, String firstName, String lastName, Date dateOfBirth, double rank, double latitude, double longitude, String aboutMe, List<String> favoriteHobieTypes, Bitmap picture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.rank = rank;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aboutMe = aboutMe;
        this.favoriteHobieTypes = favoriteHobieTypes;
        this.picture = picture;
    }

    @Ignore
    public UserEntry(String firstName, String lastName, Date dateOfBirth, double rank, double latitude, double longitude, String aboutMe, List<String> favoriteHobieTypes, Bitmap picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.rank = rank;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aboutMe = aboutMe;
        this.favoriteHobieTypes = favoriteHobieTypes;
        this.picture = picture;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public void setFavoriteHobieTypes(List<String> favoriteHobieTypes) {
        this.favoriteHobieTypes = favoriteHobieTypes;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
