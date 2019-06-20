package AppView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.LayoutActivityRowItemBinding;

import java.util.ArrayList;
import java.util.List;

import AppModel.Entity.Activity;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityAdapter extends PagedListAdapter<Activity, ActivityAdapter.ActivityHolder> {

    private final String TAG = "ActivityAdapter";
    private LayoutInflater layoutInflater;
    private List<Activity> activityList = new ArrayList<>();
//    private CustomOnItemClickListener mListener;

    protected ActivityAdapter()
    {
        super(DIFF_CALLBACK);
//        this.mListener = listener;
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
        return new ActivityHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        Activity activity = getItem(position);

        if (activity != null) {
            holder.binding.setActivity(activity);
            ActivityListFragmentDirections.ActListToDetails action = ActivityListFragmentDirections.actListToDetails(activity);
            holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action)); //{
        }
        else {
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

    class ActivityHolder extends RecyclerView.ViewHolder{

        private final LayoutActivityRowItemBinding binding;
//        CustomOnItemClickListener mListener;

        public ActivityHolder(View itemView) {
            super(itemView);
//            this.mListener = mListener;
            this.binding = DataBindingUtil.bind(itemView);
//            Activity activity = getItem(getAdapterPosition());
//            ActivityListFragmentDirections.ActListToDetails action = ActivityListFragmentDirections.actListToDetails(activity);
//            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action)); //{
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Activity act = getItem(position);
//                    mListener.openActivityDetails(v, act);
//                }
//            });
        }
    }

//    interface CustomOnItemClickListener{
//        void openActivityDetails(View view, Activity activity);
//    }

}
