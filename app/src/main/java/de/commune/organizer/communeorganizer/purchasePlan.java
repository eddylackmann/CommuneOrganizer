package de.commune.organizer.communeorganizer;

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

public class purchasePlan extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    GridView purchPlanList;
    ArrayList<Item> purchPlanListEntries=new ArrayList<>();
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_plan);
        controller = this;
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        purchPlanList = (GridView) findViewById(R.id.purchPlan_GridView);

        try
        {
            task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=getAllPurchasePlanEntries&CommuneID="
                    + ((MyApplication) this.getApplication()).getInformation("CommuneID"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Button purchPlan_AddBtn = (Button)findViewById(R.id.purchPlan_AddBtn);
        purchPlan_AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                purchPlanListEntries.add(new Item(row.getString("LineNo"),row.getString("Description")));
            }
            MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,purchPlanListEntries);
            purchPlanList.setAdapter(myAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        task = new PostResponseAsyncTask(this);
    }
}