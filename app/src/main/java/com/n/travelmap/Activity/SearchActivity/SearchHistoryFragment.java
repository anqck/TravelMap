package com.n.travelmap.Activity.SearchActivity;

/**
 * Created by Khanh An on 12/20/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.n.travelmap.Database.SavedPlace;
import com.n.travelmap.Database.SearchHistoryDA;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryFragment extends Fragment{
    View view;
    ListView list;



    public SearchHistoryFragment() {
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
        view = inflater.inflate(R.layout.search_history_fragment, container, false);

        SearchHistoryDA searchHistoryDA = new SearchHistoryDA(view.getContext());

        final List<SavedPlace> placeList = searchHistoryDA.GetSearchHistory();

        SearchHistoryListviewAdapter adapter = new    SearchHistoryListviewAdapter(this.getActivity(),placeList);


        list    =   (ListView)view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
               // Toast.makeText(SearchHistoryFragment.this.getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();



                List< MarkerTagObject> result = new ArrayList<>();
                result.add(new MarkerTagObject(placeList.get(position).getPlaceID(),placeList.get(position).getLocation()));

                ((SearchActivity)getActivity()).ReturnResult(result);



            }
        });


        return view ;
    }

}