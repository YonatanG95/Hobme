package AppView;


import android.app.Activity;
import android.os.Bundle;

import com.project.hobme.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class ActivitiesFragmentsContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_fragments_container);

        if (findViewById(R.id.activities_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            ActivityListFragment activityListFragment = new ActivityListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.activities_fragment_container, activityListFragment).commit();
        }
    }
}
