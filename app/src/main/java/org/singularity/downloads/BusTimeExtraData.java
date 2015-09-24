package org.singularity.downloads;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.singularity.schedulebus.ScheduleExtraINFO;
import org.singularity.schedulebus.ScheduleINFO;
import org.singularity.util.JSONParser;

import java.util.ArrayList;

/**
 * Created by The Machine on 9/2/2015.
 */
public class BusTimeExtraData  extends AsyncTask<String, Void, ArrayList<ScheduleExtraINFO>> {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_MESSAGE = "message";

    private static final String EXTRA_SCHEDULE = "http://brasileiro.net.br/downloadBusScheduleExtra.php";


    private ArrayList<ScheduleExtraINFO> busExtraTimeData;
    private JSONArray mData = null;
    private ListActivity activity;
    private ProgressDialog dialog;
    private Context mContext;

    public BusTimeExtraData (Context value){
          mContext = value;

    }

    @Override
    protected ArrayList<ScheduleExtraINFO> doInBackground(String... params) {

        busExtraScheduleDownload();
        return busExtraTimeData;
    }

    private void busExtraScheduleDownload() {
        Gson gson = new Gson();

        busExtraTimeData = new ArrayList();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(EXTRA_SCHEDULE);
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
                    ScheduleExtraINFO objAux = gson.fromJson(jsons, ScheduleExtraINFO.class);  /* Obj to OBJ*/
                    busExtraTimeData.add(objAux);
                }
            } else {
                System.out.println(json.getString(TAG_MESSAGE) + "(BUS SCHEDULE)");
            }

            busExtraTimeData = bubbleSortE(busExtraTimeData);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private ArrayList<ScheduleExtraINFO> bubbleSortE(ArrayList<ScheduleExtraINFO> toSort){
        int i, bottom;
        ScheduleExtraINFO temp;
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

    public ArrayList<ScheduleExtraINFO> getBusTimeExtraData(){return busExtraTimeData;}

}
