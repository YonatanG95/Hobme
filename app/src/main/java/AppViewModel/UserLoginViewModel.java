package AppViewModel;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModel;

import AppModel.Entity.User;
import AppView.UserLoginFragment;
import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel { // implements Observable {

    private AppRepository repository;
    private String password;
    private String email;
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    public UserLoginViewModel(AppRepository repository){
        this.repository = repository;
    }

    public void isAlreadyLoggedIn(View view){
        //repository.logOutUser();
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
        //registry.notifyChange(this, BR.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
        //registry.notifyChange(this, BR.email);
    }

//    @Override
//    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        registry.add(callback);
//    }
//
//    @Override
//    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        registry.remove(callback);
//    }


}
