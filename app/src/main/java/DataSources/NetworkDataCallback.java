package DataSources;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import AppModel.Entity.Activity;
import AppModel.Entity.User;

public interface NetworkDataCallback {

    void onCallback(List<Activity> activities);//, DocumentSnapshot documentSnapshot);

    void userCallback(User user);
}
