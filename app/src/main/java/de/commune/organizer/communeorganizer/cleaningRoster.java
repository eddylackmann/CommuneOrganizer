package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tka on 11.07.2017.
 */

public class cleaningRoster extends AppCompatActivity implements AsyncResponse {
    private PostResponseAsyncTask task;
    private my_Library Lib;
    GridView cleaningPlanGridView;
    ArrayList<Item> cleaningPlanList=new ArrayList<>();
    private AppCompatActivity controller;
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_roster);
        controller = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        String userEmail = ((MyApplication) getApplication()).getUserEmail();
        cleaningPlanGridView = (GridView) findViewById(R.id.cleaningPlanGridView);
        try
        {
            task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=getAllCleaningPlansForUser&CommuneID="
                            + communeID + "&Email=" + userEmail);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Button cleaningRosterAddBtn = (Button) findViewById(R.id.cleaningRosterAddBtn);
        cleaningRosterAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(cleaningRoster.this, createCleaningPlan.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void processFinish(String s) {
        try
        {
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                cleaningPlanList.add(new Item(row.getString("FromDate") +" - " + row.getString("ToDate"),row.getString("Description"),row.getString("LineNo")));
            }
            MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,cleaningPlanList);
            cleaningPlanGridView.setAdapter(myAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        task = new PostResponseAsyncTask(this);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}