package com.n.travelmap.Activity.FavoritesActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.Places;
import com.n.travelmap.Activity.SearchActivity.NearbyResultFragment;
import com.n.travelmap.Activity.SearchActivity.PlaceListViewAdapter_Place;
import com.n.travelmap.Activity.SearchActivity.SearchAutoCompleteFragment;
import com.n.travelmap.Activity.SearchActivity.SearchFragment;
import com.n.travelmap.Activity.SearchActivity.SearchMainMenuFragment;
import com.n.travelmap.Database.FavoritesDA;
import com.n.travelmap.Database.FavoritesDTO;
import com.n.travelmap.MainActivity;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/03/18.
 */

public class FavoritesFragment extends Fragment {
    View view;

    ListView list;
    List<FavoritesDTO> favoritesDTOList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        view = inflater.inflate(R.layout.activity_favorites, container, false);

        list = view.findViewById(R.id.list);
        HideView();

        return view;
    }

    public void HideView()
    {
        view.setVisibility(View.INVISIBLE);
    }

    public void ShowView()
    {
        view.setVisibility(View.VISIBLE);

        UpdateListFavorites();
    }

    private void UpdateListFavorites() {
        final FavoritesDA favoritesDA = new FavoritesDA(getActivity());
        favoritesDTOList = favoritesDA.GetFavorites();


        final PlaceListViewAdapter_FavoritesDTO adapter = new    PlaceListViewAdapter_FavoritesDTO(getActivity(),favoritesDTOList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position_1, long id) {

                String[] listItemsFirstRow = {" Xóa"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Chon thao tác: ");
                dialog.setItems(listItemsFirstRow,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position)
                    {
                        favoritesDA.DeleteFavorites(new FavoritesDTO(favoritesDTOList.get(position_1).getLatLng(),favoritesDTOList.get(position).getPlaceID()));
                        UpdateListFavorites();
                    }

                });

                AlertDialog alert = dialog.create();
                alert.show();

                return  false;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                List<MarkerTagObject> result = new ArrayList<>();
                result.add(new MarkerTagObject(favoritesDTOList.get(position).getPlaceID(),favoritesDTOList.get(position).getLatLng()));

                ((MainActivity)getActivity()).getSearchFragment().ReturnResult(result);
            }
        });
    }

    public int GetVisible()
    {
        return view.getVisibility();
    }
}
