package de.commune.organizer.communeorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;

public class login extends AppCompatActivity implements AsyncResponse {
    private Button loginBtn;
    private AlertDialog.Builder AlertBox;
    public  PostResponseAsyncTask task;
    public JSONArray js;
    public JSONObject parser;
    public my_Library Lib;
    public  AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        controller = this;

        //fetch data from url
        task.execute("http://eddy-home.ddns.net/wg-app/users.php?format=json");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lib.showMessage(js.toString(),controller);
                Intent intent = new Intent(login.this, Home.class);
                startActivity(intent);
            }

        });
    }


    @Override
    public void processFinish(String s){
        try{
            js = new JSONArray(s);
        }catch (Exception e){

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
