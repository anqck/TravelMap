package com.n.travelmap.Activity.SearchActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.n.travelmap.Library.PlaceAPI.PlaceAPI;
import com.n.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/02/18.
 */

public class NearbyMenuFragment extends Fragment {
    View view;
    ListView list;

    List<NearbySearchMenuListViewAdapter.SimpleImageStringWrapper>  listObject;

    PlaceAPI mPlaceAPI ;


    public NearbyMenuFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_autocomplete_fragment, container, false);

        listObject = new ArrayList<>();
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Ẩm thực",getResources().getDrawable(R.drawable.ic_food)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Trạm dừng xe buýt",getResources().getDrawable(R.drawable.ic_bus)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Chỗ ở",getResources().getDrawable(R.drawable.ic_hotel)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Mua sắm",getResources().getDrawable(R.drawable.ic_shopping_mall)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Cafe",getResources().getDrawable(R.drawable.ic_cafe)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Gym",getResources().getDrawable(R.drawable.ic_gym)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("ATM",getResources().getDrawable(R.drawable.ic_atm)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Rạp phim",getResources().getDrawable(R.drawable.ic_movie_theater)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Công viên",getResources().getDrawable(R.drawable.ic_park)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Spa",getResources().getDrawable(R.drawable.ic_spa)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Sở thú",getResources().getDrawable(R.drawable.ic_zoo)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Casino",getResources().getDrawable(R.drawable.ic_casino)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Cảnh sát",getResources().getDrawable(R.drawable.ic_police_station)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Bệnh viện",getResources().getDrawable(R.drawable.ic_hospital)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Sân bay",getResources().getDrawable(R.drawable.ic_airport)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Tiệm bánh",getResources().getDrawable(R.drawable.ic_bakery)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Thuê xe",getResources().getDrawable(R.drawable.ic_car_rental)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Nhà thuốc",getResources().getDrawable(R.drawable.ic_pharmacy)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Đại sứ quán",getResources().getDrawable(R.drawable.ic_embassy)));


        NearbySearchMenuListViewAdapter adapter = new    NearbySearchMenuListViewAdapter(getActivity(),listObject);

        list    =   (ListView)view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ((SearchActivity)getActivity()).OnNearbyMenuItemClick(ParserFilter(listObject.get(position).getStr()));
            }
        });



        return view ;
    }


    private String ParserFilter(String str)
    {
        switch (str)
        {
            case "Tất cả":
                return "";
            case "Ẩm thực":
                return "restaurant";
            case "Khách sạn":
                return "";
            case "Mua sắm":
                return "shopping_mall";
            case"Trạm dừng xe buýt":
                return "bus_station";
            case "Cafe":
                return "cafe";
            case "Gym":
                return "gym";
            case "ATM":
                return "atm";
            case "Sân bay":
                return "airport";
            case "Bệnh viện":
                return "hospital";
            case "Rạp phim":
                return "movie_theater";
            case "Công viên":
                return "park";
            case "Spa":
                return "spa";
            case "Sở thú":
                return "zoo";
            case "Casino":
                return "casino";
            case "Tiệm bánh":
                return "bakery";
            case "Thuê xe":
                return "car_rental";
            case "Nhà thuốc":
                return "pharmacy";
            case "Đại sứ quán":
                return "embassy";
            case "Chỗ ở":
                return "lodging";
            case "Cảnh sát":
                return "police";

            default:
                return "";
        }
    }

}
