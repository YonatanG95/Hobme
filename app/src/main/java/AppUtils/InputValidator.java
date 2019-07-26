package AppUtils;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static boolean datesRangeValid(TextInputLayout sDate, TextInputLayout sTime,
                                          TextInputLayout eDate, TextInputLayout eTime){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date start;
        Date end;
        try{
            start = format.parse(sDate.getEditText().getText().toString() + " " + sTime.getEditText().getText().toString());
        }
        catch (Exception e){
            sDate.setError("Invalid date or time");
            sTime.setError("Invalid date or time");
            return false;
        }
        sDate.setError(null);
        sTime.setError(null);
        try{
            end = format.parse(eDate.getEditText().getText().toString() + " " + eTime.getEditText().getText().toString());
        }
        catch (Exception e){
            eDate.setError("Invalid date or time");
            eTime.setError("Invalid date or time");
            return false;
        }
        eDate.setError(null);
        eTime.setError(null);
        if (!end.after(start)){
            eDate.setError("End before start");
            eTime.setError("End before start");
            return false;
        }
        eDate.setError(null);
        eTime.setError(null);
        return true;
    }

    public static boolean numRangeValid(TextInputLayout min, TextInputLayout max){
        return true;
    }


}
