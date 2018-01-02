package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n.travelmap.R;

/**
 * Created by Khanh An on 12/20/17.
 */

public class MyListView extends ArrayAdapter<String> {

    private final Activity context;


    private final String[] web;
    private final Integer[] imageId;
    public MyListView(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.image_listview, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.image_listview, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}