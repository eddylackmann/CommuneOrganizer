package de.commune.organizer.communeorganizer;

import android.app.Application;

/**
 * Created by tka on 10.07.2017.
 */

public class MyApplication extends Application {
    private String userEmail;
    private String userPassword;
    private Boolean userLoggedIn;

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
}
