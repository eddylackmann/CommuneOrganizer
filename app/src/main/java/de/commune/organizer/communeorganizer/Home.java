package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    private PostResponseAsyncTask task;
    private my_Library Lib = new my_Library();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();
    }


    public void setHomeLayoutInformation(){

        TextView userDetailsHome = (TextView) findViewById(R.id.userHomeDescrition);
        TextView homeCommuneCash = (TextView) findViewById(R.id.home_communeCash);
        TextView addressHome    = (TextView) findViewById(R.id.addressHomeDescrition);

        userDetailsHome.setText(((MyApplication) this.getApplication()).getInformation("Firstname") + " " + ((MyApplication) this.getApplication()).getInformation("Lastname"));
        homeCommuneCash.setText("Kasse: " + ((MyApplication) this.getApplication()).getInformation("CommuneCashbox") + " €");
        addressHome.setText(((MyApplication) this.getApplication()).getInformation("Address") + ", " +
                            ((MyApplication) this.getApplication()).getInformation("PostCode") + " " +
                            ((MyApplication) this.getApplication()).getInformation("City"));
    }

    public void init(){
        task = new PostResponseAsyncTask(this);
        try {
            task.execute("http://eddy-home.ddns.net/wg-app/loginMgt.php?Method=getInformation&Email=" + ((MyApplication) this.getApplication()).getUserEmail());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(Home.this, purchasePlan.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Home.this, cleaningRoster.class);
            startActivity(intent);
        } else if (id == R.id.nav_commune_setting) {
            Intent intent = new Intent(Home.this, commune_info.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(String s){
        try{
            ((MyApplication) this.getApplication()).setInformationArray(new JSONArray(s));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        task = new PostResponseAsyncTask(this);
        setHomeLayoutInformation();

    }
}
