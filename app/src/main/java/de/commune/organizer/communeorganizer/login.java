package de.commune.organizer.communeorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;

public class login extends AppCompatActivity implements AsyncResponse {
    private AlertDialog.Builder AlertBox;
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;
    public boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        controller = this;

        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        Button loginBtn = (Button) findViewById(R.id.signInBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                task.execute("http://eddy-home.ddns.net/wg-app/Temp_user.php?Method=loginUser&Email=" + userEmail.getText() + "&Password=" + userPassword.getText());
            }
        });
    }

    @Override
    public void processFinish(String s){
        switch (s)
        {
            case "loginSuccessfull":
                Intent intent = new Intent(login.this, Home.class);
                startActivity(intent);
                loggedIn = true;
                break;
            case "loginWithoutCommune":
                loggedIn = true;
                Intent intent2 = new Intent(login.this, createOrJoinCommune.class);
                startActivity(intent2);

                final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
                ((MyApplication) this.getApplication()).setUserEmail(userEmail.getText().toString());
                final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
                ((MyApplication) this.getApplication()).setUserPassword(userPassword.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "wrongPassword":
                loggedIn = false;
                Lib.showMessage("Falsches Passwort!",controller);
                break;
            case "userDoesNotExist":
                loggedIn = false;
                Lib.showMessage("Benutzer existiert nicht!",controller);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
