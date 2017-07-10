package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

public class registration extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public my_Library Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        controller = this;
        init();
    }

    public void init(){
        Lib = new my_Library();
        task = new PostResponseAsyncTask(this);

        final TextView MailText = (TextView) findViewById(R.id.MailText);
        final TextView uPwText = (TextView) findViewById(R.id.uPwText);
        final TextView uPwRepeatText = (TextView) findViewById(R.id.uPwRepeatText);
        final TextView uFirstname = (TextView) findViewById(R.id.uFirstname);
        final TextView uLastname = (TextView) findViewById(R.id.uLastname);

        Button regBtn = (Button)findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                if(uPwText.getText().toString().equals(uPwRepeatText.getText().toString()))
                {
                    try
                    {
                        task.execute("http://eddy-home.ddns.net/wg-app/Temp_user.php?Method=registerUser&Email=" + MailText.getText() + "&Password=" + uPwText.getText() + "&Firstname=" + uFirstname.getText() + "&Lastname=" + uLastname.getText());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Lib.showMessage("Passwörter stimmen nicht überein!",controller);
                }
            }
        });
    }

    @Override
    public void processFinish(String s){
        final TextView MailText = (TextView) findViewById(R.id.MailText);
        final TextView uPwText = (TextView) findViewById(R.id.uPwText);
        final TextView uFirstname = (TextView) findViewById(R.id.uFirstname);
        final TextView uLastname = (TextView) findViewById(R.id.uLastname);

        switch (s)
        {
            case "registrationSuccessful":
                Intent intent = new Intent(registration.this, createOrJoinCommune.class);
                startActivity(intent);

                ((MyApplication) this.getApplication()).setUserEmail(MailText.getText().toString());
                ((MyApplication) this.getApplication()).setUserPassword(uPwText.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "userAlreadyExists":
                Lib.showMessage("Benutzer existiert bereits!",this);
                task = new PostResponseAsyncTask(this);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
