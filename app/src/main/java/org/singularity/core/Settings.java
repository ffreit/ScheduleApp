package org.singularity.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileWriter;
import java.lang.Object;

import org.felipebertao.singularity.regentsonuniversity.R;
import org.singularity.downloads.BusTimeDoDownload;
import org.singularity.downloads.UpdateDataDownload;
import org.singularity.releasesData.DataUpdateINFO;
import org.singularity.schedulebus.ScheduleExtraINFO;
import org.singularity.schedulebus.ScheduleINFO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Settings extends ActionBarActivity {

    public static String newline = System.getProperty("line.separator");
    Toolbar toolbar;
    Button button;
    String TUG = "Print: Settings";
    String toastTxt;
    ArrayList<String> scheduleTime_start;
    ArrayList<String> scheduleTimeExtra_start;
    int passHere = 1;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        scheduleTime_start = new ArrayList<>();
        scheduleTimeExtra_start =  new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addListenerOnButton();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String value = extras.getString("BtnAction");

            if (value.compareTo("true") == 0) {


                passHere = 0;
                button.performClick();
                this.finish();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        }

    }

    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.updSchedule);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (haveNetwork() && passHere == 1) {
                    doDownload();
                    try {
                        makeTheFile();
                        makeTheExtraFile();
                        updateFileUpdate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (passHere == 0) {
                    doDownload();
                    try {
                        makeTheFile();
                        makeTheExtraFile();
                        updateFileUpdate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    CharSequence text = "Ops! Check your internet connection!";


                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

    }

    private void updateFileUpdate() throws IOException {

        String MY_FILE_NAME = "data_update.txt";
        File file = new File(this.getFilesDir() + "/" + "data_update.txt");
        int myBufferSize = 2048;

        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        String updateDB = doDownloadUpdateInfO();

        PrintWriter out = new PrintWriter(file); /* to writte on file */

        out.println(updateDB);
        out.close(); /*close writte*/
    }

    private void makeTheExtraFile() throws IOException {
        String MY_FILE_NAME = "schedule_extra_time.txt";
        int myBufferSize = 2048;
        // Get the directory for the user's public pictures directory.

        File file = new File(getApplicationContext().getFilesDir(), MY_FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        PrintWriter out = new PrintWriter(file); /* to writte on file */


        for (int i = 0; i < scheduleTimeExtra_start.size(); i++) {
            out.println(scheduleTimeExtra_start.get(i));
            out.println(",");
        }
        out.close(); /*close writte*/

       // Log.i(TUG, "Caminho File depois download: " + file.getAbsolutePath());

        /* To read the file and save in a DB*/
        BufferedReader br = new BufferedReader(new FileReader(file), myBufferSize);
        String line;
        ArrayList<String> testDB = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] RowData = line.split(",");

            for (int i = 0; i < RowData.length; i++) {
                testDB.add(RowData[i]);
            }
        }

        br.close();
    }
    private void makeTheFile() throws IOException {
        String MY_FILE_NAME = "schedule_time.txt";
        int myBufferSize = 2048;
        // Get the directory for the user's public pictures directory.

        File file = new File(getApplicationContext().getFilesDir(), MY_FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        PrintWriter out = new PrintWriter(file); /* to writte on file */


        for (int i = 0; i < scheduleTime_start.size(); i++) {
            out.println(scheduleTime_start.get(i));
            out.println(",");
        }
        out.close(); /*close writte*/

        Log.i(TUG, "Caminho File depois download: " + file.getAbsolutePath());

        /* To read the file and save in a DB*/
        BufferedReader br = new BufferedReader(new FileReader(file), myBufferSize);
        String line;
        ArrayList<String> testDB = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] RowData = line.split(",");

            for (int i = 0; i < RowData.length; i++) {
                testDB.add(RowData[i]);
            }
        }

        br.close();
    }

    private void doDownload() {


        doDownloadSchedule();
    }

    private String doDownloadUpdateInfO() {

        UpdateDataDownload udd = new UpdateDataDownload(getApplicationContext());

        try {
            udd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return udd.getBusTimeData();
    }


    private void doDownloadExtraSchedule() {


    }

    private void doDownloadSchedule() {

        BusTimeDoDownload btdd = new BusTimeDoDownload(this);
        ArrayList<ScheduleINFO> scheduleDB = btdd.getSchedule();
        ArrayList<ScheduleExtraINFO> scheduleExtraDB = btdd.getScheduleExtra();

        String[] headerCommands = {"departRU", "lighRAIL", "sdfcGYM", "garageMU", "univCollege", "univBridge"};
        String[] headerCommands2 = {"index", "destination", "departureTime", "pickUpTime", "day"};

        for (int i = 0; i < scheduleDB.size(); i++) {

            for (int j = 0; j < headerCommands.length; j++) {

                scheduleTime_start.add(scheduleDB.get(i).getGeneric(headerCommands[j]));
            }

        }

        for (int i = 0; i < scheduleExtraDB.size(); i++) {

            for (int j = 0; j < headerCommands2.length; j++) {

                scheduleTimeExtra_start.add(scheduleExtraDB.get(i).getGeneric(headerCommands[j]));

            }

        }

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

        if (id == R.id.home) {

            NavUtils.navigateUpFromSameTask(this);


        }
        return super.onOptionsItemSelected(item);
    }

    private boolean haveNetwork() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1   8.8.4.4");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                System.out.println("Internet access");
                return reachable;
            } else {
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
}
