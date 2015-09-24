package org.singularity.util;

import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by The Machine on 8/18/2015.
 */
public class ArrayOfArrayList {
    ArrayList<String> group[]; //Put the length of the array you need
    int size;


    public ArrayOfArrayList(int size){
        this.size = size;
        group = new ArrayList[size];

        int x = 0 ;
        while ( x < group.length){
            group[x] = new ArrayList<>();
            x++;
        }
    }


    public void addValue(int index, String value){

        group[index].add(value);

    }

    public String getValue(int index, int indexArray){
        return group[index].get(indexArray);
    }

    public ArrayList<String> get(int index){
        return group[index];

    }
    public int size(){

        return size;

    }


    public void print (){

        for (int i = 0; i < size; i++) {

            for (int j = 0 ; j < group[i].size(); j ++) {
                Log.i("ArraYOfArrayList Print " + i, group[i].get(j));
            }
        }

    }

    public int sizeArray(int i) {

        return group[i].size();
    }
}
