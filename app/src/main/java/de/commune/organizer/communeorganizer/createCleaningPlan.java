package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tka on 13.07.2017.
 */

public class createCleaningPlan extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    private String asyncTaskMethod;
    public AppCompatActivity controller;
    private String communeID;
    private Spinner respList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cleaning_plan);
        controller = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        getUserList();
    }

    private void getUserList(){

        try
        {
            asyncTaskMethod = "getCommuneInhabitants";
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void init(){

        Intent intent = new Intent(createCleaningPlan.this, RefreshAppGlobalInformation.class);
        startActivity(intent);

        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        respList= (Spinner) findViewById(R.id.respCleanPlan);

        final Spinner respCleanPlan = (Spinner) findViewById(R.id.respCleanPlan);
        final EditText dateFromClean = (EditText) findViewById(R.id.dateFromClean);
        final EditText dateToClean = (EditText) findViewById(R.id.dateToClean);
        final EditText descCleanPlan = (EditText) findViewById(R.id.descCleanPlan);

        Button addCleanPlan = (Button) findViewById(R.id.addCleanPlan);
        addCleanPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTaskMethod="createCleaningTast";
                try
                {
                    task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=createCleaningPlanEntry&CommuneID="
                            + communeID + "&Email=" + respCleanPlan.getSelectedItem().toString() +"&FromDate="+ dateFromClean.getText() + "&ToDate="
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

        switch (asyncTaskMethod) {
            case "createCleaningTast":

                if (s.equals("entryCreated")){
                    finish();
                    Intent intent = new Intent(createCleaningPlan.this, cleaningRoster.class);
                    startActivity(intent);
                }

                break;

            case "getCommuneInhabitants":

                try
                {
                    int n = Integer.parseInt( communeID = ((MyApplication) this.getApplication()).getInformation("NumberOfInhabitants"));
                    final String[] items = new String[n];
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        items[i] = row.getString("Email");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    respList.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                break;
        }




        task = new PostResponseAsyncTask(this);
    }
}