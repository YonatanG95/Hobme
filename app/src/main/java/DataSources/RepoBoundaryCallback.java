package DataSources;

import android.util.Log;
import AppModel.Entity.Activity;
import AppModel.Dao.ActivityDao;
import AppUtils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Date;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Activity> {

    private boolean isLoading = false;
    private final String TAG = "RepoBoundaryCallback";
    private static DocumentSnapshot lastPage;
    public static final int DATABASE_PAGE_SIZE = 15;
    private AppRepository repository;


    /**
     * Constructor
     * @param repository - repository instance
     */
    public RepoBoundaryCallback(AppRepository repository) {
        this.repository = repository;
        onZeroItemsLoaded();
    }

    /**
     * Fetches future activities when paged list is empty
     */
    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        Date d = new Date();
        repository.fetchMoreActivities(AppUtils.DataConverters.toDate(d.getTime()));
    }

    /**
     * Fetches more activities when list reaches its top
     * @param itemAtFront
     */
    @Override
    public void onItemAtFrontLoaded(@NonNull Activity itemAtFront) {
        Log.d(TAG, "onItemAtFrontLoaded");
//        repository.fetchMoreActivities(AppUtils.DataConverters.toDate(ite);
    }

    /**
     * Fetches more activities when list reaches its bottom
     * @param itemAtEnd
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Activity itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded");
        repository.fetchMoreActivities(itemAtEnd.getActivityStartDateTime());
    }
}
