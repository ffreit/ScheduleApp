package org.singularity.core;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.felipebertao.singularity.regentsonuniversity.R;

import java.util.ArrayList;

public class AdapterStops extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final ArrayList<String> valuesTwo;

    public   View rowView;

    public AdapterStops(Context context, String[] values, ArrayList<String> valuesTwo) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.valuesTwo = valuesTwo;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.rowlayout, parent, false);


        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(valuesTwo.get(position));
       // textView_2.setText(" Next: " + valuesTwo.get(position) + " | After Next: " + valuesThree.get(position));

        // change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith(" Depart RU Office")) {
            imageView.setImageResource(R.drawable.home);
        } else if(s.startsWith(" Light Rail (drop off only)")) {

            imageView.setImageResource(R.drawable.light);

        }else if(s.startsWith(" SDFC (ASU gym)")) {
            imageView.setImageResource(R.drawable.gym);
        } else if (s.startsWith(" Garage (MU)")) {
            imageView.setImageResource(R.drawable.garage);
        } else if (s.startsWith(" Univ.&College")){
            imageView.setImageResource(R.drawable.univcollege);
        } else if (s.startsWith((" Univ.Bridge"))){
            imageView.setImageResource(R.drawable.univbridge);
        }

        return rowView;
    }

  }

