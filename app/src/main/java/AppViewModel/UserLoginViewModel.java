package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppView.UserLoginFragment;
import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private AppRepository repository;
    public String password;
    public String email;

    public UserLoginViewModel(AppRepository repository){
        this.repository = repository;
    }

    public boolean isAlreadyLoggedIn(){
        return repository.currentlyLoggedIn();
    }

    public void userLogInEmail(UserLoginFragment fragment, View view){
        repository.signInUserEmail(email, password, view, fragment);
    }
}
