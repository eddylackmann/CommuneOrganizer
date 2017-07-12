package de.commune.organizer.communeorganizer;

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
        purchPlanListEntries.clear();
        purchPlanList = (GridView) findViewById(R.id.purchPlan_GridView);
        purchPlanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Item currEntry = (Item) purchPlanList.getItemAtPosition(position);
                try
                {
                    asyncTaskMethod = "deletePurchasePlanEntry";
                    task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID + "&LineNo=" + currEntry.getText1());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return true;
            }
        });

        try
        {
            asyncTaskMethod = "getAllPurchasePlanEntries";
            task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        Button purchPlan_AddBtn = (Button)findViewById(R.id.purchPlan_AddBtn);
        purchPlan_AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                final EditText edittext = new EditText(controller);
                alert.setMessage("Neue Eintrag");
                alert.setTitle("Einkaufliste");
                edittext.setTextColor(Color.WHITE);
                alert.setView(edittext);
                alert.setPositiveButton("Eintragen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String PurchaseText = edittext.getText().toString();
                        if (!PurchaseText.equals("")){
                            try
                            {
                                asyncTaskMethod = "createPurchasePlanEntry";
                                task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID="
                                        + communeID + "&Description=" + PurchaseText);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Lib.showMessage("Bitte geben Sie eine Beschreibung ein!", controller);
                        }

                    }
                });

                alert.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();







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
            }
        });

    }

    @Override
    public void processFinish(String s) {
        Intent intent;
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
                if (s.equals("entryCreated")){
                    finish();
                    intent = new Intent(purchasePlan.this, purchasePlan.class);
                    startActivity(intent);
                    Lib.showMessage("Eintrag angelegt!",controller);
                }
                else {
                    Lib.showMessage("Anlegen fehlgeschlagen!",controller);
                }
                break;

            case "deletePurchasePlanEntry":
                if (s.equals("entryDeleted")){
                    finish();
                    intent = new Intent(purchasePlan.this, purchasePlan.class);
                    startActivity(intent);
                    Lib.showMessage("Eintrag gelöscht!",controller);
                }
                else {
                    Lib.showMessage("Eintrag löschen fehlgeschlagen!",controller);
                }
                break;

            case "deleteAllPurchasePlanEntries":
                if (s.equals("allEntriesDeleted")){
                    finish();
                    intent = new Intent(purchasePlan.this, purchasePlan.class);
                    startActivity(intent);
                    Lib.showMessage("Einkauf abgeschlossen!",controller);
                }
                else{
                    Lib.showMessage("Abschließen fehlgeschlagen!",controller);
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}