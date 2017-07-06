package de.commune.organizer.communeorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class login extends AppCompatActivity {
    private Button loginBtn;
    private AlertDialog.Builder AlertBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlertBox = new AlertDialog.Builder(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


    public void init(){
         //= new AlertDialog.Builder(this)
        final TextView userEmail = (TextView)findViewById(R.id.uEmailText);
        final TextView userPassword = (TextView)findViewById(R.id.uPasswordText);
        loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence uEmail =  userEmail.getText();
                CharSequence uPw = userPassword.getText();
                Intent intent = new Intent(login.this, Home.class);
                startActivity(intent);


            }
        });
    }


}
