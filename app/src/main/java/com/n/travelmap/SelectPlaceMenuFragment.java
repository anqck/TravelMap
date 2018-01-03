package com.n.travelmap;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.n.travelmap.Database.FavoritesDA;
import com.n.travelmap.Database.FavoritesDTO;

import java.util.ArrayList;

/**
 * Created by Khanh An on 12/22/17.
 */

public class SelectPlaceMenuFragment extends Fragment {
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    enum SelectPlaceMenuState{
        Normal,
        Remove,
        Extends
    }

    ListPopupWindow popup;
    MarkerTagObject markerTagObject;
    String mPlaceID;
    private LatLng latLng;

    View view;
    LinearLayout normalView,normalView_remove, removeView,extendView;
    Button btnMoveFrom,btnMoveTo,btnExtends,btnRemove,btnFavorites;
    BottomSheetBehavior behavior;

    FavoritesDA favoritesDA;

    public SelectPlaceMenuFragment() {
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
        view = inflater.inflate(R.layout.place_select_menu, container, false);

        view.setVisibility(View.INVISIBLE);
        HideMenu();

        favoritesDA = new FavoritesDA(getActivity());

        normalView = view.findViewById(R.id.menu_normal);
        removeView = view.findViewById(R.id.menu_remove);
        extendView = view.findViewById(R.id.menu_extends);
        normalView_remove = view.findViewById(R.id.menu_normal_remove);

        btnMoveFrom = view.findViewById(R.id.btn_move_from);
        btnMoveFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnSetPlaceMoveFrom(markerTagObject);
            }
        });

        view.findViewById(R.id.btn_move_from_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnSetPlaceMoveFrom(markerTagObject);
            }
        });


        btnMoveTo = view.findViewById(R.id.btn_move_to);
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnMoveToButton();
                };

        });
        view.findViewById(R.id.btn_move_to_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnMoveToButton();
            }
        });



        btnExtends = view.findViewById(R.id.btn_extend);
        btnExtends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Chia sẽ");
                list.add("Thêm điểm kết thúc");

                popup = new ListPopupWindow(getActivity());
                popup.setAdapter(new ArrayAdapter(getActivity(),R.layout.popuplist_item, list));
                popup.setAnchorView(btnExtends);

                popup.setWidth(600);
                popup.setHeight(400);

                popup.setModal(true);

                popup.show();
            }
        });


        btnRemove = view.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).DirectionRemovePlace(markerTagObject);
            }
        });


        btnFavorites = view.findViewById(R.id.btn_favorites);
        btnFavorites.setTag("PLACE_SELECT");
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnFavoritesButtonClick(btnFavorites.getTag().toString(),markerTagObject);
            }
        });

        view.findViewById(R.id.btn_favorites_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnFavoritesButtonClick("PLACE_SELECT_REMOVE".toString(),markerTagObject);
            }
        });
        return view ;
    }

    public void HideMenu()
    {
        view.animate().translationY(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                view.setVisibility(View.INVISIBLE);
                view.animate().translationY(50);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void ShowMenu(SelectPlaceMenuState state, String PlaceID)
    {

        if(state == SelectPlaceMenuState.Normal)
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.VISIBLE);
            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else if(state == SelectPlaceMenuState.Remove)
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.VISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.VISIBLE);
        }

        view.animate().translationY(0).setDuration(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void ShowMenu(SelectPlaceMenuState state, String PlaceID, LatLng latLng)
    {
        this.latLng = latLng;

        if(state == SelectPlaceMenuState.Normal)
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.VISIBLE);
            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else if(state == SelectPlaceMenuState.Remove)
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.VISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mPlaceID = PlaceID;

            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.VISIBLE);
        }

        view.animate().translationY(0).setDuration(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void ShowMenu(SelectPlaceMenuState state, MarkerTagObject PlaceID)
    {
        markerTagObject = PlaceID;

        if(state == SelectPlaceMenuState.Normal)
        {

            if(favoritesDA.ifExists(new FavoritesDTO(PlaceID.getLatLng(),PlaceID.getPlaceID())))
            {
                normalView_remove.setVisibility(View.VISIBLE);
                normalView.setVisibility(View.INVISIBLE);
            }
            else
            {
                normalView_remove.setVisibility(View.INVISIBLE);
                normalView.setVisibility(View.VISIBLE);
            }

            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else if(state == SelectPlaceMenuState.Remove)
        {

            normalView_remove.setVisibility(View.INVISIBLE);
            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.VISIBLE);
            extendView.setVisibility(View.INVISIBLE);
        }
        else
        {

            normalView_remove.setVisibility(View.INVISIBLE);
            normalView.setVisibility(View.INVISIBLE);
            removeView.setVisibility(View.INVISIBLE);
            extendView.setVisibility(View.VISIBLE);
        }

        view.animate().translationY(0).setDuration(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void OnMoveToButton()
    {
        ((MainActivity)getActivity()).OnSetPlaceMoveTo(markerTagObject);
    }

    public Button getBtnFavorites()
    {
        return btnFavorites;
    }

    public MarkerTagObject getMarkerTagObject()
    {
        return markerTagObject;
    }
}
