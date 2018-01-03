package com.n.travelmap.Activity.SearchActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.n.travelmap.Library.PlaceAPI.PlaceAPI;
import com.n.travelmap.R;

import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public class SearchAutoCompleteFragment extends Fragment {
    View view;
    ListView list;


    List<SearchPlaceObject> resultList;

    PlaceAPI mPlaceAPI ;

    public SearchAutoCompleteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mPlaceAPI = new PlaceAPI(this.getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_autocomplete_fragment, container, false);

//        MyListView adapter = new    MyListView(this.getActivity(), web, imageId);
        list    =   (ListView)view.findViewById(R.id.list);
//        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(SearchAutoCompleteFragment.this.getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SearchFragment) getParentFragment()).onSearchItemClick(resultList.get(position));
            }
        });

        return view ;
    }

    public void UpdateOnTextChange(String str, String filter)
    {


        resultList = mPlaceAPI.autocomplete(str);

        PlaceListViewAdapter adapter = new    PlaceListViewAdapter(this.getActivity(), resultList);
        list.setAdapter(adapter);
    }



}