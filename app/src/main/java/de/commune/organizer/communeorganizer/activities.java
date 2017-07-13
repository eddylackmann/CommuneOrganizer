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
    GridView activitiesList;
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
        activitiesList = (GridView) findViewById(R.id.activities_GridView);
        activitiesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final Item currEntry = (Item) activitiesList.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                builder.setMessage(currEntry.getText2() + " löschen?")
                        .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try
                                {
                                    asyncTaskMethod = "deleteActivityEntry";
                                    task.execute("http://eddy-home.ddns.net/wg-app/activities.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID + "&LineNo=" + currEntry.getText1());
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
            asyncTaskMethod = "getAllActivityEntries";
            task.execute("http://eddy-home.ddns.net/wg-app/activities.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Button activities_AddBtn = (Button)findViewById(R.id.activities_AddBtn);
        activities_AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                final EditText edittext = new EditText(controller);
                alert.setMessage("Neuer Eintrag");
                alert.setTitle("Aktivität hinzufügen");
                edittext.setTextColor(Color.WHITE);
                alert.setView(edittext);
                alert.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String activityText = edittext.getText().toString();
                        if (!activityText.equals("")){
                            try
                            {
                                asyncTaskMethod = "createActivityEntry";
                                task.execute("http://eddy-home.ddns.net/wg-app/activities.php?Method=" + asyncTaskMethod +"&CommuneID="
                                        + communeID + "&Description=" + activityText +"&Date="+ "2017-07-13" + "&Time=00-00-00");
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
                alert.show();

            }
        });
    }

    @Override
    public void processFinish(String s) {
        Intent intent;
        switch (asyncTaskMethod){
            case "getAllActivityEntries":
                try
                {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        activitiesListEntries.add(new Item(row.getString("LineNo"),row.getString("Date") + " | " + row.getString("Time") + " " + row.getString("Description")));

                    }
                    MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,activitiesListEntries);
                    activitiesList.setAdapter(myAdapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case "createActivityEntry":
                if (s.equals("entryCreated")){
                    finish();
                    intent = new Intent(activities.this, activities.class);
                    startActivity(intent);
                    Lib.showMessage("Eintrag angelegt!",controller);
                }
                else {
                    Lib.showMessage("Anlegen fehlgeschlagen!",controller);
                }
                break;

            case "deleteActivityEntry":
                if (s.equals("entryDeleted")){
                    finish();
                    intent = new Intent(activities.this, activities.class);
                    startActivity(intent);
                    Lib.showMessage("Eintrag gelöscht!",controller);
                }
                else {
                    Lib.showMessage("Eintrag löschen fehlgeschlagen!",controller);
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}