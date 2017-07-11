package de.commune.organizer.communeorganizer;
import android.app.Application;
import android.os.AsyncTask;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tka on 10.07.2017.
 */

public class MyApplication extends Application {
    private String userEmail;
    private String userPassword;
    private Boolean userLoggedIn;
    private JSONArray userArray;

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String newUserEmail){
        userEmail = newUserEmail;
    }
    public String getUserPassword(){
        return userPassword;
    }
    public void setUserPassword (String newUserPassword){
        userPassword = newUserPassword;
    }
    public Boolean getUserLoggedIn(){
        return userLoggedIn;
    }
    public void setUserLoggedIn (Boolean newUserLoggedIn){
        userLoggedIn = newUserLoggedIn;
    }

    public String getUserInformation(String field){
        String result = "";
        try {
            for (int i = 0; i < this.userArray.length(); i++) {
                JSONObject row = this.userArray.getJSONObject(i);
                result = row.getString(field);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void setUserArray(JSONArray newUserArray){
        userArray = newUserArray;
    }
}