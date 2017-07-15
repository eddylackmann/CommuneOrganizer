package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class createOrJoinCommune extends AppCompatActivity implements AsyncResponse {
    public AppCompatActivity controller;
    public my_Library my_lib = new my_Library();
    private createOrJoinCommune c;
    private String asyncTaskMethod ="";
    private PostResponseAsyncTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_or_create_commune);
        c = this;
        init();
    }

    public void init(){

        Intent intent = new Intent(createOrJoinCommune.this, RefreshAppGlobalInformation.class);
        startActivity(intent);

        controller = this;
        Button enterComBtn = (Button) findViewById(R.id.enterComBtn);
        Button createBtn = (Button) findViewById(R.id.createBtn);
        Button deleteUserBtn = (Button) findViewById(R.id.deleteUserBtn);
        final String Email =((MyApplication) this.getApplication()).getUserEmail();

        enterComBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createOrJoinCommune.this, enter_commune.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createOrJoinCommune.this, create_commune.class);
                startActivity(intent);
            }
        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new PostResponseAsyncTask(c);
                try {
                    asyncTaskMethod ="deleteUser";
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&Email=" +Email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void processFinish(String s) {
        Intent intent;
        switch (asyncTaskMethod) {
            case("deleteUser"):
                switch (s) {
                    case("userDeleted"):
                        finish();
                        intent = new Intent(createOrJoinCommune.this, MainActivity.class);
                        startActivity(intent);
                        my_lib.showMessage("Benutzeraccount gelöscht.", this);
                        break;
                }
                break;
        }
        task = new PostResponseAsyncTask(c);
    }
}
