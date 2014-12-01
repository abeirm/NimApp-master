package com.team10.huyqp_000.nim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class GameScreen extends Activity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private ListView listView;
    private MyAdapter adapter;
    private ActionBarDrawerToggle drawerListener;
    public PieceRow currentRowClicked;
    public ArrayList<PieceRow> pieceRowList;

    public int count = 0;  // Amount of pieces selected in that row (pending confirmation)
    public int totalCount = 0;  // Total amount of pieces remaining on the board
    public boolean playerTurn;
    // start is the index position that starts being visible

    public ImageButton increment, decrement, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("onCreate");
        setContentView(R.layout.activity_game_screen);


        /*~~~~~~~ListView listener stuff~~~~~~*/
        listView = (ListView) findViewById(R.id.listView);

        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //listView.setItemChecked(position, true); //says that an item had been checked!


                if (position == 0) {
                    //do things if restart button was pressed
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            GameScreen.this);

                    // set title
                    alertDialogBuilder.setTitle("Restart Game");

                    // set dialog message
                    alertDialogBuilder
                            .setIcon(R.drawable.ic_action_question)
                            .setMessage("Are you sure you want to restart?")
                                    //.setCancelable(false) //this makes it so you can't press back and kill the dialog!
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    PieceRow.setTotalRows(0);
                                    recreate();

                                }
                            })
                            .setNegativeButton("No", null); //null for no listener!

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                } else if (position == 1) {
                    //do things if end button was pressed

                    //must build an AlertDialog using an AlertDialog builder...
                    //create an AlertDialog using the builder settings
                    //then show it


                    //kinda like SharedPreferences.editor()

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            GameScreen.this);

                    // set title
                    alertDialogBuilder.setTitle("Quit Game");

                    // set dialog message
                    alertDialogBuilder
                            .setIcon(R.drawable.ic_action_question)
                            .setMessage("Are you sure you want to quit?")
                                    //.setCancelable(false) //this makes it so you can't press back and kill the dialog!
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity and go back to home screen (the underlying activity)
                                    PieceRow.setTotalRows(0);
                                    GameScreen.this.finish();

                                }
                            })
                            .setNegativeButton("No", null); //null for no listener!

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                } else if (position == 2) {
                    //do things if settings was pressed

                    //gonna open settings activity for now
                    Intent i = new Intent(GameScreen.this, SettingScreen.class);
                    startActivity(i);
                }

            }


        });


        /*~~~~~~~Drawer listeners~~~~~~*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.drawer_resource, R.string.drawer_open, R.string.drawer_close) {};

        drawerLayout.setDrawerListener(drawerListener);

        //makes so that you can click on the icon at the top of the action bar
        getActionBar().setHomeButtonEnabled(true);

        //makes an arrow show up to you can also close the navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences("SettingsFile",0);
        int settingsRow = settings.getInt("rows", 5);
        for(int i = 1; i <= settingsRow; i++)
        {
            totalCount += i;
        }
        playerTurn = settings.getBoolean("playerFirst", true);

        /*~~~~~~ Increment Button Listeners~~*/
        increment = (ImageButton) findViewById(R.id.button4);
        increment.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {

                System.out.println(currentRowClicked + "\tcount: " + count);
                int start = currentRowClicked.getRowID() - currentRowClicked.getPiecesLeft();
                if(count < currentRowClicked.getPiecesLeft()) {
                    count++;
                    currentRowClicked.getButtonList().get(start + count - 1)
                            .setColorFilter(Color.rgb(150, 150, 150));
                }


//HEY WOOOO COM


            }
        });
        decrement = (ImageButton) findViewById(R.id.button3);
        decrement.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {

                System.out.println(currentRowClicked + "\tcount: " + count);
                int start = currentRowClicked.getRowID() - currentRowClicked.getPiecesLeft();
                if(count > 0){
                    currentRowClicked.getButtonList().get(start + count - 1)
                            .setColorFilter(Color.TRANSPARENT);
                    count--;
                }

            }
        });

        confirm = (ImageButton) findViewById(R.id.button5);
        confirm.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(count != 0) { // If you have a valid move
                    // Graphically remove pieces
                    int start = currentRowClicked.getRowID() - currentRowClicked.getPiecesLeft();
                    for (int i = start; i < start + count; i++) {
                        currentRowClicked.getButtonList().get(i).setVisibility(View.INVISIBLE);
                    }

                    // Logically remove pieces
                    currentRowClicked.setPiecesLeft(currentRowClicked.getPiecesLeft() - count);
                    totalCount -= count;


                    // Check victory conditions or pass turn to AI

                    /*
                    if (totalCount == 0) {
                        Toast.makeText(getApplicationContext(), "Congratulations! You've won!", Toast.LENGTH_LONG).show();
                    }
                    */

                    if(!checkVictory()) {

                        System.out.println("total count: " + totalCount);
                        count = 0;

                        playerTurn = false;
                        callAI();
                        // Hey this is bad code maybe but we're gonna do the game end logic right here
                        // GAME END LOGIC HERE
                    }
                    else{



                    } // end the game.  HOW?

                }
                else { // Else piss off
                    System.out.println("try again, select something");
                }

            }
        });



        //logic starts here
        TableRow tr1 = (TableRow) findViewById(R.id.tableRow1);
        TableRow tr2 = (TableRow) findViewById(R.id.tableRow2);
        TableRow tr3 = (TableRow) findViewById(R.id.tableRow3);
        TableRow tr4 = (TableRow) findViewById(R.id.tableRow4);
        TableRow tr5 = (TableRow) findViewById(R.id.tableRow5);
        TableRow tr6 = (TableRow) findViewById(R.id.tableRow6);
        TableRow tr7 = (TableRow) findViewById(R.id.tableRow7);
        TableRow tr8 = (TableRow) findViewById(R.id.tableRow8);
        TableRow tr9 = (TableRow) findViewById(R.id.tableRow9);
        TableRow tr10 = (TableRow) findViewById(R.id.tableRow10);


        this.pieceRowList = new ArrayList<PieceRow>();


        PieceRow pr1 = new PieceRow(tr1);
        PieceRow pr2 = new PieceRow(tr2);
        PieceRow pr3 = new PieceRow(tr3);
        PieceRow pr4 = new PieceRow(tr4);
        PieceRow pr5 = new PieceRow(tr5);
        PieceRow pr6 = new PieceRow(tr6);
        PieceRow pr7 = new PieceRow(tr7);
        PieceRow pr8 = new PieceRow(tr8);
        PieceRow pr9 = new PieceRow(tr9);
        PieceRow pr10 = new PieceRow(tr10);

        //populate the table row list
        pieceRowList.add(pr1);
        pieceRowList.add(pr2);
        pieceRowList.add(pr3);
        pieceRowList.add(pr4);
        pieceRowList.add(pr5);
        pieceRowList.add(pr6);
        pieceRowList.add(pr7);
        pieceRowList.add(pr8);
        pieceRowList.add(pr9);
        pieceRowList.add(pr10);



        currentRowClicked = pr1; //initialize to row1 by default
        pr1.getTableRow().setBackgroundColor(Color.rgb(120, 0, 0));

                //put button rows in PieceRow obj

        ImageView iv = (ImageView) findViewById(R.id.ImageView);
        ImageView iv2 = (ImageView) findViewById(R.id.ImageView2);
        ImageView iv3 = (ImageView) findViewById(R.id.ImageView3);
        ImageView iv4 = (ImageView) findViewById(R.id.ImageView4);
        ImageView iv5 = (ImageView) findViewById(R.id.ImageView5);
        ImageView iv6 = (ImageView) findViewById(R.id.ImageView6);
        ImageView iv7 = (ImageView) findViewById(R.id.ImageView7);
        ImageView iv8 = (ImageView) findViewById(R.id.ImageView8);
        ImageView iv9 = (ImageView) findViewById(R.id.ImageView9);
        ImageView iv10 = (ImageView) findViewById(R.id.ImageView10);
        ImageView iv11 = (ImageView) findViewById(R.id.ImageView11);
        ImageView iv12 = (ImageView) findViewById(R.id.ImageView12);
        ImageView iv13 = (ImageView) findViewById(R.id.ImageView13);
        ImageView iv14 = (ImageView) findViewById(R.id.ImageView14);
        ImageView iv15 = (ImageView) findViewById(R.id.ImageView15);
        ImageView iv16 = (ImageView) findViewById(R.id.ImageView16);
        ImageView iv17 = (ImageView) findViewById(R.id.ImageView17);
        ImageView iv18 = (ImageView) findViewById(R.id.ImageView18);
        ImageView iv19 = (ImageView) findViewById(R.id.ImageView19);
        ImageView iv20 = (ImageView) findViewById(R.id.ImageView20);
        ImageView iv21 = (ImageView) findViewById(R.id.ImageView21);
        ImageView iv22 = (ImageView) findViewById(R.id.ImageView22);
        ImageView iv23 = (ImageView) findViewById(R.id.ImageView23);
        ImageView iv24 = (ImageView) findViewById(R.id.ImageView24);
        ImageView iv25 = (ImageView) findViewById(R.id.ImageView25);
        ImageView iv26 = (ImageView) findViewById(R.id.ImageView26);
        ImageView iv27 = (ImageView) findViewById(R.id.ImageView27);
        ImageView iv28 = (ImageView) findViewById(R.id.ImageView28);
        ImageView iv29 = (ImageView) findViewById(R.id.ImageView29);
        ImageView iv30 = (ImageView) findViewById(R.id.ImageView30);
        ImageView iv31 = (ImageView) findViewById(R.id.ImageView31);
        ImageView iv32 = (ImageView) findViewById(R.id.ImageView32);
        ImageView iv33 = (ImageView) findViewById(R.id.ImageView33);
        ImageView iv34 = (ImageView) findViewById(R.id.ImageView34);
        ImageView iv35 = (ImageView) findViewById(R.id.ImageView35);
        ImageView iv36 = (ImageView) findViewById(R.id.ImageView36);
        ImageView iv37 = (ImageView) findViewById(R.id.ImageView37);
        ImageView iv38 = (ImageView) findViewById(R.id.ImageView38);
        ImageView iv39 = (ImageView) findViewById(R.id.ImageView39);
        ImageView iv40 = (ImageView) findViewById(R.id.ImageView40);
        ImageView iv41 = (ImageView) findViewById(R.id.ImageView41);
        ImageView iv42 = (ImageView) findViewById(R.id.ImageView42);
        ImageView iv43 = (ImageView) findViewById(R.id.ImageView43);
        ImageView iv44 = (ImageView) findViewById(R.id.ImageView44);
        ImageView iv45 = (ImageView) findViewById(R.id.ImageView45);
        ImageView iv46 = (ImageView) findViewById(R.id.ImageView46);
        ImageView iv47 = (ImageView) findViewById(R.id.ImageView47);
        ImageView iv48 = (ImageView) findViewById(R.id.ImageView48);
        ImageView iv49 = (ImageView) findViewById(R.id.ImageView49);
        ImageView iv50 = (ImageView) findViewById(R.id.ImageView50);
        ImageView iv51 = (ImageView) findViewById(R.id.ImageView51);
        ImageView iv52 = (ImageView) findViewById(R.id.ImageView52);
        ImageView iv53 = (ImageView) findViewById(R.id.ImageView53);
        ImageView iv54 = (ImageView) findViewById(R.id.ImageView54);
        ImageView iv55 = (ImageView) findViewById(R.id.ImageView55);



        pr1.buttonList.add(iv);

        pr2.buttonList.add(iv2);
        pr2.buttonList.add(iv3);

        pr3.buttonList.add(iv4);
        pr3.buttonList.add(iv5);
        pr3.buttonList.add(iv6);

        pr4.buttonList.add(iv7);
        pr4.buttonList.add(iv8);
        pr4.buttonList.add(iv9);
        pr4.buttonList.add(iv10);

        pr5.buttonList.add(iv11);
        pr5.buttonList.add(iv12);
        pr5.buttonList.add(iv13);
        pr5.buttonList.add(iv14);
        pr5.buttonList.add(iv15);

        pr6.buttonList.add(iv16);
        pr6.buttonList.add(iv17);
        pr6.buttonList.add(iv18);
        pr6.buttonList.add(iv19);
        pr6.buttonList.add(iv20);
        pr6.buttonList.add(iv21);

        pr7.buttonList.add(iv22);
        pr7.buttonList.add(iv23);
        pr7.buttonList.add(iv24);
        pr7.buttonList.add(iv25);
        pr7.buttonList.add(iv26);
        pr7.buttonList.add(iv27);
        pr7.buttonList.add(iv28);

        pr8.buttonList.add(iv29);
        pr8.buttonList.add(iv30);
        pr8.buttonList.add(iv31);
        pr8.buttonList.add(iv32);
        pr8.buttonList.add(iv33);
        pr8.buttonList.add(iv34);
        pr8.buttonList.add(iv35);
        pr8.buttonList.add(iv36);

        pr9.buttonList.add(iv37);
        pr9.buttonList.add(iv38);
        pr9.buttonList.add(iv39);
        pr9.buttonList.add(iv40);
        pr9.buttonList.add(iv41);
        pr9.buttonList.add(iv42);
        pr9.buttonList.add(iv43);
        pr9.buttonList.add(iv44);
        pr9.buttonList.add(iv45);

        pr10.buttonList.add(iv46);
        pr10.buttonList.add(iv47);
        pr10.buttonList.add(iv48);
        pr10.buttonList.add(iv49);
        pr10.buttonList.add(iv50);
        pr10.buttonList.add(iv51);
        pr10.buttonList.add(iv52);
        pr10.buttonList.add(iv53);
        pr10.buttonList.add(iv54);
        pr10.buttonList.add(iv55);




        for(int i = 9; i > settingsRow-1; i--){
            pieceRowList.get(i).getTableRow().setVisibility(View.GONE);
            pieceRowList.get(i).setPiecesLeft(0);
        }


        for(PieceRow row : pieceRowList)
        {
            row.getTableRow().setClickable(true);
            row.getTableRow().setOnClickListener(this);
        }

        if(!playerTurn)
        {
            callAI();
        }


    }

    // END OF LOGIC
    // END OF LOGIC
    // END OF LOGIC

    public void callAI()
    {
        disableAllButtons();
        makeMove();
        System.out.println("hey I took my turn.");
        checkVictory();
        playerTurn = true;
        enableAllButtons();
    }


    public void clearHighlightRows(PieceRow kid)
    {
            kid.getTableRow().setBackgroundColor(Color.TRANSPARENT);
    }

    public void disableAllButtons()
    {
        increment.setClickable(false);
        decrement.setClickable(false);
        confirm.setClickable(false);
        for(PieceRow row : pieceRowList)
        {
            row.getTableRow().setClickable(false);
        }
    }

    public void enableAllButtons()
    {
        increment.setClickable(true);
        decrement.setClickable(true);
        confirm.setClickable(true);
        for(PieceRow row : pieceRowList)
        {
            row.getTableRow().setClickable(true);
        }
    }

    public void makeMove()
    {
        // Dummy AI for now.
        PieceRow AIPieceRow;
        for(int i = 0; i < PieceRow.getTotalRows(); i++)
        {
            if(pieceRowList.get(i).getPiecesLeft() != 0)
            {
                System.out.println("gonna take from row " + (i+1));
                AIPieceRow = pieceRowList.get(i);
                int start = AIPieceRow.getRowID() - AIPieceRow.getPiecesLeft();
                AIPieceRow.getButtonList().get(start).setVisibility(View.INVISIBLE); // Make the button invisible
                AIPieceRow.setPiecesLeft(AIPieceRow.getPiecesLeft() - 1); // This row has one less piece
                System.out.println("there should be " + AIPieceRow.getPiecesLeft() + " pieces left");
                totalCount--;
                break;
            }
        }
    }

    public boolean checkVictory()
    // if true, someone won
    {
        if(totalCount == 0) // Victory condition
        {
            Toast.makeText(this.getApplicationContext(),  playerTurn ? "Congratulations, you've won!" : "Boo you suck", Toast.LENGTH_SHORT).show();
            return true;
        }
        // else keep going
        else
        {
            return false;
        }
    }

    // Testing shit


    @Override
    protected void onStop() {
        System.out.println("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        System.out.println("onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.out.println("onResume");
        super.onResume();
    }

    // Done testing shit


    @Override

    protected void onPostCreate(Bundle savedInstanceState) { //after activity creation method
        super.onPostCreate(savedInstanceState);

        drawerListener.syncState(); //sync the bar state so the user is seeing how the navigation drawer changes
        //makes it so you can now see the little navigation bars in the corner
        //makes it minimize the bars when open and maximize when it closes
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                GameScreen.this);

        // set title
        alertDialogBuilder.setTitle("Quit Game");

        // set dialog message
        alertDialogBuilder
                .setIcon(R.drawable.ic_action_question)
                .setMessage("Are you sure you want to quit?")
                        //.setCancelable(false) //this makes it so you can't press back and kill the dialog!
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity and go back to home screen (the underlying activity)
                        PieceRow.setTotalRows(0);
                        GameScreen.this.finish();
                    }
                })
                .setNegativeButton("No", null); //null for no listener!

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //onHomeIconSelected (basically)

        //This if statement "forwards" the action of pressing the icon button on the action bar
        // to the drawer listener. so now we can now connect clicking the icon at the top to opening the navigation drawer!
        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        clearPieces(currentRowClicked);
        clearHighlightRows(currentRowClicked);

        switch(v.getId())
        {
            case R.id.tableRow1 :

                this.currentRowClicked = pieceRowList.get(0);

                break;

            case R.id.tableRow2 :
                this.currentRowClicked = pieceRowList.get(1);

                break;

            case R.id.tableRow3 :
                this.currentRowClicked = pieceRowList.get(2);

                break;

            case R.id.tableRow4 :
                this.currentRowClicked = pieceRowList.get(3);

                break;

            case R.id.tableRow5 :
                this.currentRowClicked = pieceRowList.get(4);

                break;

            case R.id.tableRow6 :
                this.currentRowClicked = pieceRowList.get(5);

                break;

            case R.id.tableRow7 :
                this.currentRowClicked = pieceRowList.get(6);

                break;

            case R.id.tableRow8 :
                this.currentRowClicked = pieceRowList.get(7);

                break;

            case R.id.tableRow9 :
                this.currentRowClicked = pieceRowList.get(8);

                break;

            case R.id.tableRow10 :
                this.currentRowClicked = pieceRowList.get(9);

                break;

        }
        count = 0;
        v.setBackgroundColor(Color.rgb(120, 0, 0));
    }

    public void clearPieces(PieceRow row){

        for(ImageView piece : row.getButtonList())
        {
                piece.setColorFilter(Color.TRANSPARENT);
        }
    }

}


