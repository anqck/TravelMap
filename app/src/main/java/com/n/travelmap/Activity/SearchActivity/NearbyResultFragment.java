package com.n.travelmap.Activity.SearchActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.n.travelmap.Library.PlaceAPI.Place;
import com.n.travelmap.Library.PlaceAPI.PlaceAPI;
import com.n.travelmap.R;

import java.util.List;

/**
 * Created by Khanh An on 01/02/18.
 */

public class NearbyResultFragment extends Fragment {
    View view;
    ListView list;


    List<SearchPlaceObject> resultList;


    public NearbyResultFragment() {
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
        view = inflater.inflate(R.layout.nearby_result_fragment, container, false);

        list = view.findViewById(R.id.list);
        return view ;
    }


    public void ShowResult(List<Place> places) {


        final PlaceListViewAdapter_Place adapter = new    PlaceListViewAdapter_Place(getActivity(),places);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //((SearchActivity)getActivity()).OnNearbyMenuItemClick();
            }
        });
    }
}
