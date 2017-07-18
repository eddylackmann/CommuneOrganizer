package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class CreateJoinCommune_Activity extends AppCompatActivity implements AsyncResponse {
    public AppCompatActivity controller;
    public AppLibrary my_lib = new AppLibrary();
    private CreateJoinCommune_Activity c;
    private String asyncTaskMethod = "";
    private PostResponseAsyncTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_create_commune_layout);
        c = this;
        init();
    }

    public void init() {
        controller = this;
        Button enterComBtn = (Button) findViewById(R.id.enterComBtn);
        Button createBtn = (Button) findViewById(R.id.createBtn);
        Button deleteUserBtn = (Button) findViewById(R.id.deleteUserBtn);
        final String Email = ((MyApplication) this.getApplication()).getUserEmail();

        enterComBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJoinCommune_Activity.this, EnterCommune_Activity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJoinCommune_Activity.this, CreateCommune_Activity.class);
                startActivity(intent);
            }
        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new PostResponseAsyncTask(c);
                try {
                    asyncTaskMethod = "deleteUser";
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=" + asyncTaskMethod + "&Email=" + Email);
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
            case ("deleteUser"):
                switch (s) {
                    case ("userDeleted"):
                        finish();
                        intent = new Intent(CreateJoinCommune_Activity.this, MainActivity.class);
                        startActivity(intent);
                        my_lib.showMessage("Benutzeraccount gelöscht.", this);
                        break;
                }
                break;
        }
        task = new PostResponseAsyncTask(c);
    }
}
