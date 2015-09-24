package org.singularity.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.singularity.downloads.BusTimeData;
import org.singularity.downloads.BusTimeDoDownload;
import org.singularity.downloads.UpdateDataDownload;
import org.singularity.schedulebus.ScheduleINFO;
import org.singularity.util.CheckData;
import org.singularity.util.ViewPagerAdapter;
import org.felipebertao.singularity.regentsonuniversity.R;
import org.singularity.tabs.SlidingTabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;

    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Weekly Shuttle Time", "Extra Time"};
    int Numboftabs = 2;
    CheckData checkData;
    String TUG = "Print";
    ArrayList<String> scheduleTime_start;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkIfHasDataSchedule()) {

            try {
                checkData = new CheckData(this, this);
                checkData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (IOException e) {
                Log.i(TUG, "Error checkDataTask: " + e.toString());
                e.printStackTrace();
            }
            // Creating The Toolbar and setting it as the Toolbar for the activity

            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);


            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, this, getApplicationContext());

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
        } else {

            String text = " Trying Download Bus Data. Wait...";
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();

            if (haveNetwork()) {

                Intent intent = new Intent(this, Settings.class);
                intent.putExtra("BtnAction", "true");
                startActivity(intent);

            } else {

                startActivity(new Intent(this, Settings.class));
                text = " Please Connect to the Internet and get updated schedule";
                int duration = Toast.LENGTH_LONG;

                toast = Toast.makeText(this, text, duration);
                toast.show();

            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

      if (id == R.id.nextSettings){
          startActivity(new Intent(this, Settings.class));

      }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfHasDataSchedule() {

        String MY_FILE_NAME = "schedule_time.txt";
        File file = new File(getApplicationContext().getFilesDir(), MY_FILE_NAME);

        Boolean value;
        // if file doesnt exists, then create it
        if (file.exists()) {
        value = true;
        } else {
            value = false;
        }

        return value;

    }


    private boolean haveNetwork(){


        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1  8.8.4.4");
            this.dialog.show();
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);

            if(reachable){

                return reachable;
            }
            else{

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

}
