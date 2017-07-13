package de.commune.organizer.communeorganizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class commune_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commune_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }


    private void init(){
        final TextView infocommuneID = (TextView) findViewById(R.id.infocommuneID);
        final TextView infoPWText = (TextView) findViewById(R.id.infoPWText);
        final TextView infoAddressText = (TextView) findViewById(R.id.infoAddressText);
        final TextView infoZIPText = (TextView) findViewById(R.id.infoZIPText);


        infocommuneID.setText(((MyApplication) this.getApplication()).getInformation("CommuneID"));
        infoPWText.setText(((MyApplication) this.getApplication()).getInformation("CommunePassword"));
        infoAddressText.setText(((MyApplication) this.getApplication()).getInformation("Address"));
        infoZIPText.setText(((MyApplication) this.getApplication()).getInformation("PostCode"));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
