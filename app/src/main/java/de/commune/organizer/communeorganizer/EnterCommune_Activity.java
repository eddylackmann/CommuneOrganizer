package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class EnterCommune_Activity extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public AppLibrary Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_commune_layout);

        init();
        setLayout();
    }

    public void init() {

        Lib = new AppLibrary();
        task = new PostResponseAsyncTask(this);
        controller = this;

        final TextView communeID = (TextView) findViewById(R.id.enter_communeID);
        final TextView communePwText = (TextView) findViewById(R.id.enter_communePwText);
        final String globalUserEmail = ((MyApplication) this.getApplication()).getUserEmail();
        Button joinBtn = (Button) findViewById(R.id.enter_joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=enterCommune&CommuneID=" + communeID.getText()
                        + "&communePassword=" + communePwText.getText() + "&Email=" + globalUserEmail);
            }
        });
    }

    private void setLayout() {

        TextView enter_communeID = (TextView) findViewById(R.id.enter_communeID);
        enter_communeID.setHintTextColor(Color.WHITE);
        TextView enter_communePW = (TextView) findViewById(R.id.enter_communePwText);
        enter_communePW.setHintTextColor(Color.WHITE);
    }

    @Override
    public void processFinish(String s) {
        switch (s) {
            case "successfulEnter":
                Intent intent = new Intent(EnterCommune_Activity.this, Home_Activity.class);
                startActivity(intent);
                Lib.showMessage("Beitreten der WG erfolgreich.", this);
                break;
            case "noPlaceInCommune":
                Lib.showMessage("Keine freien Plätze in der WG verfügbar!", this);
                break;
            case "communeDoesNotExist":
                Lib.showMessage("WG existiert nicht!", this);
                break;
            case "wrongCommunePassword":
                Lib.showMessage("WG Passwort ist falsch!", this);
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}
