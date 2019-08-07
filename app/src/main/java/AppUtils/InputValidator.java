package AppUtils;

import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Application forms and fields input validation
 */
public class InputValidator {

    /**
     * Validates a password (minimum length)
     * @param editText
     * @return True if password is valid (by conditions). Else - returns false
     */
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

    /**
     * Validates a field (no null)
     * @param editText
     * @return True if field has data. Else - returns false
     */
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

    /**
     * Validates email input (email format)
     * @param editText
     * @return True if email is in the right format. Else - returns false
     */
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

    /**
     * Validates a dates range
     * @param sDate
     * @param sTime
     * @param eDate
     * @param eTime
     * @return True if the dates are not null and the first date is earlier than the second.
     * Else - returns false
     */
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

    public static boolean locationValid(TextInputLayout inputLayout, boolean isSet){
        if(!isSet){
            inputLayout.setError("Location is missing");
            return false;
        }
        else{
            inputLayout.setError(null);
            return true;
        }
    }

}
