package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        final EditText MailText =    (EditText) findViewById(R.id.MailText);
        final EditText uPwText =     (EditText) findViewById(R.id.uPwText);
        final EditText uPwRepeatText = (EditText) findViewById(R.id.uPwRepeatText);
        final EditText uFirstname = (EditText) findViewById(R.id.uFirstname);
        final EditText uLastname = (EditText) findViewById(R.id.uLastname);

        styleTextInput(MailText);
        styleTextInput(uPwText);
        styleTextInput(uFirstname);
        styleTextInput(uLastname);
        styleTextInput(uPwRepeatText);

        Button regBtn = (Button)findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data from url
                if(!Lib.validate_Email(MailText.getText().toString())){
                    MailText.setError("Keine gültige Email Adresse!");
                }
                if(MailText.getText().toString().trim().equals("")| uPwText.getText().toString().trim().equals("")
                   | uFirstname.getText().toString().trim().equals("") | uLastname.getText().toString().trim().equals("")
                        ){
                    MailText.setError("Erforderlich !");
                    uPwText.setError("Erforderlich !");
                    uFirstname.setError("Erforderlich !");
                    uLastname.setError("Erforderlich !");
                }else {
                    if (uPwText.getText().toString().equals(uPwRepeatText.getText().toString()) & Lib.validate_Email(MailText.getText().toString())) {

                        try {
                            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=registerUser&Email=" + MailText.getText() + "&Password=" + uPwText.getText() + "&Firstname=" + uFirstname.getText() + "&Lastname=" + uLastname.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        uPwText.setError("Passwörter stimmen nicht überein!");
                        uPwRepeatText.setError("Passwörter stimmen nicht überein!");
                    }
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
                finish();
                Intent intent = new Intent(registration.this, createOrJoinCommune.class);
                startActivity(intent);
                ((MyApplication) this.getApplication()).setUserEmail(MailText.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "userAlreadyExists":
                Lib.showMessage("Benutzer existiert bereits!",this);
                task = new PostResponseAsyncTask(this);
                break;
        }
        task = new PostResponseAsyncTask(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void styleTextInput(final EditText Edit){
        Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    Edit.setBackgroundColor(Color.WHITE);
                    Edit.setTextColor(Color.BLACK);}
                else{
                    Edit.setBackgroundColor(0);
                    Edit.setHintTextColor(Color.WHITE);

                }
            }
        });
    }
}
