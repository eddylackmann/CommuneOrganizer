package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.w3c.dom.Text;

/**
 * Created by Tom on 11.07.2017.
 */

public class activity_userInfo extends AppCompatActivity {

    private PostResponseAsyncTask task;
    private my_Library Lib = new my_Library();
    private String test = new String();
    private activity_userInfo c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
        setLayout();
    }

    private void setLayout(){


    }

    private  void  init(){
        final TextView infoemailText = (TextView) findViewById(R.id.infoEmailTextInfo);
        final TextView infoPWTextInfo = (TextView) findViewById(R.id.infoPWTextInfo);
        final TextView infoFirstnameTextInfo = (TextView) findViewById(R.id.infoFirstnameTextInfo);
        final TextView infoLastnameTextInfo = (TextView) findViewById(R.id.infoLastnameTextInfo);
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);

        infoemailText.setText(((MyApplication) this.getApplication()).getInformation("Email"));
        infoPWTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Password"));
        infoFirstnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Firstname"));
        infoLastnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Lastname"));
        infoBirthdayTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Birthday"));


        final Button leaveBtn = (Button)findViewById(R.id.user_info_leaveBtn);


        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               LeaveCommune();


            }
        });
    }

    private void LeaveCommune(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Messag");
        alert.setTitle("Enter Your Title");
        edittext.setTextColor(Color.WHITE);
        alert.setView(edittext);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String YouEditTextValue = edittext.getText().toString();
                test = YouEditTextValue;
                Lib.showMessage(test,c);
            }
        });

        alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
}
