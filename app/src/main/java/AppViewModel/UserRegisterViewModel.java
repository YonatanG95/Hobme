package AppViewModel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import AppModel.Entity.User;
import AppView.UserRegisterFragment;
import DataSources.AppRepository;

public class UserRegisterViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private AppRepository repository;
    private String email;
    private String password;
    private User user;
    private String displayName;


    public UserRegisterViewModel(AppRepository repository){
        this.repository = repository;
        this.user = new User();
    }

    public void createUserEmail(UserRegisterFragment fragment, View view){
        repository.createUserEmail(user, email, password, displayName, view, fragment);
    }

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
}
