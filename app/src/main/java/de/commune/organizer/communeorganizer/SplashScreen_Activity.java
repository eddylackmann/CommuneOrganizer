package de.commune.organizer.communeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen_Activity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen_layout);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen_Activity.this, MainActivity.class);
                SplashScreen_Activity.this.startActivity(mainIntent);
                SplashScreen_Activity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
