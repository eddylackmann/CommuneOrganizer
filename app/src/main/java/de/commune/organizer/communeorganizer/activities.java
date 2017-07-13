package de.commune.organizer.communeorganizer;

/**
 * Created by tka on 13.07.2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by tka on 11.07.2017.
 */

public class activities extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    GridView purchPlanList;
    ArrayList<Item> activitiesListEntries=new ArrayList<>();
    public AppCompatActivity controller;
    private String asyncTaskMethod = "";
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activties);
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
        activitiesListEntries.clear();
    }

    @Override
    public void processFinish(String s) {
        Intent intent;
        switch (asyncTaskMethod){

        }
        task = new PostResponseAsyncTask(this);
    }
}