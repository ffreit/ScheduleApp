package org.singularity.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.felipebertao.singularity.regentsonuniversity.R;
import org.singularity.core.MainActivity;
import org.singularity.core.MySimpleArrayAdapter;
import org.singularity.core.Settings;
import org.singularity.core.StopMenu;
import org.singularity.downloads.BusTimeDoDownload;
import org.singularity.schedulebus.ScheduleINFO;
import org.singularity.util.ArrayOfArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment {


    ArrayList <String> scheduleTime;
    ArrayOfArrayList stopsList;

    String[] headerSchedule = {" Depart RU Office", " Light Rail (drop off only)", " SDFC (ASU gym)",
            " Garage (MU)", " Univ.&College", " Univ.Bridge"};

    ArrayList <String> times;
    ArrayList <String> timesAfter;
    MySimpleArrayAdapter adapter;

    String[] values;

    ListView myListView;
    private String[] strListView;
    private Context context;
    private ProgressDialog barProgressDialog;
    private Bundle savedInstanceState;
    private View v;


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.savedInstanceState = savedInstanceState;

        scheduleTime = new ArrayList<String>();
        stopsList = new ArrayOfArrayList(6);

        times = new ArrayList<String>();
        timesAfter = new ArrayList<String>();
        context = getActivity().getBaseContext();

        v = inflater.inflate(R.layout.tab_1, container, false);

        myListView = (ListView) v.findViewById(R.id.myListView);

        try {
            startScheduleDataTime();
            dataSchedule();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        adapter = new MySimpleArrayAdapter(context, headerSchedule, times, timesAfter);
        myListView.setAdapter(adapter);


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> optionsStops = new ArrayList<String>();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View convertView = (View) inflater.inflate(R.layout.list, null);
                alertDialog.setView(convertView);

                if (position == 0) {
                    alertDialog.setTitle("Depart RU Office");
                    optionsStops = stopsList.get(0);
                } else if (position == 1) {
                    alertDialog.setTitle("Light Rail");
                    optionsStops = stopsList.get(1);
                } else if (position == 2) {
                    alertDialog.setTitle("SDFC (Gym) ");
                    optionsStops = stopsList.get(2);
                } else if (position == 3) {
                    alertDialog.setTitle("Garage (MU) ");
                    optionsStops = stopsList.get(3);
                } else if (position == 4) {
                    alertDialog.setTitle("Univ.& College ");
                    optionsStops = stopsList.get(4);
                } else {
                    alertDialog.setTitle("Univ. Bridge ");
                    optionsStops = stopsList.get(5);
                }

                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(((MainActivity) getActivity()).getBaseContext(), android.R.layout.simple_list_item_1, optionsStops);
                lv.setAdapter(adapter);
                alertDialog.show();


            }
        });

        return v;
    }

    public void dataSchedule() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

        String currentTimeString = formatter.format(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        //String currentTimeStringTest = "07:50 PM";
        Date dateCurrent = formatter.parse(currentTimeString);
        Time currentTime = new Time(dateCurrent.getTime());
        Time stopTime = null;


        Log.i("dateCurrent", dateCurrent.toString());
        Log.i("currentTimeString", currentTimeString);
        Log.i("currentTime", currentTime.toString());
        switch (day){
                    case Calendar.SUNDAY:
                        break;
                    case Calendar.SATURDAY:
                        break;
                    default:
                        int j = 0, controlled = 0;

                        for (int i = 0; i < stopsList.size(); i++) {

                    j = 0;
                    controlled = 1;
                    while (controlled == 1) {

                        String timeAux = stopsList.getValue(i, j);
                        Date date = formatter.parse(timeAux);
                        stopTime = new Time(date.getTime());

                        if (currentTime.before(stopTime)) {

                            times.add(stopsList.getValue(i, j));

                            if (j == stopsList.sizeArray(i) - 1) {
                                timesAfter.add("None");

                            } else {
                                Log.i("J:", String.valueOf(j));

                                timesAfter.add(stopsList.getValue(i, j + 1));
                            }

                            Log.i("Size ", String.valueOf(stopsList.sizeArray(i) - 1));
                            controlled = 0;

                        } else {
                            if (j == stopsList.sizeArray(i) - 1 && controlled == 1) {
                                controlled = 0;
                                times.add("None");
                                timesAfter.add("None");
                            }

                        }
                        j++;
                    }
                } // end for
                        break;
        }// END  case

        if ( (times.isEmpty() && timesAfter.isEmpty())) {
            times.add("None");
            times.add("None");
            times.add("None");
            times.add("None");
            times.add("None");
            times.add("None");

            timesAfter = times;

        }


    }

    private void  startScheduleDataTime() throws IOException {

        String MY_FILE_NAME = "schedule_time.txt";
        File file = new File(context.getFilesDir(), MY_FILE_NAME);


        if (!file.exists()) {
            Log.i("Nao existe", "File Nao exsite");
            scheduleTime.add("None");
            scheduleTime.add("None");
            scheduleTime.add("None");
            scheduleTime.add("None");
            scheduleTime.add("None");
            scheduleTime.add("None");
            timesAfter = times;
            int j = 0;
            for (int i = 0; i < scheduleTime.size(); i++) {

                stopsList.addValue(j, scheduleTime.get(i));

                if (j == 5) {
                    j = 0;
                } else {
                    j++;
                }


            }

        } else {
            BufferedReader br = new BufferedReader(new FileReader(file), 2048);
            String line;

            while ((line = br.readLine()) != null) {
                String[] RowData = line.split(",");

                for (int i = 0; i < RowData.length; i++) {
                    scheduleTime.add(RowData[i]);
                }
            }
            br.close();

        /*it is to create a ArrayList with the time to each stop */
            int j = 0;
            for (int i = 0; i < scheduleTime.size(); i++) {

                stopsList.addValue(j, scheduleTime.get(i));

                if (j == 5) {
                    j = 0;
                } else {
                    j++;
                }


            }
        }
    }

}