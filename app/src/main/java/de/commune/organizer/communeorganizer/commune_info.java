package de.commune.organizer.communeorganizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

        Intent intent = new Intent(commune_info.this, RefreshAppGlobalInformation.class);
        startActivity(intent);

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
        infoSpaceText.setText(((MyApplication) this.getApplication()).getInformation("LivingSpace").replace(".",","));
        infoRentText.setText(((MyApplication) this.getApplication()).getInformation("ColdRent").replace(".",","));
        infoAddCostsText.setText(((MyApplication) this.getApplication()).getInformation("AdditionalCosts").replace(".",","));
        infoOtherCostsText.setText(((MyApplication) this.getApplication()).getInformation("OtherCosts").replace(".",","));
        infoDescText.setText(((MyApplication) this.getApplication()).getInformation("Description"));

        infoInhText.setEnabled(false);
        infoInhText.setTextColor(Color.BLACK);

        final Button comInfo_saveChangesBtn = (Button)findViewById(R.id.comInfo_saveChangesBtn);
        comInfo_saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCommuneInformation();
            }
        });

        final Button comInfo_deleteCommune = (Button) findViewById(R.id.comInfo_deleteCommune);
        comInfo_deleteCommune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCommue();
            }
        });
    }

    public void changeCommuneInformation(){
        final TextView infoPWText = (TextView) findViewById(R.id.infoPWText);
        final TextView infoAddressText = (TextView) findViewById(R.id.infoAddressText);
        final TextView infoZIPText = (TextView) findViewById(R.id.infoZIPText);
        final TextView infoCityText = (TextView) findViewById(R.id.infoCityText);
        final TextView infoMaxInhText = (TextView) findViewById(R.id.infoMaxInhText);
        final CheckBox infoPetsText = (CheckBox) findViewById(R.id.infoPetsText);
        final TextView infoSpaceText = (TextView) findViewById(R.id.infoSpaceText);
        final TextView infoRentText = (TextView) findViewById(R.id.infoRentText);
        final TextView infoAddCostsText = (TextView) findViewById(R.id.infoAddCostText);
        final TextView infoOtherCostsText = (TextView) findViewById(R.id.infoOtherCostText);
        final TextView infoDescText = (TextView) findViewById(R.id.infoComDescText);
        final TextView infoInhText = (TextView) findViewById(R.id.infoInhText);
        final String CommuneID = (((MyApplication) this.getApplication()).getInformation("CommuneID"));
        final String IsAdmin = (((MyApplication) this.getApplication()).getInformation("CommuneAdmin"));

        if (IsAdmin.equals("1")){
            if (Integer.parseInt(infoMaxInhText.getText().toString()) >= Integer.parseInt(infoInhText.getText().toString()))
            {
                task = new PostResponseAsyncTask(this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
                builder.setMessage("WG-Informationen speichern? ")
                        .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String pedsAllowedValue = "0";
                                if (infoPetsText.isChecked()){
                                    pedsAllowedValue = "1";
                                }

                                try {
                                    asyncTaskMethod = "updateCommune";
                                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method="+ asyncTaskMethod +"&CommuneID=" + CommuneID + "&CommunePassword=" +
                                            infoPWText.getText() + "&Address=" + infoAddressText.getText() + "&PostCode=" + infoZIPText.getText()+ "&City=" + infoCityText.getText()
                                            + "&NumberOfMaxInhabitants=" + infoMaxInhText.getText() +"&PetsAllowed=" + pedsAllowedValue + "&LivingSpace=" +
                                            infoSpaceText.getText() + "&ColdRent=" + infoRentText.getText() + "&AdditionalCosts=" + infoAddCostsText.getText() +
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
            else
            {
                Lib.showMessage("Die Anzahl der maximalen Mitbewohner muss größer oder gleich der Anzahl aktueller Mitbewohner sein.",controller);
                infoMaxInhText.setText(infoInhText.getText());
            }
        }
        else
        {
            Lib.showMessage("Sie sind nicht berechtigt.",controller);
        }
    }

    public void deleteCommue(){
        final String CommuneID = (((MyApplication) this.getApplication()).getInformation("CommuneID"));
        final String IsAdmin = (((MyApplication) this.getApplication()).getInformation("CommuneAdmin"));

        if (IsAdmin.equals("1")){
            task = new PostResponseAsyncTask(this);
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
            builder.setMessage("WG löschen?")
                    .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try
                            {
                                asyncTaskMethod = "deleteCommune";
                                task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method="+ asyncTaskMethod +"&CommuneID=" + CommuneID);
                            }
                            catch (Exception e)
                            {
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
        else
        {
            Lib.showMessage("Sie sind nicht berechtigt.",controller);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void processFinish(String s){
        switch (asyncTaskMethod){
            case "updateCommune":
                switch (s)
                {
                    case "communeUpdated":
                        finish();
                        Intent intent = new Intent(commune_info.this, Home.class);
                        startActivity(intent);
                        break;
                }
                break;
            case "deleteCommune":
                switch (s)
                {
                    case "communeDeleted":
                        finish();
                        Intent intent = new Intent(commune_info.this, createOrJoinCommune.class);
                        startActivity(intent);
                        break;
                }
                break;
        }
        task = new PostResponseAsyncTask(this);
    }
}
