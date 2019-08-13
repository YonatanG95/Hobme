package AppView;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import AppUtils.InjectorUtils;
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


//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(binding.drawerLayout)
//                        .build();



        Toolbar toolbar = binding.myToolbar;
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle (this, binding.drawerLayout, toolbar, R.string.open, R.string.open);
//        //binding.drawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();

        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activities_fragment_container)).getNavController();
        NavigationView navView = binding.navView;
        NavigationUI.setupWithNavController(navView, navController);
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

//    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.top_bar_list, menu);
////        return true;
////    }

//    public void hideBottomNav()
//    {
//        binding.bottomNavigation.setVisibility(View.GONE);
//    }
//
//    public void showBottomNav(){
//        binding.bottomNavigation.setVisibility(View.VISIBLE);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment host = getSupportFragmentManager().findFragmentById(R.id.activities_fragment_container);
        Fragment fragmentById = host.getChildFragmentManager().getFragments().get(0);
        if (id == R.id.nav_home) {
            if (fragmentById instanceof  DetailedActivityFragment){
                navController.navigate(R.id.actDetailsToList);
            } else if (fragmentById instanceof  CreateActivityFragment){
                navController.navigate(R.id.actCreateToList);
            }
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {
            containerViewModel.logOut();
            if(fragmentById instanceof ActivityListFragment) {
                navController.navigate(R.id.actListToLogin);
            } else if (fragmentById instanceof  DetailedActivityFragment){
                navController.navigate(R.id.actDetailsToLogin);
            } else if (fragmentById instanceof  CreateActivityFragment){
                navController.navigate(R.id.actCreateToLogin);
            }
//            else{
//
//            }
//        } else if (id == R.id.nav_send) {
//
        }

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
