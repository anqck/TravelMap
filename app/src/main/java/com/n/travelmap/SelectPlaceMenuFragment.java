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
    String mPlaceID;
    private LatLng latLng;

    View view;
    LinearLayout normalView, removeView,extendView;
    Button btnMoveFrom,btnMoveTo,btnExtends,btnRemove;
    BottomSheetBehavior behavior;

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


        normalView = view.findViewById(R.id.menu_normal);
        removeView = view.findViewById(R.id.menu_remove);
        extendView = view.findViewById(R.id.menu_extends);


        btnMoveFrom = view.findViewById(R.id.btn_move_from);
        btnMoveFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnSetPlaceMoveFrom(mPlaceID);
            }
        });

        btnMoveTo = view.findViewById(R.id.btn_move_to);
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OnSetPlaceMoveTo(mPlaceID);

                };

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
                ((MainActivity)getActivity()).DirectionRemovePlace(mPlaceID,latLng);
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
}
