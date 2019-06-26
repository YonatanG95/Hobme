package AppView;


import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.project.hobme.R;
import com.project.hobme.databinding.ActivityActivitiesFragmentsContainerBinding;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class ActivitiesFragmentsContainer extends AppCompatActivity {

    private ActivityActivitiesFragmentsContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_activities_fragments_container);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_fragments_container);

        bindData();

        NavController navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activities_fragment_container)).getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(binding.drawerLayout)
                        .build();

        NavigationView navView = binding.navView;
        NavigationUI.setupWithNavController(navView, navController);

        Toolbar toolbar = binding.myToolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle (this, binding.drawerLayout, toolbar, R.string.open, R.string.open);
        //binding.drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
//        DrawerLayout drawerLayout = binding.drawerLayout;
//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, false, );
//        final ActionBar actionBar = getSupportActionBar();
//        if(actionBar!=null)
//        {
//            //actionBar.setHomeAsUpIndicator(android.R.drawable.ic_lock_lock);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }

//        if (findViewById(R.id.activities_fragment_container) != null) {
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            ActivityListFragment activityListFragment = new ActivityListFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.activities_fragment_container, activityListFragment).commit();
//        }
    }

    private void bindData() {

        binding.setLifecycleOwner(this);
    }

}
