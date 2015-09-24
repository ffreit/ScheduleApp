package org.singularity.util;

import android.content.Context;

import org.felipebertao.singularity.regentsonuniversity.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by The Machine on 8/4/2015.
 */
public class ReadFile {

  public ReadFile(Context ctx){

      String FILENAME = "schedule_time.txt";
      String string = "hello World";

      try {
          FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
          fos.write(string.getBytes());


          fos.close();


          BufferedReader br = new BufferedReader( new FileReader(ctx.getFilesDir() + "/" + "schedule_time.txt"));
          try {
              StringBuilder sb = new StringBuilder();
              String line = br.readLine();

              while (line != null) {
                  sb.append(line);
                  sb.append("/");
                  line = br.readLine();
              }
              String everything = sb.toString();
              System.out.println(everything);
          } finally {
              br.close();
          }


      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }

  }



}
