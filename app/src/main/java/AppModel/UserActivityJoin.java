package AppModel;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "user_activity_join_table", primaryKeys = {"userId", "activityId"},
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"),
        @ForeignKey(entity = Activity.class, parentColumns = "id", childColumns = "activityId")})
public class UserActivityJoin {

    private final int userId;
    private final int activityId;

    public UserActivityJoin(final int userId, final int activityId){
        this.activityId = activityId;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getActivityId() {
        return activityId;
    }
}
