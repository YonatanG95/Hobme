package AppView;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.project.hobme.R;
import com.project.hobme.databinding.ActivityActivitiesFragmentsContainerBinding;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.ContainerViewModel;
import AppViewModel.CustomViewModelFactory;


public class ActivitiesFragmentsContainer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityActivitiesFragmentsContainerBinding binding;
    private CustomViewModelFactory factory;
    private ContainerViewModel containerViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_activities_fragments_container);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_fragments_container);

        bindData();

        factory = InjectorUtils.provideViewModelFactory(this);
        containerViewModel = ViewModelProviders.of(this, factory)
                .get(ContainerViewModel.class);

        navController = ((NavHostFragment) getSupportFragmentManager()
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
        navView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {
            containerViewModel.logOut();
            Fragment host = getSupportFragmentManager().findFragmentById(R.id.activities_fragment_container);
            Fragment fragmentById = host.getChildFragmentManager().getFragments().get(0);
            if(fragmentById instanceof ActivityListFragment) {
                navController.navigate(R.id.actListToLogin);
            } else if (fragmentById instanceof  DetailedActivityFragment){
                navController.navigate(R.id.actDetailsToLogin);
            } else if (fragmentById instanceof  CreateActivityFragment){
                navController.navigate(R.id.actCreateToList);
            }
            else{

            }
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
