package AppView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.ActivityRowItemBinding;

import java.util.ArrayList;
import java.util.List;

import AppModel.Activity;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    private LayoutInflater layoutInflater;
    private List<Activity> activityList = new ArrayList<>();

    public ActivityAdapter()
    {
        this.activityList = activityList;
    }

    public void setActivities(List<Activity> activities){
        this.activityList = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ActivityRowItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_row_item, parent, false);
        return new ActivityHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        holder.binding.setActivity(activityList.get(position));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    class ActivityHolder extends RecyclerView.ViewHolder {

        private final ActivityRowItemBinding binding;

        public ActivityHolder(final ActivityRowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
