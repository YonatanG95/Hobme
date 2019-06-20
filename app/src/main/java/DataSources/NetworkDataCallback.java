package DataSources;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import AppModel.Entity.Activity;

public interface NetworkDataCallback {

    void onCallback(List<Activity> activities, DocumentSnapshot documentSnapshot);
}
