package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.n.travelmap.R;

import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public class PlaceListViewAdapter extends ArrayAdapter<SearchPlaceObject> {

    private final Activity context;

    List<SearchPlaceObject> placeObjectList;


    public PlaceListViewAdapter(Activity context, List<SearchPlaceObject> list ) {
            super(context, R.layout.place_listview_item, list);


            this.context = context;

            placeObjectList = list;

            }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.place_listview_item, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.mainTitle_text);
            TextView txtSubTitle = (TextView) rowView.findViewById(R.id.subTitle_text);

            TextView txtTypes = (TextView) rowView.findViewById(R.id.type_text);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

            txtTitle.setText(placeObjectList.get(position).getName());
            txtSubTitle.setText(placeObjectList.get(position).getSubTitle());

            txtTypes.setText(placeObjectList.get(position).GetTypesTrans().toString());
            //txtTitle.setText(placeObjectList.get(position).);

            imageView.setImageResource(R.drawable.common_google_signin_btn_icon_light);

            return rowView;
            }
};
