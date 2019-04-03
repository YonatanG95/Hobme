package AppView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateActivityFragment extends Fragment {

    private FragmentCreateActivityBinding mFragmentCreateActivityBinding;

    public CreateActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentCreateActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_activity, container, false);

        return mFragmentCreateActivityBinding.getRoot();
    }

}
