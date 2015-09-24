package org.singularity.core;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import org.felipebertao.singularity.regentsonuniversity.R;

import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RowAcitivity extends ListActivity {

    String[][] scheduleTime;
    ArrayList<String> times;
    String[] headerSchedule = {" Depart RU Office", " Light Rail (drop off only)", " SDFC (ASU gym)",
            " Garage (MU)", " Univ.&College", " Univ.Bridge"};
    MySimpleArrayAdapter adapter;
    Context cotext;

    public RowAcitivity(Context ctx){
        cotext = ctx;
        scheduleTime = startScheduleDataTime();
        times = new ArrayList<>();

    }
    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        currentTime();
        String[] valuesTwo = new String[]{"1", "2", "3",
                "4", "5", "6", "7", "8",
                "9", "10"};
        // use your custom layout
        adapter = new MySimpleArrayAdapter(this, headerSchedule, times, times);
        setListAdapter(adapter);

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
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

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void currentTime() {
        //EditText t = (EditText) findViewById(R.id.editText);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        Date dt = new Date();
        String Time1 = sdf.format(dt);
        // t.setText(Time1);
        dataSchedule(Time1);
    }



    public void dataSchedule(String pCurrentTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        java.sql.Time timeValue;

        String currentTimeTest_2 = "09:25 AM";
        Time currentTimeTest = null;
        try {
            currentTimeTest = new Time(formatter.parse(currentTimeTest_2).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // EditText t = (EditText) findViewById(R.id.editText2);
        int aux = 0;

        for (int i = 0; i < scheduleTime.length; i++) {
            for (int j = 0; j < scheduleTime[i].length; j++) {

                try {
                    Date date = null;
                        date = formatter.parse(scheduleTime[i][j]);
                    String dateN = formatter.format(date);

                    timeValue = new java.sql.Time(formatter.parse(scheduleTime[i][j]).getTime());
                    System.out.println(timeValue);
                    System.out.println(currentTimeTest);

                    if (currentTimeTest.before(timeValue)) {
                        if (aux < 7) {
                            System.out.println(timeValue + ("esse valor e antes"));
//                            t.setText(scheduleTime[i][j]);
                            times.add(scheduleTime[i][j]);
                            aux++;
                        } else if (aux == 0) {
                            //  t.setText("Nao ha horario disponivel");}
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                }
            }
        }


    private String[][] startScheduleDataTime() {

        File file = new File(cotext.getFilesDir() + "/" + "schedule_time.txt");



        String[][] scheduleTime_start =
                { {"07:00 AM", "07:05 AM", "07:10 AM", "07:15 AM", "07:20 AM", "07:25 AM"},
                {"07:35 AM", "07:40 AM", "07:45 AM", "07:50 AM", "07:55 AM", "08:00 AM"},
                {"08:10 AM", "08:15 AM", "08:20 AM", "08:25 AM", "08:30 AM", "08:35 AM"},
                {"08:45 AM", "08:50 AM", "08:55 AM", "09:00 AM", "09:05 AM", "09:10 AM"},
                {"09:20 AM", "09:25 AM", "09:30 AM", "09:35 AM", "09:40 AM", "09:45 AM"},
                {"09:55 AM", "10:00 AM", "10:05 AM", "10:10 AM", "10:15 AM", "10:20 AM"},
                {"10:30 AM", "10:35 AM", "10:40 AM", "10:45 AM", "10:50 AM", "10:55 AM"},
                {"11:05 AM", "11:10 AM", "11:15 AM", "11:20 AM", "11:25 AM", "11:30 AM"},
                {"12:15 PM", "12:20 PM", "12:25 PM", "12:30 PM", "12:35 PM", "12:40 PM"},
                {"12:50 PM", "12:55 PM", "01:00 PM", "01:05 PM", "01:10 PM", "01:15 PM"},
                {"01:25 PM", "01:30 PM", "01:35 PM", "01:40 PM", "01:45 PM", "01:50 PM"},
                {"02:00 PM", "02:05 PM", "02:10 PM", "02:15 PM", "02:20 PM", "02:25 PM"},
                {"02:35 PM", "02:40 PM", "02:45 PM", "02:50 PM", "02:55 PM", "03:00 PM"},
                {"03:15 PM", "03:20 PM", "03:25 PM", "03:30 PM", "03:35 PM", "03:40 PM"},
                {"04:00 PM", "04:05 PM", "04:10 PM", "04:15 PM", "04:20 PM", "04:25 PM"},
                {"04:45 PM", "04:50 PM", "04:55 PM", "05:00 PM", "05:05 PM", "05:10 PM"},
                {"05:30 PM", "05:35 PM", "05:40 PM", "05:45 PM", "05:50 PM", "05:55 PM"},
                {"06:15 PM", "06:20 PM", "06:25 PM", "06:30 PM", "06:35 PM", "06:40 PM"},};

        return scheduleTime_start;
    }


}
