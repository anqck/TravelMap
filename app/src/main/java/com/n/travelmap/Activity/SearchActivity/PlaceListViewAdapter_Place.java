package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n.travelmap.Library.PlaceAPI.Place;
import com.n.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/02/18.
 */

public class PlaceListViewAdapter_Place  extends ArrayAdapter<Place> {

    private final Activity context;

    List<Place> placeObjectList;


    public PlaceListViewAdapter_Place(Activity context, List<Place> list ) {
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
        txtSubTitle.setText(placeObjectList.get(position).getVicinity());

        List<String> types = new ArrayList<>();
        for(int i = 0; i < placeObjectList.get(position).getTypes().length; i++)
        {
            if(ParseType(placeObjectList.get(position).getTypes()[i]).compareTo("") != 0)
                types.add(ParseType(placeObjectList.get(position).getTypes()[i]));
        }

        txtTypes.setText(types.toString());
        //txtTitle.setText(placeObjectList.get(position).);

        imageView.setImageResource(R.drawable.common_google_signin_btn_icon_light);

        return rowView;
    }

    private String ParseType(String str)
    {


        switch (str)
        {
            case "school":
                return "Trường học";
            case "establishment":
                //return "Tổ chức";
                return"";
            case "point_of_interest":
                return "";
            case "restaurant":
                return "Nhà hàng";
            case "food":
                return "Thức ăn";
            case "gym":
                return "Gym";
            case "health":
                return "Sức khỏe";
            case "bus_station":
                return "Trạm dừng xe buýt";
            case "transit_station":
                return "Trạm giao thông";
            case "cafe":
                return "Cà phê";
            case "finance":
                return "Tài chính";
            case "store":
                return "Cửa hàng";
            case "atm":
                return "ATM";
            case "airport":
                return "Sân bay";
            case "hospital":
                return "Bệnh viện";
            case "movie_theater":
                return "Rạp phim";
            case "park":
                return "Công viên";
            case "spa":
                return "Spa";
            case "zoo":
                return "Sở thú";
            case "casino":
                return "Sòng bạc";
            case "bakery":
                return "Tiệm bánh";
            case "car_rental":
                return "Thuê xe";
            case "pharmacy":
                return "Nhà thuốc";
            case "embassy":
                return "Đại sứ quán";
            case "shopping_mall":
                return "Mua sắm";
            case "lodging":
                return "Chỗ ở";
            case "premise":
                return "";
            case "parking":
                return "Chỗ đậu xe";
            case "police":
                return "Cảnh sát";
            default:
                return str;
        }
    }
}
