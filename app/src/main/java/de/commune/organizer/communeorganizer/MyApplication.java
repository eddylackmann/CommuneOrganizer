package de.commune.organizer.communeorganizer;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tka on 10.07.2017.
 */

public class MyApplication extends Application {
    private String userEmail;
    private int communeID;
    private boolean userLoggedIn;
    private JSONArray informationArray;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String newUserEmail) {
        userEmail = newUserEmail;
    }

    public int getCommuneID() {
        return communeID;
    }

    public void setCommuneID(int newCommuneID) {
        communeID = newCommuneID;
    }

    public boolean getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean newUserLoggedIn) {
        userLoggedIn = newUserLoggedIn;
    }

    public String getInformation(String field) {
        String result = "";
        try {
            for (int i = 0; i < this.informationArray.length(); i++) {
                JSONObject row = this.informationArray.getJSONObject(i);
                result = row.getString(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setInformationArray(JSONArray newInformationArray) {
        informationArray = newInformationArray;
    }
}