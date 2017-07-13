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

public class createActivity extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);
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

        final EditText descActivity = (EditText) findViewById(R.id.descActivity);
        final EditText dateActivity = (EditText) findViewById(R.id.dateActivity);
        final EditText timeActivity = (EditText) findViewById(R.id.timeActivity);

        Button addActivity = (Button) findViewById(R.id.addActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    task.execute("http://eddy-home.ddns.net/wg-app/activities.php?Method=createActivityEntry&CommuneID="
                            + communeID + "&Description=" + descActivity.getText() +"&Date="+ dateActivity.getText() + "&Time=" + timeActivity.getText());
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
            Intent intent = new Intent(createActivity.this, activities.class);
            startActivity(intent);
        }
        task = new PostResponseAsyncTask(this);
    }
}