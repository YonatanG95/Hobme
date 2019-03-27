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
    //private String aboutMe;


    @Ignore
    private Bitmap profilePicture;
    @Ignore
    private Bitmap picture1;
    @Ignore
    private Bitmap picture2;
    @Ignore
    private Bitmap picture3;
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
        //this.aboutMe = aboutMe;
        this.favoriteHobieTypes = favoriteHobieTypes;
        //this.picture = picture;
    }

    @Ignore
    public UserEntry(String fullName, Date dateOfBirth, double rank, double latitude, double longitude, String aboutMe, List<String> favoriteHobieTypes, Bitmap profilePicture, Bitmap picture1, Bitmap picture2, Bitmap picture3) {
        Log.d("Check","UE - UserEntry2");
        //this.firstName = firstName;
        //this.lastName = lastName;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.rank = rank;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.aboutMe = aboutMe;
        this.favoriteHobieTypes = favoriteHobieTypes;
        this.profilePicture = profilePicture;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
    }

//    public void setAboutMe(String aboutMe) {
//        this.aboutMe = aboutMe;
//    }

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public double getRank() {
        return rank;
    }

    public List<String> getFavoriteHobieTypes() {
        return favoriteHobieTypes;
    }

//    public String getAboutMe() {
//        return aboutMe;
//    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }


    public Bitmap getPicture1() {
        return picture1;
    }

    public Bitmap getPicture2() {
        return picture2;
    }

    public Bitmap getPicture3() {
        return picture3;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setPicture1(Bitmap picture1) {
        this.picture1 = picture1;
    }

    public void setPicture2(Bitmap picture2) {
        this.picture2 = picture2;
    }

    public void setPicture3(Bitmap picture3) {
        this.picture3 = picture3;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
