package org.singularity.downloads;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.singularity.schedulebus.ScheduleExtraINFO;
import org.singularity.util.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.singularity.schedulebus.ScheduleINFO;

import java.util.ArrayList;

/**
 * Created by The Machine on 8/4/2015.
 */
 public class BusTimeData extends AsyncTask <String, Void, ArrayList<ScheduleINFO>> {


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_MESSAGE = "message";

    private static final String URL = "http://brasileiro.net.br/downloadBusSchedule.php";
    private static final String EXTRA_SCHEDULE = "http://brasileiro.net.br/downloadBusScheduleExtra.php";


    private ArrayList<ScheduleINFO> busTimeData;
    private ArrayList<ScheduleExtraINFO> busExtraTimeData;
    private JSONArray mData = null;
    private ListActivity activity;
    private ProgressDialog dialog;
    private Context mContext;


    public BusTimeData (Context value){
        System.out.println("Enrou BustTimeData");
        mContext = value;

    }



    @Override
    protected ArrayList<ScheduleINFO> doInBackground(String... params) {
        busTimeDownload();
              return busTimeData;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    protected void onPostExecute(Long result) {

    }


    public void busTimeDownload() {

        Gson gson = new Gson();
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content.
        busTimeData = new ArrayList<>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(URL);
        JSONObject c = null;

        try {
            if (json.getString(TAG_MESSAGE).compareTo("No Data Available!") != 0) {
                mData = json.getJSONArray(TAG_POSTS);

                // looping through all SeriosDB data acccording to the json object returned
                for (int i = 0; i < mData.length(); i++) {

                    c = mData.getJSONObject(i);

                    //gets the content of each tag
                    /* it is to translate the Obj JSON to a ProductBigOjb*/
                   String jsons = gson.toJson(c);  /* Obj from fromJSON*/
                   ScheduleINFO objAux = gson.fromJson(jsons, ScheduleINFO.class);  /* Obj to OBJ*/
                   busTimeData.add(objAux);
                }
            } else {
                System.out.println(json.getString(TAG_MESSAGE) + "(BUS SCHEDULE)");
            }

                busTimeData = bubbleSort(busTimeData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<ScheduleINFO> bubbleSort(ArrayList<ScheduleINFO> toSort){
        int i, bottom;
        ScheduleINFO temp;
        boolean exchanged = true;

        bottom = toSort.size()-2;

        while (exchanged){
            exchanged = false;
            for (i = 0; i<=bottom; i++){
                int a = Integer.parseInt(toSort.get(i).getGeneric("index"));
                int b = Integer.parseInt(toSort.get(i+1).getGeneric("index"));
                if (a > b){
                    temp = toSort.get(i);
                    toSort.set(i, toSort.get(i+1));
                    toSort.set(i+1, temp);
                    exchanged = true;
                }

            }


        }
        return toSort;
    }


    public ArrayList<ScheduleINFO> getBusTimeData (){
        return busTimeData;
    }

}
