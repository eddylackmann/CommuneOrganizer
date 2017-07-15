package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;

/**
 * Created by Tom on 11.07.2017.
 */

public class activity_userInfo extends AppCompatActivity implements AsyncResponse {
    private my_Library Lib = new my_Library();
    private String test = new String();
    private activity_userInfo c;
    private String asyncTaskMethod ="";
    public PostResponseAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        c = this;
        init();
        setLayout();
    }

    private void setLayout(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private  void  init(){
        final TextView infoemailText = (TextView) findViewById(R.id.infoEmailTextInfo);
        //final TextView infoPWTextInfo = (TextView) findViewById(R.id.infoPWTextInfo);
        final TextView infoFirstnameTextInfo = (TextView) findViewById(R.id.infoFirstnameTextInfo);
        final TextView infoLastnameTextInfo = (TextView) findViewById(R.id.infoLastnameTextInfo);
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);

        infoemailText.setText(((MyApplication) this.getApplication()).getInformation("Email"));
        //infoPWTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Password"));
        infoFirstnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Firstname"));
        infoLastnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Lastname"));
        infoBirthdayTextInfo.setText(((MyApplication) this.getApplication()).getInformation("formatted_date"));

        final Button changeInfoBtn = (Button)findViewById(R.id.changeInfoBtn);
        changeInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUserInformation();
            }
        });
        final Button leaveBtn = (Button)findViewById(R.id.user_info_leaveBtn);
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LeaveCommune();
            }
        });

        final Button deleteBtn = (Button) findViewById(R.id.user_info_deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUser();
            }
        });

    }

    private void ChangeUserInformation(){
        final String Email =((MyApplication) this.getApplication()).getInformation("Email");
        final TextView infoPWTextInfo = (TextView) findViewById(R.id.infoPWTextInfo);
        final TextView infoFirstnameTextInfo = (TextView) findViewById(R.id.infoFirstnameTextInfo);
        final TextView infoLastnameTextInfo = (TextView) findViewById(R.id.infoLastnameTextInfo);
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);
        final String oldPW = ((MyApplication) this.getApplication()).getInformation("Password");

        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,R.style.AlertDialogCustom));
        builder.setMessage("Benutzerinformationen speichern? ")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod ="updateUser";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod
                                    +"&Email="+Email
                                    +"&Password="+infoPWTextInfo.getText()
                                    +"&Firstname="+infoFirstnameTextInfo.getText()
                                    +"&Lastname="+infoLastnameTextInfo.getText()
                                    +"&Birthday="+infoBirthdayTextInfo.getText()
                                    +"&oldPW=" + oldPW);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    private void LeaveCommune(){
        final String CommuneID =((MyApplication) this.getApplication()).getInformation("CommuneID");
        final String Email =((MyApplication) this.getApplication()).getInformation("Email");
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,R.style.AlertDialogCustom));
        builder.setMessage("Möchten Sie die WG verlassen?")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod ="exitCommune";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method="+ asyncTaskMethod + "&CommuneID=" + CommuneID + "&Email=" +Email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    private void DeleteUser(){
        final String Email =((MyApplication) this.getApplication()).getInformation("Email");
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,R.style.AlertDialogCustom));
        builder.setMessage("Möchten Sie Ihren Account löschen?")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod ="deleteUser";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&Email=" +Email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void processFinish(String s) {
        Intent intent;
        switch (asyncTaskMethod){
            case "updateUser":
                switch (s)
                {
                    case "userUpdated":
                        finish();
                        intent = new Intent(activity_userInfo.this, Home.class);
                        startActivity(intent);
                        break;
                }
                break;
            case "exitCommune":
                switch (s)
                {
                    case "exitSuccessful":
                        finish();
                        intent = new Intent(activity_userInfo.this, createOrJoinCommune.class);
                        startActivity(intent);
                        Lib.showMessage("WG erfolgreich verlassen.",this);
                        break;
                    case "communeDeletedexitSuccessful":
                        finish();
                        intent = new Intent(activity_userInfo.this, createOrJoinCommune.class);
                        startActivity(intent);
                        Lib.showMessage("WG erfolgreich verlassen. Die WG wurde gelöscht.",this);
                        break;
                }
                break;
            case "deleteUser":
                switch (s)
                {
                    case "userDeleted":
                        finish();
                        intent = new Intent(activity_userInfo.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("Benutzeraccount gelöscht.",this);
                        break;
                    case "exitSuccessfuluserDeleted":
                        finish();
                        intent = new Intent(activity_userInfo.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("WG verlassen und Benutzeraccount gelöscht.",this);
                        break;
                    case "communeDeletedexitSuccessfuluserDeleted":
                        finish();
                        intent = new Intent(activity_userInfo.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("WG verlassen, gelöscht und Benutzeraccount gelöscht.",this);
                        break;
                }
        }
        task = new PostResponseAsyncTask(c);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
