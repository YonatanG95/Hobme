package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppModel.Entity.User;
import AppView.UserLoginFragment;
import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private AppRepository repository;
    private String password;
    private String email;

    public UserLoginViewModel(AppRepository repository){
        this.repository = repository;
    }

    public boolean isAlreadyLoggedIn(){
        return repository.currentlyLoggedIn();
    }

    public void userLogInEmail(UserLoginFragment fragment, View view){
        repository.signInUserEmail(email, password, view, fragment);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
