package com.n.travelmap.Activity.SearchActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.n.travelmap.Library.PlaceAPI.Place;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/02/18.
 */

public class NearbyResultFragment extends Fragment {
    View view;
    ListView list;


    private  List<Place> places;


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


    public void ShowResult(final List<Place> places) {

        this.places = places;

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
                ((SearchFragment)getParentFragment()).OnNearbyResultItemClick(places.get(position));
            }
        });
    }

    public List<MarkerTagObject> GetResult()
    {
        List<MarkerTagObject> res = new ArrayList<>();
        for(Place p : places)
        {
            res.add(new MarkerTagObject(p.getPlaceId(),new LatLng(p.getLatitude(),p.getLongitude())));
        }

        return res;
    }
}
