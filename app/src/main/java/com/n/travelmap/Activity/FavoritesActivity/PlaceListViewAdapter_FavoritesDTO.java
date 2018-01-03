package com.n.travelmap.Activity.FavoritesActivity;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.n.travelmap.Database.FavoritesDTO;
import com.n.travelmap.Library.PlaceAPI.Place;
import com.n.travelmap.MainActivity;
import com.n.travelmap.R;

import java.util.List;

/**
 * Created by Khanh An on 01/03/18.
 */

public class PlaceListViewAdapter_FavoritesDTO extends ArrayAdapter<FavoritesDTO> {

    private MainActivity context;

    List<FavoritesDTO> placeObjectList;


    public PlaceListViewAdapter_FavoritesDTO(MainActivity context, List<FavoritesDTO> list ) {
        super(context, R.layout.favourite_listview_item, list);

        this.context = context;

        placeObjectList = list;

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.favourite_listview_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.mainTitle_text);
        TextView txtSubTitle = (TextView) rowView.findViewById(R.id.subTitle_text);

        TextView txtTypes = (TextView) rowView.findViewById(R.id.type_text);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle.setText(placeObjectList.get(position).getTitle());
        txtSubTitle.setText(placeObjectList.get(position).getSubTitle());


        ImageButton removeFavourite = rowView.findViewById(R.id.remove_favorite);
        removeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getFavoritesFragment().RemoveFavourite(new FavoritesDTO(placeObjectList.get(position).getLatLng(),placeObjectList.get(position).getPlaceID()));

            }
        });


        if(placeObjectList.get(position).getBitmap() != null)
        {
            imageView.setImageBitmap(placeObjectList.get(position).getBitmap() );
        }
        else
            imageView.setImageResource(R.drawable.no_image);



        return rowView;
    }


}
