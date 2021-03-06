package de.commune.organizer.communeorganizer;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tom on 11.07.2017.
 */

public class UserInfo_Activity extends AppCompatActivity implements AsyncResponse, DatePickerDialog.OnDateSetListener {
    private AppLibrary Lib = new AppLibrary();
    private UserInfo_Activity c;
    private Spinner respList;
    private ArrayList<String> respListItems = new ArrayList<String>();
    private String asyncTaskMethod = "";
    private PostResponseAsyncTask task;
    private String communeID;
    private String CommuneAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        c = this;
        init();
        setLayout();
        getUserList();
    }

    private void getUserList() {
        task = new PostResponseAsyncTask(c);
        try {
            asyncTaskMethod = "getCommuneInhabitants";
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&CommuneID=" + communeID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLayout() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        Intent intent = new Intent(UserInfo_Activity.this, RefreshApp_Activity.class);
        startActivity(intent);

        final TextView infoemailText = (TextView) findViewById(R.id.infoEmailTextInfo);
        //final TextView infoPWTextInfo = (TextView) findViewById(R.id.infoPWTextInfo);
        final TextView infoFirstnameTextInfo = (TextView) findViewById(R.id.infoFirstnameTextInfo);
        final TextView infoLastnameTextInfo = (TextView) findViewById(R.id.infoLastnameTextInfo);
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);

        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        CommuneAdmin = ((MyApplication) this.getApplication()).getInformation("CommuneAdmin");
        respList = (Spinner) findViewById(R.id.newAdmin);
        final Spinner newAdminValue = (Spinner) findViewById(R.id.newAdmin);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, UserInfo_Activity.this, today.year, today.month, today.monthDay);

        infoBirthdayTextInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        infoemailText.setText(((MyApplication) this.getApplication()).getInformation("Email"));
        //infoPWTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Password"));
        infoFirstnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Firstname"));
        infoLastnameTextInfo.setText(((MyApplication) this.getApplication()).getInformation("Lastname"));
        infoBirthdayTextInfo.setText(((MyApplication) this.getApplication()).getInformation("formatted_date"));


        final Button changeInfoBtn = (Button) findViewById(R.id.changeInfoBtn);
        changeInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUserInformation();
            }
        });
        final Button leaveBtn = (Button) findViewById(R.id.user_info_leaveBtn);
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

        Button TransferAdminStatus = (Button) findViewById(R.id.user_info_transferAdminStatusBtn);
        TransferAdminStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommuneAdmin.equals("1")) {
                    task = new PostResponseAsyncTask(c);
                    try {
                        asyncTaskMethod = "transferAdminStatus";
                        task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&FromEmail=" + infoemailText.getText() +
                                "&ToEmail=" + newAdminValue.getSelectedItem().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Lib.showMessage("Sie sind nicht berechtigt.", c);
                }
            }
        });
    }

    private void ChangeUserInformation() {
        final String Email = ((MyApplication) this.getApplication()).getInformation("Email");
        final TextView infoPWTextInfo = (TextView) findViewById(R.id.infoPWTextInfo);
        final TextView infoFirstnameTextInfo = (TextView) findViewById(R.id.infoFirstnameTextInfo);
        final TextView infoLastnameTextInfo = (TextView) findViewById(R.id.infoLastnameTextInfo);
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);
        final String oldPW = ((MyApplication) this.getApplication()).getInformation("Password");

        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AlertDialogCustom));
        builder.setMessage("Benutzerinformationen speichern? ")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod = "updateUser";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod
                                    + "&Email=" + Email
                                    + "&Password=" + infoPWTextInfo.getText()
                                    + "&Firstname=" + infoFirstnameTextInfo.getText()
                                    + "&Lastname=" + infoLastnameTextInfo.getText()
                                    + "&Birthday=" + infoBirthdayTextInfo.getText()
                                    + "&oldPW=" + oldPW);
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

    private void LeaveCommune() {
        final String CommuneID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        final String Email = ((MyApplication) this.getApplication()).getInformation("Email");
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AlertDialogCustom));
        builder.setMessage("Möchten Sie die WG verlassen?")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod = "exitCommune";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&CommuneID=" + CommuneID + "&Email=" + Email);
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

    private void DeleteUser() {
        final String Email = ((MyApplication) this.getApplication()).getInformation("Email");
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AlertDialogCustom));
        builder.setMessage("Möchten Sie Ihren Account löschen?")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task = new PostResponseAsyncTask(c);
                        try {
                            asyncTaskMethod = "deleteUser";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&Email=" + Email);
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
        switch (asyncTaskMethod) {
            case "updateUser":
                switch (s) {
                    case "userUpdated":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, Home_Activity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case "exitCommune":
                switch (s) {
                    case "exitSuccessful":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, CreateJoinCommune_Activity.class);
                        startActivity(intent);
                        Lib.showMessage("WG erfolgreich verlassen.", this);
                        break;
                    case "communeDeletedexitSuccessful":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, CreateJoinCommune_Activity.class);
                        startActivity(intent);
                        Lib.showMessage("WG erfolgreich verlassen. Die WG wurde gelöscht.", this);
                        break;
                }
                break;
            case "deleteUser":
                switch (s) {
                    case "userDeleted":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("Benutzeraccount gelöscht.", this);
                        break;
                    case "exitSuccessfuluserDeleted":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("WG verlassen und Benutzeraccount gelöscht.", this);
                        break;
                    case "communeDeletedexitSuccessfuluserDeleted":
                        finish();
                        intent = new Intent(UserInfo_Activity.this, MainActivity.class);
                        startActivity(intent);
                        Lib.showMessage("WG verlassen, gelöscht und Benutzeraccount gelöscht.", this);
                        break;
                }
            case "getCommuneInhabitants":
                try {
                    String email = ((MyApplication) this.getApplication()).getInformation("Email");
                    respListItems.clear();
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        if (!row.getString("Email").equals(email)) {
                            respListItems.add(row.getString("Email"));
                        }

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, respListItems);
                    respList.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "transferAdminStatus":
                if (s.equals("adminStatusTransfered")) {
                    finish();
                    intent = new Intent(UserInfo_Activity.this, Home_Activity.class);
                    startActivity(intent);
                }
                break;
        }
        task = new PostResponseAsyncTask(c);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        final TextView infoBirthdayTextInfo = (TextView) findViewById(R.id.infoBirthdayTextInfo);
        infoBirthdayTextInfo.setText(formatDate(i2, i1, i));
    }

    private String formatDate(int day, int month, int year) {
        String sMonth;
        String sDay;
        month++;

        if (day < 10) {
            sDay = "0" + day;
        } else {
            sDay = Integer.toString(day);
        }

        if (month < 10) {
            sMonth = "0" + month;
        } else {
            sMonth = Integer.toString(month);
        }
        return sDay + "." + sMonth + "." + year;
    }
}
