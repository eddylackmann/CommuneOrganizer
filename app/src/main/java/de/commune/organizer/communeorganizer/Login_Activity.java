package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;

public class Login_Activity extends AppCompatActivity implements AsyncResponse {
    public PostResponseAsyncTask task;
    public AppLibrary Lib;
    public AppCompatActivity controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        controller = this;
        setLayout();
        init();

    }

    public void setLayout() {
        TextView emailHint = (TextView) findViewById(R.id.uEmailText);
        emailHint.setHintTextColor(Color.WHITE);
        TextView passHint = (TextView) findViewById(R.id.uPasswordText);
        passHint.setHintTextColor(Color.WHITE);

        final EditText EmailTest = (EditText) findViewById(R.id.uEmailText);
        final EditText PassTest = (EditText) findViewById(R.id.uPasswordText);

        styleTextInput(EmailTest);
        styleTextInput(PassTest);
    }

    public void init() {
        Lib = new AppLibrary();

        final TextView userEmail = (TextView) findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView) findViewById(R.id.uPasswordText);
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        task = new PostResponseAsyncTask(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Lib.validate_Email(userEmail.getText().toString())) {
                    userEmail.setError("Keine gültige Email Adresse!");
                }

                if (userEmail.getText().toString().equals("")) {
                    userEmail.setError("Bitte tragen Sie Ihre Email ein!");
                }

                if (userPassword.getText().toString().equals("")) {
                    userPassword.setError("Bitte tragen Sie Ihr Passwort ein!");
                }

                //fetch data from url
                if (!userEmail.getText().toString().equals("") & !userPassword.getText().toString().equals("") & Lib.validate_Email(userEmail.getText().toString())) {
                    try {
                        task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=loginUser&Email=" + userEmail.getText() + "&Password=" + userPassword.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void processFinish(String s) {
        final TextView userEmail = (TextView) findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView) findViewById(R.id.uPasswordText);
        switch (s) {
            case "loginSuccessfull":
                Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                startActivity(intent);
                ((MyApplication) this.getApplication()).setUserEmail(userEmail.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                finish();
                break;
            case "loginWithoutCommune":
                Intent intent2 = new Intent(Login_Activity.this, CreateJoinCommune_Activity.class);
                startActivity(intent2);
                ((MyApplication) this.getApplication()).setUserEmail(userEmail.getText().toString());
                ((MyApplication) this.getApplication()).setUserLoggedIn(true);
                break;
            case "wrongPassword":
                userPassword.setError("Falsches Passwort!");
                break;
            case "userDoesNotExist":
                userEmail.setError("Benutzer existiert nicht!");
                break;
        }
        task = new PostResponseAsyncTask(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void styleTextInput(final EditText Edit) {
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
                if (s.length() != 0) {
                    Edit.setBackgroundColor(Color.WHITE);
                    Edit.setTextColor(Color.BLACK);
                } else {
                    Edit.setBackgroundColor(0);
                    Edit.setLinkTextColor(Color.WHITE);
                    Edit.setHintTextColor(Color.WHITE);

                }
            }
        });
    }
}
