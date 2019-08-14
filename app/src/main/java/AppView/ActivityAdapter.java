package AppView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hobme.R;
import com.project.hobme.databinding.LayoutActivityRowItemBinding;

import java.text.DecimalFormat;

import AppModel.Entity.Activity;
import AppModel.Entity.SimplePlace;
import AppModel.Entity.User;
import AppUtils.LocationUtils;

/**
 * Presents a row in the recycler view of activities
 */
public class ActivityAdapter extends PagedListAdapter<Activity, ActivityAdapter.ActivityHolder> {

    private final String TAG = "ActivityAdapter";
    private LayoutInflater layoutInflater;
    private User currUser;

    protected ActivityAdapter()
    {
        super(DIFF_CALLBACK);
    }

    /**
     * Inflates single activity UI
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutActivityRowItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_activity_row_item, parent, false);
        return new ActivityHolder(binding.getRoot());
    }

    /**
     * Binds a row with an activity with additional information presented
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        Activity activity = getItem(position);

        if (activity != null) {

            //Calculates the distance between user's location and a single activity
            //and presents in the row UI
            SimplePlace actLoc = activity.getSimplePlace();
            SimplePlace userLoc = getCurrUser().getCurrLocation();
            if(userLoc != null) {
                double dist = LocationUtils.calcDistance(actLoc.getLatitude(), actLoc.getLongitude(), userLoc.getLatitude(), userLoc.getLongitude());
                holder.binding.setDistance(new DecimalFormat("###.#").format(dist));
            }
            else
            {
                holder.binding.setDistance("--");
            }
            holder.binding.setActivity(activity);

            //Creates a listener for each activity row to move to the detailed activity
            ActivityListFragmentDirections.ActListToDetails action = ActivityListFragmentDirections.actListToDetails(activity, currUser);
            holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action)); //{
        }
        else {
            holder.binding.setActivity(null);
        }
    }

    /**
     * Determines whether a row should be updated based on its data
     */
    public static final DiffUtil.ItemCallback<Activity> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Activity>() {
            @Override
            public boolean areItemsTheSame(@NonNull Activity oldItem, @NonNull Activity newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Activity oldItem, @NonNull Activity newItem) {
                return oldItem.equals(newItem);
            }
    };


    /**
     * Binds the layout (row) with necessary data
     */
    class ActivityHolder extends RecyclerView.ViewHolder{

        private final LayoutActivityRowItemBinding binding;

        public ActivityHolder(View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }
    }

    /**
     *
     * @return the current app user
     */
    public User getCurrUser() {
        return currUser;
    }

    /**
     * Sets the current app user, to be passed later to the detailed activity
     * @param currUser
     */
    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

}
