package de.commune.organizer.communeorganizer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.content.DialogInterface;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import android.text.InputType;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.w3c.dom.Text;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    private PostResponseAsyncTask task;
    private my_Library Lib = new my_Library();
    private AppCompatActivity c = this;
    private AsyncResponse controller;
    private String asyncTaskMethod;
    private Timer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setLayout();
        controller = this;
        init();
    }

    public void setLayout(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setHomeLayoutInformation() {
        Typeface.defaultFromStyle(Typeface.BOLD);
        TextView userDetailsHome = (TextView) findViewById(R.id.userHomeDescrition);
        TextView homeCommuneCash = (TextView) findViewById(R.id.home_communeCash);
        TextView addressHome = (TextView) findViewById(R.id.addressHomeDescrition);
        TextView communeHomeDescrition = (TextView) findViewById(R.id.communeHomeDescrition);
        userDetailsHome.setText(((MyApplication) this.getApplication()).getInformation("Firstname") + " " + ((MyApplication) this.getApplication()).getInformation("Lastname"));
        homeCommuneCash.setText(("" + ((MyApplication) this.getApplication()).getInformation("CommuneCashbox") + " €").replace(".",","));
        homeCommuneCash.setTypeface(null, Typeface.BOLD);
        addressHome.setText(((MyApplication) this.getApplication()).getInformation("Address") + ", " +
                ((MyApplication) this.getApplication()).getInformation("PostCode") + " " +
                ((MyApplication) this.getApplication()).getInformation("City"));
        communeHomeDescrition.setText(((MyApplication) this.getApplication()).getInformation("Description"));

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        final TextView home_DateDisplay = (TextView) findViewById(R.id.home_DateDisplay);
        home_DateDisplay.setText(formatDate(today.monthDay,today.month,today.year));
    }

    public void init() {
        asyncTaskMethod="getInformation";
        task = new PostResponseAsyncTask(this);
        try {
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=getInformation&Email=" + ((MyApplication) this.getApplication()).getUserEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Button cashBtn = (Button)findViewById(R.id.home_cashBtn);
        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashManagement();
            }
        });
    }

    private void cashManagement(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(c,R.style.AlertDialogCustom));
        final EditText edittext = new EditText(c);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        alert.setMessage("WG Kasse");
        alert.setTitle("Zahlung erfassen");
        edittext.setTextColor(Color.WHITE);
        alert.setView(edittext);
        alert.setPositiveButton("Einzahlen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String CashText = edittext.getText().toString();
                if (!CashText.equals("")){
                    task = new PostResponseAsyncTask(controller);
                    asyncTaskMethod="addCash";
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=addCashToCommune&CommuneID="
                            +((MyApplication) c.getApplication()).getInformation("CommuneID")
                            + "&CurrentCash="+((MyApplication) c.getApplication()).getInformation("CommuneCashbox")
                            +"&Cash="+CashText+"&CashType=sum");
                }
            }
        });

        alert.setNegativeButton("Abziehen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String CashText = edittext.getText().toString();
                if (!CashText.equals("")){
                    task = new PostResponseAsyncTask(controller);
                    asyncTaskMethod="addCash";
                    task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=addCashToCommune&CommuneID="
                            +((MyApplication) c.getApplication()).getInformation("CommuneID")
                            + "&CurrentCash="+((MyApplication) c.getApplication()).getInformation("CommuneCashbox")
                            +"&Cash="+CashText+"&CashType=sub");
                }
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
            builder.setMessage("Möchten Sie die App schließen? ")
                    .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Logout();
        }else if(id == R.id.action_info){
            Intent intent = new Intent(Home.this, app_info.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shoppingTask) {
            Intent intent = new Intent(Home.this, purchasePlan.class);
            startActivity(intent);
        } else if (id == R.id.nav_cleaningTask) {
            Intent intent = new Intent(Home.this, cleaningRoster.class);
            startActivity(intent);
        } else if (id == R.id.nav_commune_setting) {
            Intent intent = new Intent(Home.this, commune_info.class);
            startActivity(intent);
        }else if (id == R.id.nav_Logout) {
                Logout();
        }else if (id == R.id.nav_app_information) {
            Intent intent = new Intent(Home.this, app_info.class);
            startActivity(intent);
        }else if (id == R.id.nav_edit_users) {
            Intent intent = new Intent(Home.this, activity_userInfo.class);
            startActivity(intent);
        }else if (id == R.id.nav_activityTask) {
            Intent intent = new Intent(Home.this, activities.class);
            startActivity(intent);
        }else if (id == R.id.nav_manage_users){
            Intent intent = new Intent(Home.this, inhabitant_list.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(String s){
        switch (asyncTaskMethod) {
            case "getInformation":
                if (s.equals("null")){
                    finish();
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    Lib.showMessage("Der Administrator hat Sie aus der WG entfernt.",this);
                }
                else
                {
                    try {
                        ((MyApplication) this.getApplication()).setInformationArray(new JSONArray(s));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "addCash":
                UpdateApp();
                break;
        }
        task = new PostResponseAsyncTask(this);
        setHomeLayoutInformation();
    }

    private void UpdateApp(){
        finish();
        startActivity(getIntent());
    }

    private void Logout(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        builder.setMessage("Möchten Sie sich abmelden? ")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Home.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    private String formatDate(int day, int month, int year){
        String sMonth;
        String sDay;
        month++;

        if (day < 10)
        {
            sDay = "0" + day;
        }
        else
        {
            sDay = Integer.toString(day);
        }

        if (month < 10)
        {
            sMonth = "0" + month;
        }
        else
        {
            sMonth = Integer.toString(month);
        }
        return sDay + "." + sMonth + "." + year;
    }
}
