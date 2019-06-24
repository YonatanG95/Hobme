package DataSources;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import AppModel.Entity.Activity;
import AppModel.Entity.User;

public class NetworkDataCallback {

    public interface ActivityCallback {
        void onCallback(List<Activity> activities);//, DocumentSnapshot documentSnapshot);
    }


}
