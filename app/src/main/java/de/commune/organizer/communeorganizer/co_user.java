package de.commune.organizer.communeorganizer;

/**
 * Created by Ray on 06.07.2017.
 */

public class co_user {

    public int commune_id,commune_admin;

    public String email, password,firstname,lastname,birthday;


    public boolean Login(String email, String pW){

        if(email=="a.eddy@hotmail.de" & pW=="test"){
            return true;
        }else{
            return false;
        }


    }
}

