package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class registration extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        controller = this;
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);

        final TextView uEmailText = (TextView) findViewById(R.id.uEmailText);
        final TextView uPasswordText = (TextView) findViewById(R.id.uPasswordText);
        final TextView uFirstname = (TextView) findViewById(R.id.uFirstname);
        final TextView uLastname = (TextView) findViewById(R.id.uLastname);

        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                try {
                    task.execute("http://eddy-home.ddns.net/wg-app/Temp_user.php?Method=registerUser&Email=" + uEmailText.getText() + "&Password=" + uPasswordText.getText() + "&Firstname=" + uFirstname.getText() + "&Lastname=" + uLastname.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void processFinish(String s){
        final TextView uEmailText = (TextView) findViewById(R.id.uEmailText);
        final TextView uPasswordText = (TextView) findViewById(R.id.uPasswordText);
        final TextView uFirstname = (TextView) findViewById(R.id.uFirstname);
        final TextView uLastname = (TextView) findViewById(R.id.uLastname);

        switch (s)
        {
            case "registrationSuccessful":
                Intent intent = new Intent(registration.this, Home.class);
                startActivity(intent);

                ((MyApplication) this.getApplication()).setUserEmail(uEmailText.getText().toString());
                ((MyApplication) this.getApplication()).setUserPassword(uPasswordText.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "userAlreadyExists":
                Lib.showMessage("Benutzer existiert bereits!",this);
                task = new PostResponseAsyncTask(this);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
