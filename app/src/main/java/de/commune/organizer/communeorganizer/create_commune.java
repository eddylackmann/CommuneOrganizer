package de.commune.organizer.communeorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;

public class create_commune extends AppCompatActivity implements AsyncResponse {
    private AlertDialog.Builder AlertBox;
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_commune);
        init();
    }
    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);
        controller = this;

        final TextView comAddressText = (TextView)findViewById(R.id.comAddressText);
        final TextView comZipText = (TextView)findViewById(R.id.comZipText);
        final TextView comCityText = (TextView)findViewById(R.id.comCityText);
        final TextView comPW1 = (TextView)findViewById(R.id.comPW1);
        final TextView comPW2 = (TextView)findViewById(R.id.comPW2);
        Button createBtn = (Button) findViewById(R.id.createBtn);
        final String globalUserEmail = ((MyApplication) this.getApplication()).getUserEmail();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comPW1.getText().toString().equals(comPW2.getText().toString()))
                {

                    try {
                        task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=registerCommune&communePassword="
                                + comPW1.getText() + "&address=" + comAddressText.getText() + "&postCode=" + comZipText.getText()
                                + "&city=" + comCityText.getText() + "&Email=" + globalUserEmail);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Lib.showMessage("Die Passwörter stimmen nicht überein!",controller);
                }
            }
        });
    }

    @Override
    public void processFinish(String s){
        switch (s)
        {
            case "communeCreationSuccessful":
                Intent intent = new Intent(create_commune.this, Home.class);
                Lib.showMessage("Erstellen der WG erfolgreich.",controller);
                startActivity(intent);
                finish();
                break;
            case "communeCreationFailed":
                Lib.showMessage("Erstellen der WG fehlgeschlagen! Bitte versuchen Sie es erneut.",controller);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
