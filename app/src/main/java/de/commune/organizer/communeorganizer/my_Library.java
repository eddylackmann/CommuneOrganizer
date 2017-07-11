package de.commune.organizer.communeorganizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.AsyncResponse;
import org.json.JSONArray;

/**
 * Created by Ray on 04.07.2017.
 */

public class my_Library {


    public void showMessage(String s,  AppCompatActivity a){
        AlertDialog.Builder AlertBox = new AlertDialog.Builder(a);
        AlertBox.setMessage(s);
        AlertBox.create();
        AlertBox.show();
     }




}
