package DataSources;

import android.content.Context;
import android.util.Log;

import AppModel.Activity;
import AppModel.ActivityDao;
import AppUtils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.paging.PagedList;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Activity>{

    private AppExecutors executors;
    private ActivityDao activityDao;
    private NetworkData networkData;
    private boolean isLoading = false;
    private final String TAG = "RepoBoundaryCallback";
    private static DocumentSnapshot lastPage;

    public RepoBoundaryCallback(AppExecutors executors, NetworkData networkData, Context context){
        this.executors = executors;
        this.activityDao = AppDB.getInstance(context).activityDao();
        this.networkData = networkData;
    }

    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        fetchActivities();
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull Activity itemAtFront) {
        //super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Activity itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded");
        fetchActivities();
    }

    private void fetchActivities(){

        if(isLoading) return;
        isLoading = true;
        executors.networkIO().execute(()-> {
            networkData.getActivities(lastPage, new NetworkDataCallback() {
                @Override
                public void onCallback(List<Activity> activities, DocumentSnapshot documentSnapshot) {
                    lastPage = documentSnapshot;
                    Log.d(TAG, "In callback");
                    if(activities != null) {
                        executors.diskIO().execute(() -> {
                            activityDao.bulkInsertActivity(activities);
//                            for (Activity act : activities) {
//                                if(act.getActivityInfo() != null)
//                                    Log.d(TAG, "Image: " + act.getActivityInfo());
//                                else
//                                    Log.d(TAG, "Image: null");
//                            }
                            Log.d(TAG, "Activities inserted to DB");
                            isLoading = false;
                        });
                    }
                }
            });
        });
    }
}
