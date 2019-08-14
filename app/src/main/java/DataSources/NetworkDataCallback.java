package DataSources;

import java.util.List;
import AppModel.Entity.Activity;
import AppModel.Entity.ActivityType;
import AppModel.Entity.Category;
import AppModel.Entity.User;

/**
 * Class of callback interfaces. Using each of the interfaces to return data from remote DB
 */
public class NetworkDataCallback {

    public interface ActivityCallback {
        /**
         * Implement this to get a list of activities from remote DB
         * @param activities
         */
        void onActivityCallback(List<Activity> activities);
    }

    public interface UserCallback{
        /**
         * Implement this to get a user from remote DB
         * @param user
         */
        void onUserCallback(User user);
    }

    public interface CategoryCallback{
        /**
         * Implement this to get a list of categories from remote DB
         * @param categories
         */
        void onCategoryCallback(List<Category> categories);
    }

    public interface ActivityTypeCallback{
        /**
         * Implement this to get a list of activity types from remote DB
         * @param activityTypes
         */
        void onActivityTypeCallback(List<ActivityType> activityTypes);
    }
}
