package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

/**
 * Created by tka on 13.07.2017.
 */

public class createCleaningPlan extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cleaning_plan);
        controller = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");

        final EditText respCleanPlan = (EditText) findViewById(R.id.respCleanPlan);
        final EditText dateFromClean = (EditText) findViewById(R.id.dateFromClean);
        final EditText dateToClean = (EditText) findViewById(R.id.dateToClean);
        final EditText descCleanPlan = (EditText) findViewById(R.id.descCleanPlan);

        Button addCleanPlan = (Button) findViewById(R.id.addCleanPlan);
        addCleanPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=createCleaningPlanEntry&CommuneID="
                            + communeID + "&Email=" + respCleanPlan.getText() +"&FromDate="+ dateFromClean.getText() + "&ToDate="
                            + dateToClean.getText() + "&Description=" + descCleanPlan.getText());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void processFinish(String s) {
        if (s.equals("entryCreated")){
            finish();
            Intent intent = new Intent(createCleaningPlan.this, cleaningRoster.class);
            startActivity(intent);
        }
        task = new PostResponseAsyncTask(this);
    }
}