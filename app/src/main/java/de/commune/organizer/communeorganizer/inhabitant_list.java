package de.commune.organizer.communeorganizer;

/**
 * Created by Tom on 13.07.2017.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class inhabitant_list extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    GridView inhabitantList;
    ArrayList<Item> inhabitantListEntries=new ArrayList<>();
    public AppCompatActivity controller;
    private String asyncTaskMethod = "";
    private String communeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhabitant_list);
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
        inhabitantListEntries.clear();
        inhabitantList = (GridView) findViewById(R.id.inhabitantListGridView);
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
    public void processFinish(String s) {
        try
        {
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                inhabitantListEntries.add(new Item(row.getString("Firstname") + " | " + row.getString("Lastname"),row.getString("Email"),"",""));
            }
            MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,inhabitantListEntries);
            inhabitantList.setAdapter(myAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        task = new PostResponseAsyncTask(this);
    }
}
