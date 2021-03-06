package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
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

public class CleaningPlan_Activity extends AppCompatActivity implements AsyncResponse {
    private PostResponseAsyncTask task;
    private AppLibrary Lib;
    GridView cleaningPlanGridView;
    ArrayList<Item> cleaningPlanList = new ArrayList<>();
    private AppCompatActivity controller;
    private String communeID;
    private String asyncTaskMethod;
    String userEmail;
    private boolean showAllEntries = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaning_plan_layout);
        controller = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {
        Intent intent = new Intent(CleaningPlan_Activity.this, RefreshApp_Activity.class);
        startActivity(intent);

        Lib = new AppLibrary();
        task = new PostResponseAsyncTask(this);
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        userEmail = ((MyApplication) getApplication()).getUserEmail();
        cleaningPlanGridView = (GridView) findViewById(R.id.cleaningPlanGridView);
        cleaningPlanGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final Item currEntry = (Item) cleaningPlanGridView.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(controller, R.style.AlertDialogCustom));
                builder.setMessage(currEntry.getText2() + " löschen?")
                        .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    asyncTaskMethod = "deleteCleaningPlanEntry";
                                    task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=" + asyncTaskMethod + "&CommuneID=" + communeID + "&Email=" + currEntry.getUserEmail() + "&LineNo=" + currEntry.getLineNo());
                                } catch (Exception e) {
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

        try {
            asyncTaskMethod = "getAllCleaningPlansForUser";
            task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=" + asyncTaskMethod + "&CommuneID="
                    + communeID + "&Email=" + userEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button cleaningRosterAddBtn = (Button) findViewById(R.id.cleaningRosterAddBtn);
        cleaningRosterAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CleaningPlan_Activity.this, CreateCleaningPlan_Activity.class);
                startActivity(intent);
            }
        });

        Button cleaningRosterChangeViewBtn = (Button) findViewById(R.id.cleaningRosterChangeViewBtn);
        cleaningRosterChangeViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showAllEntries) {
                    try {
                        asyncTaskMethod = "getAllCleaningPlansForUser";
                        task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=" + asyncTaskMethod + "&CommuneID="
                                + communeID + "&Email=" + userEmail);
                        showAllEntries = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        asyncTaskMethod = "getAllCleaningPlans";
                        task.execute("http://eddy-home.ddns.net/wg-app/cleaningPlan.php?Method=" + asyncTaskMethod + "&CommuneID=" + communeID);
                        showAllEntries = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void processFinish(String s) {
        Intent intent;
        switch (asyncTaskMethod) {
            case "getAllCleaningPlansForUser":
                cleaningPlanList.clear();
                task = new PostResponseAsyncTask(this);
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        cleaningPlanList.add(new Item(row.getString("FromDate") + " - " + row.getString("ToDate"), row.getString("Description"), row.getString("LineNo"), row.getString("UserEmail")));
                    }
                    MyAdapter myAdapter = new MyAdapter(this, R.layout.grid_view_items, cleaningPlanList);
                    cleaningPlanGridView.setAdapter(myAdapter);
                    Button cleaningRosterChangeViewBtn = (Button) findViewById(R.id.cleaningRosterChangeViewBtn);
                    cleaningRosterChangeViewBtn.setText("Alle anzeigen");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "deleteCleaningPlanEntry":
                if (s.equals("entryDeleted")) {
                    finish();
                    intent = new Intent(CleaningPlan_Activity.this, CleaningPlan_Activity.class);
                    startActivity(intent);
                    Lib.showMessage("Eintrag gelöscht!", controller);
                } else {
                    Lib.showMessage("Eintrag löschen fehlgeschlagen!", controller);
                }
                break;
            case "getAllCleaningPlans":
                cleaningPlanList.clear();
                task = new PostResponseAsyncTask(this);
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        cleaningPlanList.add(new Item(row.getString("FromDate") + " - " + row.getString("ToDate"), "[" + row.getString("UserFirstname") + "] " + row.getString("Description"), row.getString("LineNo"), row.getString("UserEmail")));
                    }
                    MyAdapter myAdapter = new MyAdapter(this, R.layout.grid_view_items, cleaningPlanList);
                    cleaningPlanGridView.setAdapter(myAdapter);
                    Button cleaningRosterChangeViewBtn = (Button) findViewById(R.id.cleaningRosterChangeViewBtn);
                    cleaningRosterChangeViewBtn.setText("Meine anzeigen");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}