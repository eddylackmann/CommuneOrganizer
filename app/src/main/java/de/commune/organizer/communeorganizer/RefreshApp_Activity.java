package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;

/**
 * Created by Tom on 16.07.2017.
 */

public class RefreshApp_Activity extends AppCompatActivity implements AsyncResponse {
    private PostResponseAsyncTask task;
    private AsyncResponse asyncResponse;
    private AppLibrary Lib = new AppLibrary();
    AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_data_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        asyncResponse = this;
        controller = this;
        init();
    }

    private void init() {
        task = new PostResponseAsyncTask(this, false, asyncResponse);
        try {
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=getInformation&Email=" + ((MyApplication) this.getApplication()).getUserEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String s) {
        if (s.equals("null")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(controller, R.style.AlertDialogCustom));
            builder.setMessage("Der Admin hat sie aus der WG entfernt.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            controller.finish();
                            Intent intent = new Intent(RefreshApp_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.create();
            builder.show();
        } else {
            try {
                ((MyApplication) this.getApplication()).setInformationArray(new JSONArray(s));
                this.finish();
            } catch (Exception e) {
                e.printStackTrace();
                this.finish();
            }
        }
    }
}
