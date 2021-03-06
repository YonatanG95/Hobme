package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppView.UserRegisterFragment;
import DataSources.AppRepository;

public class UserRegisterViewModel extends ViewModel {

    private AppRepository repository;
    private String email;
    private String password;
    private String displayName;

    /**
     * Constructor
     * @param repository  - repository instance
     */
    public UserRegisterViewModel(AppRepository repository){
        this.repository = repository;
    }

    /**
     * Handles register button press - Firebase create user with email.
     * On result - navigation to  ActivityListFragment
     * @param view
     */
    public void createUserEmail(UserRegisterFragment fragment, View view){
        repository.createUserEmail(email, password, displayName, view, fragment);
    }

    //region Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    //endregion
}
