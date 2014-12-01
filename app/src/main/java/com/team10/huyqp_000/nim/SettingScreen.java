package com.team10.huyqp_000.nim;

//import android.app.ActionBar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingScreen extends Activity {
    public static final String PREFS_FILE = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        final SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        final SharedPreferences.Editor editor = settings.edit();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        boolean firstPlayer = settings.getBoolean("playerFirst", true);
        int rows = settings.getInt("rows", 5);
        boolean fastAnimation = settings.getBoolean("fastAnimation", false);


        SeekBar seek = (SeekBar) findViewById(R.id.seekBar);

        seek.setMax(7); //use the range 0-7 and add 3 to it to get 3-10 because you can't set min of seekbar (defaults to 0)
        final TextView seekBarValue = (TextView) findViewById(R.id.seekValue);
        seekBarValue.setText(String.valueOf(rows));
        seek.setProgress(rows - 3);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress + 3));
                editor.putInt("rows", progress + 3);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // NOTHING
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Nothing

            }
        });


        Switch playerSwitch = (Switch) findViewById(R.id.switch1);
        playerSwitch.setChecked(firstPlayer);

        playerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(getApplicationContext(), "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();
                if (isChecked) {
                    editor.putBoolean("playerFirst", true);
                } else {
                    editor.putBoolean("playerFirst", false);
                }
                editor.commit();
            }

        });
        Switch animationSwitch = (Switch) findViewById(R.id.switch2);
        animationSwitch.setChecked(fastAnimation);
        animationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(getApplicationContext(), "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();
                if (isChecked) {
                    editor.putBoolean("fastAnimation", true);
                } else {
                    editor.putBoolean("fastAnimation", false);
                }
                editor.commit();
            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //onHomeIconSelected (basically)

        //This if statement "forwards" the action of pressing the icon button on the action bar
        // to the drawer listener. so now we can now connect clicking the icon at the top to opening the navigation drawer!
        this.finish();
        return super.onOptionsItemSelected(item);
    }


}

