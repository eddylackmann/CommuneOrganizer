package de.commune.organizer.communeorganizer;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ray on 04.07.2017.
 */

public class AppLibrary {

    public void showMessage(String s, AppCompatActivity a) {
        AlertDialog.Builder AlertBox = new AlertDialog.Builder(new ContextThemeWrapper(a, R.style.AlertDialogCustom));
        AlertBox.setTitle("Commune Organizer");
        AlertBox.setMessage(s);
        AlertBox.create();
        AlertBox.show();
    }

    public boolean checkField(EditText edit, String ErrorText) {

        if (edit.getText().toString().equals("")) {
            edit.setError(ErrorText);
            return false;
        } else {
            return true;
        }

    }


    public boolean validate_Email(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
