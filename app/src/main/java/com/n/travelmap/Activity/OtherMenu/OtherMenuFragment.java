package com.n.travelmap.Activity.OtherMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.n.travelmap.Activity.ARDirection.ARDirection;
import com.n.travelmap.Activity.FavoritesActivity.PlaceListViewAdapter_FavoritesDTO;
import com.n.travelmap.Activity.InformationActivity.InformationActivity;
import com.n.travelmap.Activity.SearchActivity.NearbySearchMenuListViewAdapter;
import com.n.travelmap.Database.FavoritesDA;
import com.n.travelmap.Database.FavoritesDTO;
import com.n.travelmap.MainActivity;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/04/18.
 */

public class OtherMenuFragment extends Fragment {
    View view;

    ListView list;

    List<NearbySearchMenuListViewAdapter.SimpleImageStringWrapper>  listObject;

    FavoritesDA favoritesDA;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        view = inflater.inflate(R.layout.activity_other, container, false);

        listObject = new ArrayList<>();
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("Thông tin",getResources().getDrawable(R.drawable.ic_information)));
        listObject.add(new NearbySearchMenuListViewAdapter.SimpleImageStringWrapper("AR Direction",getResources().getDrawable(R.drawable.ic_virtual_reality)));

        NearbySearchMenuListViewAdapter adapter = new    NearbySearchMenuListViewAdapter(getActivity(),listObject);
        list    =   (ListView)view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in;
                switch (listObject.get(position).getStr())
               {
                   case "Thông tin":
                        in =new Intent(getActivity(),InformationActivity.class);

                       getActivity().startActivity(in);
                       //getActivity().overridePendingTransition(0, 0);
                       break;
                   case "AR Direction":

                        in =new Intent(getActivity(),ARDirection.class);


                       getActivity().startActivityForResult(in,0);
                       getActivity().overridePendingTransition(0, 0);
                       break;
               }
            }
        });

        HideView();
//        favoritesDA = new FavoritesDA(getActivity());
//
//        list = view.findViewById(R.id.list);
//        HideView();



        return view;
    }

    public void HideView()
    {
        view.setVisibility(View.INVISIBLE);
    }

    public void ShowView()
    {
        view.setVisibility(View.VISIBLE);


    }



    public int GetVisible()
    {
        return view.getVisibility();
    }
}