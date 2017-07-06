package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void init(){
        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uEmail =  userEmail.getText().toString();
                String uPw = userPassword.getText().toString();

                if(uEmail=="test@test.de" & uPw =="test"){
                    Intent intent = new Intent(login.this, Home.class);
                    startActivity(intent);
                }else{

                    userEmail.setTextColor(Color.RED);
                    userPassword.setTextColor(Color.RED);
                }

            }
        });
    }


}
