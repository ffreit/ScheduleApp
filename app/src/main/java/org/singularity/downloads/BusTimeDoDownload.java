package org.singularity.downloads;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;

import org.singularity.schedulebus.ScheduleExtraINFO;
import org.singularity.schedulebus.ScheduleINFO;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by The Machine on 8/4/2015.
 */
public class BusTimeDoDownload extends ListActivity {

    private static BusTimeDoDownload instance;
    private ArrayList<ScheduleINFO> dataShedule;
    private ArrayList<ScheduleExtraINFO> dataScheduleExttra;

    public BusTimeDoDownload(Activity value){


        BusTimeData task = new BusTimeData(value);
        BusTimeExtraData task2 = new BusTimeExtraData(value);

        try {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            dataShedule = task.getBusTimeData();
            dataScheduleExttra = task2.getBusTimeExtraData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
    public static Context getContext() {
        return instance;
    }

    public ArrayList<ScheduleINFO> getSchedule(){

        return dataShedule;

    }

    public ArrayList<ScheduleExtraINFO> getScheduleExtra(){return dataScheduleExttra;}

}
