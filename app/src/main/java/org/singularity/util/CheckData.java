package org.singularity.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.singularity.downloads.BusTimeDoDownload;
import org.singularity.downloads.UpdateDataDownload;
import org.singularity.schedulebus.ScheduleINFO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Created by The Machine on 8/5/2015.
 */
public class CheckData extends AsyncTask<String, Void, Void> {

    Activity contex;
    Context contex2;
    private ProgressDialog dialog;
    private String TUG = "Print ";
    String toastMsg = "Nothing";
    ArrayList<ScheduleINFO> dataSchedule;

    public CheckData(Activity ctx, Context ctx2) throws IOException {

        Log.i(TUG, "Check Data inside");
        contex = ctx;
        contex2 = ctx2;
    }


    @Override
    protected Void doInBackground(String... params) {
        Log.i(TUG, "Task inside");

            if (haveNetwork()) {
                try {
                    if (hasUpdate()) {  /*check update*/

                       toastMsg = "An update is available. Go to Settings";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TUG, "ALGUM ERRRO");
                }
            } else {
                toastMsg = "No internet";
            }
        return null;
    }


    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
           // this.dialog.dismiss();
            //contex.finish();
        if(!(toastMsg.compareTo("Nothing") == 0)) {
            Toast.makeText(contex2, toastMsg, Toast.LENGTH_LONG).show();
         }
        }

        //we will develop this method in version


    private void doDownload() {

        BusTimeDoDownload btd = new BusTimeDoDownload(contex);
       dataSchedule = btd.getSchedule();
        try {
            makeTheFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void makeTheFile() throws IOException {
        String MY_FILE_NAME = "schedule_time.txt";
        int myBufferSize = 2048;
        // Get the directory for the user's public pictures directory.

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), MY_FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        } else{
            file.delete();
            file.createNewFile();
        }

        PrintWriter out = new PrintWriter(file); /* to writte on file */


        for (int i = 0; i < dataSchedule.size(); i++) {
            out.println(dataSchedule.get(i));
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

            for ( int i=0; i< RowData.length; i++){
                testDB.add(RowData[i]);
            }
        }

        br.close();
        int i = 0;
    }
    private boolean hasUpdate() throws IOException {


        UpdateDataDownload udd = new UpdateDataDownload(contex);


        File file = new File(contex.getFilesDir() + "/" + "data_update.txt");
        Boolean valueReturn = false;

        if (!file.exists()) {
            String content = "0 0000-00-00 00:00:00";
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            Log.i(TUG, "make File Empty Done");

        } else {
            Log.i (TUG, "Already Has Update File");
        }

        String lastLine = tail(file);
        Log.i(TUG, "Last Line from File: " + lastLine);

        String lastLineFromDB = null;

        try {
            udd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        lastLineFromDB = udd.getBusTimeData();

        if (lastLineFromDB.compareTo(lastLine) == 0){
            valueReturn = false;
        } else {
            valueReturn = true;
        }

        Log.i(TUG, "Has Update?: " + valueReturn);
        return valueReturn;


    }


    private Boolean checkIfHasDataSchedule(Activity ctx) {

        File file = new File(contex.getFilesDir(), "schedule_time.txt");
        Boolean value;
        // if file doesnt exists, then create it
        if (file.exists()) {
            value = true;
        } else {
            value = false;
        }
        Log.i(TUG, "checkIfHasDataSchedule: " + value);
        return value;


    }


    /***
     * util methods
     */
    private String tail(File file) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile(file, "r");
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (filePointer == fileLength) {
                        continue;
                    }
                    break;

                } else if (readByte == 0xD) {
                    if (filePointer == fileLength - 1) {
                        continue;
                    }
                    break;
                }

                sb.append((char) readByte);
            }

            String lastLine = sb.reverse().toString();

            return lastLine;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileHandler != null)
                try {
                    fileHandler.close();
                } catch (IOException e) {
                /* ignore */
                }
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) contex2.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        Log.i (TUG, "Internet Network (Wifi or Mobile): " + ( haveConnectedWifi || haveConnectedMobile) );
        return haveConnectedWifi || haveConnectedMobile;
    }

    private boolean haveNetwork(){
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if(reachable){
                System.out.println("Internet access");
                return reachable;
            }
            else{
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

}