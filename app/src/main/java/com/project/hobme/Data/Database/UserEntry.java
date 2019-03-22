package com.project.hobme.Data.Database;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "user")
public class UserEntry
{
    @PrimaryKey (autoGenerate = true)
    private int id;
    //private String firstName;
    //private String lastName;
    private String fullName;
    private Date dateOfBirth;
    private double rank;
    private double latitude;
    private double longitude;
    private String aboutMe;


    @Ignore
    private Bitmap picture;
    @Ignore
    private List<String> favoriteHobieTypes;

    public UserEntry(int id, String fullName, Date dateOfBirth, double rank, double latitude, double longitude, String aboutMe) {
        Log.d("Check","UE - UserEntry");
        this.id = id;
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.rank = rank;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aboutMe = aboutMe;
        this.favoriteHobieTypes = favoriteHobieTypes;
        this.picture = picture;
    }

    @Ignore
    public UserEntry(String fullName, Date dateOfBirth, double rank, double latitude, double longitude, String aboutMe, List<String> favoriteHobieTypes, Bitmap picture) {
        Log.d("Check","UE - UserEntry2");
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.fullName = fullName;
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

//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public double getRank() {
        return rank;
    }

    public List<String> getFavoriteHobieTypes() {
        return favoriteHobieTypes;
    }

    public String getAboutMe() {
        return aboutMe;
    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }

}
