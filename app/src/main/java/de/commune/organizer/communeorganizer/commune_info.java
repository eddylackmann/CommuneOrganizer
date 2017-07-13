package de.commune.organizer.communeorganizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
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
        final TextView infoCityText = (TextView) findViewById(R.id.infoCityText);
        final TextView infoMaxInhText = (TextView) findViewById(R.id.infoMaxInhText);
        final TextView infoInhText = (TextView) findViewById(R.id.infoInhText);
        final CheckBox infoPetsText = (CheckBox) findViewById(R.id.infoPetsText);
        final TextView infoSpaceText = (TextView) findViewById(R.id.infoSpaceText);
        final TextView infoRentText = (TextView) findViewById(R.id.infoRentText);
        final TextView infoAddCostsText = (TextView) findViewById(R.id.infoAddCostText);
        final TextView infoOtherCostsText = (TextView) findViewById(R.id.infoOtherCostText);
        final TextView infoDescText = (TextView) findViewById(R.id.infoComDescText);

        infocommuneID.setText(((MyApplication) this.getApplication()).getInformation("CommuneID"));
        infoPWText.setText(((MyApplication) this.getApplication()).getInformation("CommunePassword"));
        infoAddressText.setText(((MyApplication) this.getApplication()).getInformation("Address"));
        infoZIPText.setText(((MyApplication) this.getApplication()).getInformation("PostCode"));
        infoCityText.setText(((MyApplication) this.getApplication()).getInformation("City"));
        infoMaxInhText.setText(((MyApplication) this.getApplication()).getInformation("NumberOfMaxInhabitants"));
        infoInhText.setText(((MyApplication) this.getApplication()).getInformation("NumberOfInhabitants"));
        if (((MyApplication) this.getApplication()).getInformation("PetsAllowed").equals("1")){
            infoPetsText.setChecked(true);
        }
        else{
            infoPetsText.setChecked(false);
        }
        infoSpaceText.setText(((MyApplication) this.getApplication()).getInformation("LivingSpace_(m²)"));
        infoRentText.setText(((MyApplication) this.getApplication()).getInformation("ColdRent_(Total)"));
        infoAddCostsText.setText(((MyApplication) this.getApplication()).getInformation("AdditionalCosts"));
        infoOtherCostsText.setText(((MyApplication) this.getApplication()).getInformation("OtherCosts"));
        infoDescText.setText(((MyApplication) this.getApplication()).getInformation("Description"));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
