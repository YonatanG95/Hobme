package AppView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.LayoutActivityRowItemBinding;

import java.util.ArrayList;
import java.util.List;

import AppModel.Activity;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityAdapter extends PagedListAdapter<Activity, ActivityAdapter.ActivityHolder> {

    private final String TAG = "ActivityAdapter";
    private LayoutInflater layoutInflater;
    private List<Activity> activityList = new ArrayList<>();

    protected ActivityAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setActivities(List<Activity> activities) {
        Log.d("Check", "" + getItemCount());
        this.activityList = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutActivityRowItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_activity_row_item, parent, false);
        return new ActivityHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        Activity activity = getItem(position);

        if (activity != null) {
//            if (activity.getDisplayedImage() != null)
//                Log.d(TAG, "Image: " + activity.getDisplayedImage().toString());
//            else
//                Log.d(TAG, "Image: " + "null");
            holder.binding.setActivity(activity);
        }
        else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.binding.setActivity(null);
        }
    }

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

    class ActivityHolder extends RecyclerView.ViewHolder {

        private final LayoutActivityRowItemBinding binding;

        public ActivityHolder(final LayoutActivityRowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

    }

}
