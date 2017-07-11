package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;

public class login extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controller = this;
        init();
    }

    public void init(){
        Lib = new my_Library();

        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        task = new PostResponseAsyncTask(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                try {
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=loginUser&Email=" + userEmail.getText() + "&Password=" + userPassword.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void processFinish(String s){
        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        switch (s)
        {
            case "loginSuccessfull":
                Intent intent = new Intent(login.this, Home.class);
                startActivity(intent);

                ((MyApplication) this.getApplication()).setUserEmail(userEmail.getText().toString());
                ((MyApplication) this.getApplication()).setUserPassword(userPassword.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "loginWithoutCommune":
                Intent intent2 = new Intent(login.this, createOrJoinCommune.class);
                startActivity(intent2);

                ((MyApplication) this.getApplication()).setUserEmail(userEmail.getText().toString());
                ((MyApplication) this.getApplication()).setUserPassword(userPassword.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "wrongPassword":
                Lib.showMessage("Falsches Passwort!",controller);
                break;
            case "userDoesNotExist":
                Lib.showMessage("Benutzer existiert nicht!",controller);
                break;
        }
        task = new PostResponseAsyncTask(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
