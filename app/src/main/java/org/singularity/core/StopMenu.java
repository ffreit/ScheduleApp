package org.singularity.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.felipebertao.singularity.regentsonuniversity.R;
import org.singularity.downloads.BusTimeDoDownload;
import org.singularity.schedulebus.ScheduleINFO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StopMenu extends ActionBarActivity {
    public static String newline = System.getProperty("line.separator");
    Toolbar toolbar;
    Button button;
    String TUG = "Print: Settings";
    String toastTxt;
    ArrayList<String> scheduleTime;
    int positionStop;
    ListView myListView;
    private View v;
    AdapterStops adapter;

    String[] headerSchedule = {" Depart RU Office", " Light Rail (drop off only)", " SDFC (ASU gym)",
            " Garage (MU)", " Univ.&College", " Univ.Bridge"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        scheduleTime = extras.getStringArrayList("scheduleTime");
        setContentView(R.layout.activity_stops_menu);
        scheduleTime = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListView(scheduleTime);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.home){

            NavUtils.navigateUpFromSameTask(this);


        }
        return super.onOptionsItemSelected(item);
    }

    public void setListView(ArrayList<String> listView) {

        myListView = (ListView) v.findViewById(R.id.myListView);
        adapter = new AdapterStops(this, headerSchedule, scheduleTime);

    }
}
