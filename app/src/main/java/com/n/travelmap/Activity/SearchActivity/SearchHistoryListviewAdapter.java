package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n.travelmap.Database.SearchHistoryDTO;
import com.n.travelmap.R;

import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public class SearchHistoryListviewAdapter extends ArrayAdapter<SearchHistoryDTO> {

    private final Activity context;

    private  List<SearchHistoryDTO> listHistory;


    public SearchHistoryListviewAdapter(Activity context, List<SearchHistoryDTO> savedPlaces) {
        super(context, R.layout.place_listview_item, savedPlaces);
        this.context = context;
        listHistory = savedPlaces;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View view1= inflater.inflate(R.layout.place_listview_item, null, true);
        TextView txtTitle = (TextView) view1.findViewById(R.id.mainTitle_text);
        TextView txtSubTitle = (TextView) view1.findViewById(R.id.subTitle_text);
        TextView txtTypes = (TextView) view1.findViewById(R.id.type_text);
        ImageView imageView = (ImageView) view1.findViewById(R.id.img);

        txtTitle.setText(listHistory.get(position).getName());
        txtSubTitle.setText(listHistory.get(position).getAddress());
        txtTypes.setText(listHistory.get(position).getTypes().toString());

        if(listHistory.get(position).getBitmap() != null)
        {
            imageView.setImageBitmap(listHistory.get(position).getBitmap());
        }
        else
            imageView.setImageResource(R.drawable.common_google_signin_btn_icon_light);
        return view1;
    }
}