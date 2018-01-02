package com.n.travelmap;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Khanh An on 12/22/17.
 */

public class DirectionMenuFragment extends Fragment {




    enum DirectionMenuState{
        NeedStartPos,
        NeedFinishPos,
        Result
    }

    DirectionMenuState directionMenuState;

    View view;
    LinearLayout resultView, needInfoView;
    TextView textViewDistance, textViewTime;
    Button btnAddPlace;


    public DirectionMenuFragment() {
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
        view = inflater.inflate(R.layout.direction_menu_fragment, container, false);

        resultView = (LinearLayout) view.findViewById(R.id.menu_normal);
        needInfoView = (LinearLayout) view.findViewById(R.id.view_need_pos);

        view.setVisibility(View.INVISIBLE);
        HideMenu();

        textViewDistance =  view.findViewById(R.id.text_distance);
        textViewTime =  view.findViewById(R.id.text_time);

        btnAddPlace =  view.findViewById(R.id.btn_add_place);
        return view ;
    }

    //public void ShowResult
    public void ShowMenu(DirectionMenuState state)
    {
        switch (state)
        {
            case NeedStartPos:
                btnAddPlace.setText("Bổ sung địa điểm bắt đầu để lập kế hoạch lộ trình");

                resultView.setVisibility(View.INVISIBLE);
                needInfoView.setVisibility(View.VISIBLE);


                break;
            case NeedFinishPos:
                btnAddPlace.setText("Bổ sung địa điểm kết thúc để lập kế hoạch lộ trình");

                resultView.setVisibility(View.INVISIBLE);
                needInfoView.setVisibility(View.VISIBLE);
                break;
            case Result:
                resultView.setVisibility(View.VISIBLE);
                needInfoView.setVisibility(View.INVISIBLE);
                break;
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

    public void ShowMenu(DirectionMenuState state, String distance, String period)
    {

        textViewDistance.setText(distance);
        textViewTime.setText(period.replaceAll("hour","giờ").replaceAll("mins","phút"));
        ShowMenu(state);

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

}
