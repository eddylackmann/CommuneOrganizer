package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class createOrJoinCommune extends AppCompatActivity {
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_or_create_commune);
        init();
    }

    public void init(){
        controller = this;
        Button enterComBtn = (Button) findViewById(R.id.enterComBtn);
        Button createBtn = (Button) findViewById(R.id.createBtn);


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
    }
}
