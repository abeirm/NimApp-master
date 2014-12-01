package com.team10.huyqp_000.nim;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.app.ActionBar;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final Button settings = (Button) findViewById(R.id.button2);
        Button gameScreen = (Button) findViewById(R.id.button);
        ActionBar action = getActionBar();
        action.hide();



        //settings.setEnabled(false);
        //settings.setVisibility(View.INVISIBLE);


        Toast.makeText(getApplicationContext(), "hey there", Toast.LENGTH_LONG).show();

        settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(HomeScreen.this, SettingScreen.class);
                startActivity(i);

            }

        });

        gameScreen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent i = new Intent(HomeScreen.this, GameScreen.class);
                startActivity(i);


            }
        });

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);

        return;
    }


}
