package AppModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserActivityJoinDao {

    @Query("SELECT * FROM user_table INNER JOIN user_activity_join_table ON id = userId WHERE activityId = :activityId")
    LiveData<List<User>> getUsersFromActivity(final int activityId);

    @Query("SELECT * FROM activity_table INNER JOIN user_activity_join_table ON id = activityId WHERE userId = :userId")
    LiveData<List<Activity>> getActivitiesFromUser(final int userId);
}