class MyAdapter extends BaseAdapter {
    private Context context;
    private String[] options;
    private int[] images = {R.drawable.ic_action_restart,
            R.drawable.ic_action_close,
            R.drawable.ic_action_settings};

    public MyAdapter(Context context) {
        options = context.getResources().getStringArray(R.array.options);
        this.context = context;

    }

    @Override
    public int getCount() {
        return options.length;
    }

    @Override
    public Object getItem(int i) {
        return options[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = null;
        if (row == null) { //creating row for first time

            //convert xml to java object
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.custom_row, viewGroup, false);
        } else {
            row = view;

        }

        TextView text = (TextView) row.findViewById(R.id.textView);
        ImageView image = (ImageView) row.findViewById(R.id.imageView);

        text.setText(options[i]);

        image.setImageResource(images[i]);


        return row;
    }


}


class PieceRow {

    ArrayList<ImageView> buttonList = new ArrayList<ImageView>();
    private TableRow tableRow;
    private int rowID;
    private static int totalRows = 0;
    private int piecesLeft;

    public PieceRow(TableRow tableRow)
    {
        this.tableRow = tableRow;
        this.rowID = ++totalRows;
        this.piecesLeft = rowID;

    }

    public ArrayList<ImageView> getButtonList() {
        return buttonList;
    }

    public void setButtonList(ArrayList<ImageView> buttonList) {
        this.buttonList = buttonList;
    }

    public TableRow getTableRow() {
        return tableRow;
    }

    public void setTableRow(TableRow tableRow) {
        this.tableRow = tableRow;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public static int getTotalRows() {
        return totalRows;
    }

    public static void setTotalRows(int totalRows) {
        PieceRow.totalRows = totalRows;
    }

    public int getPiecesLeft() {
        return piecesLeft;
    }

    public void setPiecesLeft(int piecesLeft) {
        this.piecesLeft = piecesLeft;
    }
    @Override
    public String toString(){
        return "Row: " + rowID + "\tPieces Left: " + piecesLeft;
    }
}
