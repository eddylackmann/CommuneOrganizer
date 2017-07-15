package de.commune.organizer.communeorganizer;

/**
 * Created by Tom on 13.07.2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
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
    private String currUserEmail;

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

        Intent intent = new Intent(inhabitant_list.this, RefreshAppGlobalInformation.class);
        startActivity(intent);

        Lib = new my_Library();
        communeID = ((MyApplication) this.getApplication()).getInformation("CommuneID");
        currUserEmail = ((MyApplication) this.getApplication()).getInformation("Email");
        final String isAdmin = ((MyApplication) this.getApplication()).getInformation("CommuneAdmin");

        task = new PostResponseAsyncTask(this);
        try
        {
            asyncTaskMethod = "getCommuneInhabitants";
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        inhabitantListEntries.clear();
        inhabitantList = (GridView) findViewById(R.id.inhabitantListGridView);
        inhabitantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final Item currEntry = (Item) inhabitantList.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(controller,R.style.AlertDialogCustom));
                if (isAdmin.equals("1")){
                    builder.setMessage(currEntry.getText1() + " aus WG entfernen?")
                            .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try
                                    {
                                        asyncTaskMethod = "exitCommune";
                                        task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod +"&CommuneID=" + communeID + "&Email=" + currEntry.getUserEmail());
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
                }
                return true;
            }
        });
    }

    @Override
    public void processFinish(String s) {
        switch (asyncTaskMethod){
            case "getCommuneInhabitants":
                try
                {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        if (!currUserEmail.equals(row.getString("Email"))){
                            inhabitantListEntries.add(new Item(row.getString("Firstname") + " " + row.getString("Lastname"),row.getString("Email"),"",row.getString("Email")));
                        }
                    }
                    MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,inhabitantListEntries);
                    inhabitantList.setAdapter(myAdapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case "exitCommune":
                if (s.equals("exitSuccessful")){
                    finish();
                    Intent intent = new Intent(inhabitant_list.this, Home.class);
                    startActivity(intent);
                    Lib.showMessage("Benutzer entfernt!",controller);
                    break;
                }
        }
        task = new PostResponseAsyncTask(this);
    }
}
