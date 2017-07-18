package de.commune.organizer.communeorganizer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

/**
 * Created by tka on 13.07.2017.
 */

public class CreateActivities_Activity extends AppCompatActivity implements AsyncResponse, DatePickerDialog.OnDateSetListener {
    public PostResponseAsyncTask task;
    public AppLibrary Lib;
    public AppCompatActivity controller;
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activities_layout);
        controller = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void init() {

        Intent intent = new Intent(CreateActivities_Activity.this, RefreshApp_Activity.class);
        startActivity(intent);

        Lib = new AppLibrary();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");

        final EditText descActivity = (EditText) findViewById(R.id.descActivity);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, CreateActivities_Activity.this, today.year, today.month, today.monthDay);

        final EditText dateActivity = (EditText) findViewById(R.id.dateActivity);
        dateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        final EditText timeActivity = (EditText) findViewById(R.id.timeActivity);

        Button addActivity = (Button) findViewById(R.id.addActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lib.checkField(descActivity, "Feld darf nicht leer sein!")
                        & Lib.checkField(dateActivity, "Feld darf nicht leer sein!")
                        & Lib.checkField(timeActivity, "Feld darf nicht leer sein!")
                        ) {

                    try {
                        task.execute("http://eddy-home.ddns.net/wg-app/activities.php?Method=createActivityEntry&CommuneID="
                                + communeID + "&Description=" + descActivity.getText() + "&Date=" + dateActivity.getText() + "&Time=" + timeActivity.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void processFinish(String s) {
        if (s.equals("entryCreated")) {
            finish();
            Intent intent = new Intent(CreateActivities_Activity.this, Activities_Activity.class);
            startActivity(intent);
        }
        task = new PostResponseAsyncTask(this);
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        final EditText dateActivity = (EditText) findViewById(R.id.dateActivity);
        dateActivity.setText(formatDate(i2, i1, i));
    }


}