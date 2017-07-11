package de.commune.organizer.communeorganizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tka on 11.07.2017.
 */

public class cleaningRoster extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    GridView simpleList;
    ArrayList<Item> animalList=new ArrayList<>();
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_roster);
        controller = this;
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);

        String userEmail = ((MyApplication) getApplication()).getUserEmail();

        simpleList = (GridView) findViewById(R.id.simpleGridView);

        try
        {
            task.execute("http://eddy-home.ddns.net/wg-app/cleaningRoster.php?Email=" + userEmail);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String s) {

        try
        {
            int id;
            String name;
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                animalList.add(new Item(row.getString("Description"),row.getString("UserEmail")));
            }
            MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,animalList);
            simpleList.setAdapter(myAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        task = new PostResponseAsyncTask(this);
    }
}