package AppView;


import android.os.Bundle;
import android.view.MenuItem;

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

import com.google.android.material.navigation.NavigationView;
import com.project.hobme.R;
import com.project.hobme.databinding.ActivityActivitiesFragmentsContainerBinding;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_fragments_container);

        factory = InjectorUtils.provideViewModelFactory(this);
        containerViewModel = ViewModelProviders.of(this, factory)
                .get(ContainerViewModel.class);

        bindData();
        initializeUI();
    }

    /**
     * Handles UI modifications
     */
    private void initializeUI(){
        Toolbar toolbar = binding.myToolbar;
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //Set navigation drawer
        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activities_fragment_container)).getNavController();
        NavigationView navView = binding.navView;
        NavigationUI.setupWithNavController(navView, navController);
        navView.setNavigationItemSelectedListener(this);
    }

    /**
     * Set databinding parameters
     */
    private void bindData() {
        binding.setLifecycleOwner(this);
    }

    /**
     * Handles selection on navigation drawer
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Get the calling fragment
        Fragment host = getSupportFragmentManager().findFragmentById(R.id.activities_fragment_container);
        Fragment fragmentById = host.getChildFragmentManager().getFragments().get(0);

//        //Home button - return to activities list
//        if (id == R.id.nav_home) {
//            if (fragmentById instanceof  DetailedActivityFragment){
//                navController.navigate(R.id.actDetailsToList);
//            } else if (fragmentById instanceof  CreateActivityFragment){
//                navController.navigate(R.id.actCreateToList);
//            }

        //Sign out button - end user's firebase session
        //} else
        if (id == R.id.nav_logout) {
            containerViewModel.logOut();
            if(fragmentById instanceof ActivityListFragment) {
                navController.navigate(R.id.actListToLogin);
            } else if (fragmentById instanceof  DetailedActivityFragment){
                navController.navigate(R.id.actDetailsToLogin);
            } else if (fragmentById instanceof  CreateActivityFragment){
                navController.navigate(R.id.actCreateToLogin);
            }
        }

        //Close drawer on selection
        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
