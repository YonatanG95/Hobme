package AppView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.hobme.R;
import com.project.hobme.databinding.UserLoginFragmentBinding;

import AppUtils.InjectorUtils;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.DetailedActivityViewModel;
import AppViewModel.UserLoginViewModel;

public class UserLoginFragment extends Fragment {

    private UserLoginViewModel mViewModel;
    private UserLoginFragmentBinding binding;
    private CustomViewModelFactory viewModelFactory;

    //private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;
//
//    public static UserLoginFragment newInstance() {
//        return new UserLoginFragment();
//    }
//
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_login_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserLoginViewModel.class);

//        mAuth = FirebaseAuth.getInstance();


        bindData();

        checkUserStatus();

        return binding.getRoot();
    }


    /**
     * Check if user already logged in - If so, move to main page
     */
    private void checkUserStatus() {
        if(mViewModel.isAlreadyLoggedIn()){
            NavController nc = (((NavHostFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.activities_fragment_container))
                    .getNavController());
            nc.navigate(R.id.loginToActList);
        }
    }

    private void bindData() {
        binding.setView(mViewModel);
        binding.setHandler(this);
        binding.setLifecycleOwner(this);
    }

    public void loginBtn(View view){
        mViewModel.userLogInEmail(this, view);
        //userSignInEmail();
    }

    public void registerBtn(View view){
        Navigation.findNavController(view).navigate(R.id.loginToRegister);
    }

//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(UserLoginViewModel.class);
//        // TODO: Use the ViewModel
////    }
//public void userSignInEmail(String email, String pass, View view){
//    mAuth.signInWithEmailAndPassword(email, pass)
//            .addOnCompleteListener(UserLoginFragment.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d("111", "signInWithEmail:success");
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Navigation.findNavController(view).navigate(R.id.loginToActList);
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w("222", "signInWithEmail:failure", task.getException());
//                        Toast.makeText(view.getContext(), "Authentication failed.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
}



