package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private String asyncTaskMethod = "";
    private String communeID;

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
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        purchPlanList = (GridView) findViewById(R.id.purchPlan_GridView);
        purchPlanListEntries.clear();

        try
        {
            asyncTaskMethod = "getAllPurchasePlanEntries";
            task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        final EditText purchPlan_DescriptionText = (EditText)findViewById(R.id.purchPlan_DescriptionText);
        Button purchPlan_AddBtn = (Button)findViewById(R.id.purchPlan_AddBtn);
        purchPlan_AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!purchPlan_DescriptionText.getText().equals("")){
                    try
                    {
                        asyncTaskMethod = "createPurchasePlanEntry";
                        task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID="
                                + communeID + "&Description=" + purchPlan_DescriptionText.getText());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finish();
                    Intent intent = new Intent(purchasePlan.this, purchasePlan.class);
                    startActivity(intent);
                }
                else
                {
                    Lib.showMessage("Bitte geben Sie eine Beschreibung ein!", controller);
                }
            }
        });


        Button purchPlan_finishPurch = (Button)findViewById(R.id.purchPlan_finishPurch);
        purchPlan_finishPurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    asyncTaskMethod = "deleteAllPurchasePlanEntries";
                    task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                finish();
                Intent intent = new Intent(purchasePlan.this, purchasePlan.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void processFinish(String s) {
        switch (asyncTaskMethod){
            case "getAllPurchasePlanEntries":
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
                break;

            case "createPurchasePlanEntry":
                Lib.showMessage("Eintrag angelegt!",controller);
                break;

            case "deleteAllPurchasePlanEntries":
                Lib.showMessage("Einkauf abgeschlossen!",controller);
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}