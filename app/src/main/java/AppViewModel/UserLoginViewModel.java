package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppView.UserLoginFragment;
import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel {

    private AppRepository repository;
    private String password;
    private String email;

    /**
     * Constructor
     * @param repository  - repository instance
     */
    public UserLoginViewModel(AppRepository repository){
        this.repository = repository;
    }

    /**
     * If user session exists - navigate directly to main page (ActivityListFragment)
     * @param view
     */
    public void isAlreadyLoggedIn(View view){
        repository.currentlyLoggedIn(view);
    }

    /**
     * Handles login button press - Firebase email login. On result - navigation to  ActivityListFragment
     * @param fragment
     * @param view
     */
    public void userLogInEmail(UserLoginFragment fragment, View view){
        repository.signInUserEmail(email, password, view, fragment);
    }


    //region Getters and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
    //endregion
}

