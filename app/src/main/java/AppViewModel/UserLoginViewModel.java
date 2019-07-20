package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppView.UserLoginFragment;
import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel {

    private AppRepository repository;
    private String password;
    private String email;

    public UserLoginViewModel(AppRepository repository){
        this.repository = repository;
    }

    public void isAlreadyLoggedIn(View view){
        repository.currentlyLoggedIn(view);
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

    public void setEmail(String email)
    {
        this.email = email;
    }
}
