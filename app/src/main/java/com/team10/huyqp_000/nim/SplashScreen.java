package com.team10.huyqp_000.nim;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;


public class SplashScreen extends Activity {

    public static final String PREFS_FILE = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        boolean playerFirst = settings.getBoolean("playerFirst", true);
        int rows = settings.getInt("rows", 5);
        boolean fastAnimation = settings.getBoolean("fastAnimation", false);

        // NO TODO:  Check to see if sharedpreferences file already exists.  If not, initialize to default.  DONE

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("playerFirst", playerFirst);
        editor.putInt("rows", rows);
        editor.putBoolean("fastAnimation", fastAnimation);
        editor.commit();

        Thread thread1 = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
        this.finish();


    }


    //TODO:  Check for configuration file.  If not present, then create file with default settings.
    //      Save file is kept even when app is closed.
    //      Save preferences in one of the resource files, like String

    //  Shared preferences are saved as dictionary/hash table.  Just give the key and it'll retrieve the value through the getWhatever methods invoked by the SharedPreferences object.

}
