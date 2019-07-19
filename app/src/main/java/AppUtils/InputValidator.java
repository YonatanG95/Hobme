package AppUtils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class InputValidator {

    public static boolean isPasswordValid(TextInputLayout editText) {
        int minimumLength = 5;
        String text = editText.getEditText().getText().toString();
        if (!TextUtils.isEmpty(text)) {
            if (text.length() < minimumLength) {
                editText.setError("Password must be minimum " + minimumLength + " characters length");
                return false;
            }
            else {
                editText.setError(null);
                return true;
            }
        }
        return false;
    }

    public static boolean isValidField(TextInputLayout editText){
        String text = editText.getEditText().getText().toString();
        if(TextUtils.isEmpty(text)){
            editText.setError("Field can't be empty");
            return false;
        }
        else {
            editText.setError(null);
            return true;
        }
    }

    public static boolean isEmailValid(TextInputLayout editText) {
        String text = editText.getEditText().getText().toString();
        if (!TextUtils.isEmpty(text)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                editText.setError("Invalid email address");
                return false;
            }
            else {
                editText.setError(null);
                return true;
            }
        }
        return false;
    }


}
