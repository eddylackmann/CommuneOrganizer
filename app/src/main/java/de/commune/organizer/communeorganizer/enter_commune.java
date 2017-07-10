package de.commune.organizer.communeorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class enter_commune extends AppCompatActivity implements AsyncResponse {
    private AlertDialog.Builder AlertBox;
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;
    public boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_commune);
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        controller = this;

        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        Button createBtn = (Button) findViewById(R.id.createBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}
