package AppViewModel;

import androidx.lifecycle.ViewModel;

import DataSources.AppRepository;

public class UserLoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public UserLoginViewModel(AppRepository repository){}

    public boolean isAlreadyLoggedIn(){
        return true;
    }
}
