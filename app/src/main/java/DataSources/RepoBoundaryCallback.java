package DataSources;

import android.content.Context;
import android.util.Log;

import AppModel.Entity.Activity;
import AppModel.Dao.ActivityDao;
import AppUtils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.paging.PagedList;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Activity> {

    private AppExecutors executors;
    private ActivityDao activityDao;
    private NetworkData networkData;
    private boolean isLoading = false;
    private final String TAG = "RepoBoundaryCallback";
    private static DocumentSnapshot lastPage;
    public static final int DATABASE_PAGE_SIZE = 15;
    private AppRepository repository;

    public RepoBoundaryCallback(AppRepository repository) {
        this.repository = repository;
        //onZeroItemsLoaded();
    }//AppExecutors executors, NetworkData networkData, Context context){
//        this.executors = executors;
//        this.activityDao = AppDB.getInstance(context).activityDao();
//        this.networkData = networkData;
//    }

    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        //fetchActivities();
        Date d = new Date();
        repository.fetchMoreActivities(AppUtils.DataConverters.toDate(d.getTime()));
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull Activity itemAtFront) {
        Log.d(TAG, "onItemAtFrontLoaded");
        //super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Activity itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded");
        //fetchActivities();
    }

//    private void fetchActivities(){
//
//        if(isLoading) return;
//        isLoading = true;
//        executors.networkIO().execute(()-> {
//            networkData.getActivities(lastPage, new NetworkDataCallback() {
//                @Override
//                public void onCallback(List<Activity> activities, DocumentSnapshot documentSnapshot) {
//                    lastPage = documentSnapshot;
//                    Log.d(TAG, "In callback");
//                    if(activities != null) {
//                        executors.diskIO().execute(() -> {
//                            activityDao.insertActivities(activities);
////                            for (Activity act : activities) {
////                                if(act.getActivityInfo() != null)
////                                    Log.d(TAG, "Image: " + act.getActivityInfo());
////                                else
////                                    Log.d(TAG, "Image: null");
////                            }
//                            Log.d(TAG, "Activities inserted to DB");
//                            isLoading = false;
//                        });
//                    }
//                }
//            });
//        });
//    }
}
