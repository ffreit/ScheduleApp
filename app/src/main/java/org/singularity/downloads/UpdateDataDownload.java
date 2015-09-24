package org.singularity.downloads;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.singularity.releasesData.DataUpdateINFO;
import org.singularity.schedulebus.ScheduleINFO;
import org.singularity.util.JSONParser;

import java.util.ArrayList;

/**
 * Created by The Machine on 8/5/2015.
 */
public class UpdateDataDownload extends AsyncTask <String, Void, String> {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_MESSAGE = "message";

    private static final String URL = "http://brasileiro.net.br/downloadDataUpdate.php";

    String value;
    String TAG = "Print: ";
    private ArrayList<DataUpdateINFO> dataUpdate;
    private JSONArray mData = null;
    private ListActivity activity;
    private ProgressDialog dialog;
    private Context mContext;

    public UpdateDataDownload (Context ctxValue){
       Log.i(TAG, "Entrou UpdateData");
         mContext = ctxValue;

    }

    @Override
    protected final String doInBackground(String... params) {

        Log.i(TAG, "Dentro do doINBackground UpdateDataDownload ");
        dataUpdateDownload();
        return value;
    }


    protected Void onPosExecute(String value){
        super.onPostExecute(value);

        return null;
    }

    public void dataUpdateDownload() {


        Gson gson = new Gson();
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content.
        dataUpdate = new ArrayList<DataUpdateINFO>();

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

                    Log.i("Print: ", jsons);
                    DataUpdateINFO objAux = gson.fromJson(jsons, DataUpdateINFO.class);  /* Obj to OBJ*/
                    dataUpdate.add(objAux);

                                                  }
            } else {
                Log.i("Print: ",  json.getString(TAG_MESSAGE) + "(BUS SCHEDULE)");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //getBusTimeData();


    }
    public String getBusTimeData(){
        value =  dataUpdate.get(0).getGeneric("index");
        value = value + " " + dataUpdate.get(0).getGeneric("date") + " " +
                dataUpdate.get(0).getGeneric("time");

        Log.i("Print: ", "Last Line DB: " + value);
        return value;


    }
}
