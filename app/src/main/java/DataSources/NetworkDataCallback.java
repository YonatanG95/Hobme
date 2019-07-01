package DataSources;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import AppModel.Entity.Activity;
import AppModel.Entity.User;

public class NetworkDataCallback {

    public interface ActivityCallback {
        void onActivityCallback(List<Activity> activities);//, DocumentSnapshot documentSnapshot);
    }

    public interface UserIDCallback{
        void onUserIdCallback(String id, String displayName, String email);
    }

    public interface UserCallback{
        void onUserCallback(User user);
    }


}
