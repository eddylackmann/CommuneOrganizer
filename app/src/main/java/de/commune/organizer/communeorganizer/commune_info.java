package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;

public class commune_info extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;
    private String asyncTaskMethod ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        controller = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commune_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        Lib = new my_Library();

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

        final Button comInfo_saveChangesBtn = (Button)findViewById(R.id.comInfo_saveChangesBtn);
        comInfo_saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCommuneInformation();
            }
        });
    }

    public void changeCommuneInformation(){
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
        final String CommuneID = (((MyApplication) this.getApplication()).getInformation("CommuneID"));

        task = new PostResponseAsyncTask(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        builder.setMessage("WG-Informationen speichern? ")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //fetch data from url
                        String test = "http://eddy-home.ddns.net/wg-app/loginMgt.php?Method="+ asyncTaskMethod +"&communeID=" + CommuneID + "&communePassword=" +
                        infoPWText.getText() + "&Address=" + infoAddressText.getText() + "&PostCode=" + infoZIPText.getText()+ "&City=" + infoCityText.getText()
                                + "&NumberOfMaxInhabitants=" + infoMaxInhText.getText() +"&PetsAllowed=" + infoPetsText.isChecked() + "&LivingSpace=" +
                                infoSpaceText.getText() + "&ColdRent_(Total)=" + infoRentText.getText() + "&AdditionalCosts=" + infoAddCostsText.getText() +
                                "&OtherCosts=" + infoOtherCostsText.getText() + "&Description=" + infoDescText.getText();
                        try {
                            asyncTaskMethod = "updateCommune";
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method="+ asyncTaskMethod +"&communeID=" + CommuneID + "&communePassword=" +
                                    infoPWText.getText() + "&Address=" + infoAddressText.getText() + "&PostCode=" + infoZIPText.getText()+ "&City=" + infoCityText.getText()
                                    + "&NumberOfMaxInhabitants=" + infoMaxInhText.getText() +"&PetsAllowed=" + infoPetsText.getText() + "&LivingSpace=" +
                                    infoSpaceText.getText() + "&ColdRent_(Total)=" + infoRentText.getText() + "&AdditionalCosts=" + infoAddCostsText.getText() +
                                    "&OtherCosts=" + infoOtherCostsText.getText() + "&Description=" + infoDescText.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void processFinish(String s){
        Intent intent;
        switch (asyncTaskMethod){
            case "getCommuneInformation":
                switch (s){
                    case "communeUpdated":
                        try{
                            ((MyApplication) this.getApplication()).setInformationArray(new JSONArray(s));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finish();
                        intent = new Intent(commune_info.this, commune_info.class);
                        startActivity(intent);
                        break;
                }

                break;
            case "updateCommune":
                task = new PostResponseAsyncTask(this);
                try {
                    asyncTaskMethod = "getCommuneInformation";
                    task.execute("http://eddy-home.ddns.net/wg-app/communes.php?Method="+asyncTaskMethod+
                            "&CommuneID=" + ((MyApplication) this.getApplication()).getCommuneID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}
