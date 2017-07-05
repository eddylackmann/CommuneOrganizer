package de.commune.organizer.communeorganizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class create_commune extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_commune);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
