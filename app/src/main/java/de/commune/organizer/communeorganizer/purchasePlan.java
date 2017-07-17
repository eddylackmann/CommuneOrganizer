package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
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
    String CashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_plan);
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

        Intent intent = new Intent(purchasePlan.this, RefreshAppGlobalInformation.class);
        startActivity(intent);

        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        purchPlanListEntries.clear();
        purchPlanList = (GridView) findViewById(R.id.purchPlan_GridView);
        purchPlanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final Item currEntry = (Item) purchPlanList.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                builder.setMessage(currEntry.getText2() + " löschen?")
                        .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try
                            {
                                asyncTaskMethod = "deletePurchasePlanEntry";
                                task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID + "&LineNo=" + currEntry.getLineNo());
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();
            builder.show();
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
                alert.setMessage("Neuer Eintrag");
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
                                        + communeID + "&Description=" + PurchaseText+"&Firstname="+ ((MyApplication) controller.getApplication()).getInformation("Firstname"));
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
                if (!purchPlanListEntries.isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                    final EditText edittext = new EditText(controller);
                    edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    alert.setMessage("Bitte geben Sie die Kosten ein.");
                    alert.setTitle("Einkauf abschließen? Dies wird alle Einträge löschen.");
                    edittext.setTextColor(Color.WHITE);
                    alert.setView(edittext);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            CashText = edittext.getText().toString();
                            if (!CashText.equals("")){
                                try
                                {
                                    asyncTaskMethod = "deleteAllPurchasePlanEntries";
                                    task.execute("http://eddy-home.ddns.net/wg-app/purchasePlans.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Lib.showMessage("Keine Kosten angegeben. Abbruch.",controller);
                            }
                        }

                    });

                    alert.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }
                else
                {
                    Lib.showMessage("Welchen Einkauf wollen Sie abschließen? Hier sind keine Einträge vorhanden.",controller);
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
                        purchPlanListEntries.add(new Item(row.getString("User_Firstname"),row.getString("Description"),row.getString("LineNo"),""));
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
                }
                else {
                    Lib.showMessage("Eintrag löschen fehlgeschlagen!",controller);
                }
                break;

            case "deleteAllPurchasePlanEntries":
                if (s.equals("allEntriesDeleted")){
                    task = new PostResponseAsyncTask(this);
                    asyncTaskMethod = "addCash";
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=addCashToCommune&CommuneID=" +((MyApplication) controller.getApplication()).getInformation("CommuneID")
                            + "&CurrentCash="+((MyApplication) controller.getApplication()).getInformation("CommuneCashbox") +"&Cash="+ CashText +"&CashType=sub");
                }
                else
                {
                    Lib.showMessage("Abschließen fehlgeschlagen!",controller);
                }
                break;
            case "addCash":
                if (s.equals("cashUpdated")){
                    finish();
                    intent = new Intent(purchasePlan.this, Home.class);
                    startActivity(intent);
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}