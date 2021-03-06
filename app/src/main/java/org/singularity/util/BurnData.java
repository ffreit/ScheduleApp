package org.singularity.util;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by The Machine on 8/4/2015.
 */
public class BurnData extends Activity {

    //a handle to the application's resources
    private Resources resources;
    //a string to output the contents of the files to LogCat
    private String output;



    public  BurnData (Resources res) {

        resources =res;

        System.out.println("TESTANDO SE ENTROU");

        try {
            //Load the file from the raw folder - don't forget to OMIT the extension
            output = LoadFile("schedule_time", true);

           // System.out.println("TEST  " + output);
            //output to LogCat
            Log.i("test", output);
        } catch (IOException e)  {
            //display an error toast message
            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    //load file from apps res/raw folder or Assets folder
    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException {
        //Create a InputStream to read the file into
        InputStream iS;

        if (loadFromRawFolder){
            //get the resource id from the file name
            int rID = resources.getIdentifier("org.singularity:raw/"+fileName, null, null);
            //get the file as a stream
                iS = resources.openRawResource(rID);
        }
        else
        {
            //get the file as a stream
            iS = resources.getAssets().open(fileName);
        }

        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }
}
