package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

public class NearbySearchMenuListViewAdapter extends ArrayAdapter<NearbySearchMenuListViewAdapter.SimpleImageStringWrapper> {

    private List<SimpleImageStringWrapper> listObject;

    private final Activity context;



    public NearbySearchMenuListViewAdapter(Activity context, List<SimpleImageStringWrapper> listObject) {
        super(context, R.layout.place_listview_item, listObject);
        this.context = context;

        this.listObject = listObject;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View view1= inflater.inflate(R.layout.simple_listview_item, null, true);

        TextView txtTitle = (TextView) view1.findViewById(R.id.mainTitle_text);

        ImageView imageView = (ImageView) view1.findViewById(R.id.img);

        txtTitle.setText(listObject.get(position).getStr());


        if(listObject.get(position).getDrawable() != null)
        {
            imageView.setImageDrawable(listObject.get(position).getDrawable() );
        }
        else
            imageView.setImageResource(R.drawable.common_google_signin_btn_icon_light);



        return view1;
    }

     public static class SimpleImageStringWrapper
    {
        private String str;
        private Drawable drawable;

        SimpleImageStringWrapper(String str, Drawable drawable)
        {
            this.str = str;
            this.drawable = drawable;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}