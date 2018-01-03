package com.n.travelmap.Library.BottomSheetInfomation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.n.travelmap.Database.FavoritesDA;
import com.n.travelmap.Database.FavoritesDTO;
import com.n.travelmap.Library.BottomSheetInfomation.BottomSheetInformation;
import com.n.travelmap.Library.BottomSheetInfomation.ItemPagerAdapter;
import com.n.travelmap.MainActivity;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;
import com.n.travelmap.SelectPlaceMenuFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Khanh An on 12/18/17.
 */


public class BottomInformationSheetController {

    ViewPager viewPager;
    ItemPagerAdapter adapter;

    private TextView bottomSheetTitle;
    private TextView bottomSheetSubTitle;

    private ImageButton button1;
    private TextView button1text;

    private ImageButton button2;
    private TextView button2text;

    private ImageButton button3;
    private TextView button3text;

    private Button info_button1;
    private Button info_button2;
    private Button info_button3;

    MainActivity mainActivity;
    public BottomInformationSheetController(final View bottomSheet, ViewPager adapter, Context context)
    {
        this.viewPager = adapter;

        mainActivity = (MainActivity)context;

        bottomSheetTitle = (TextView) bottomSheet.findViewById(R.id.bottom_sheet_title);
        bottomSheetSubTitle = (TextView) bottomSheet.findViewById(R.id.text_dummy1);

        button1 = (ImageButton) bottomSheet.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.OnFavoritesButtonClick(button1.getTag().toString(),mainActivity.getSelectPlaceMenuFragment().getMarkerTagObject());

            }
        });



        button2 = (ImageButton) bottomSheet.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = null;
                try {
                    query = URLEncoder.encode(bottomSheetTitle.getText().toString(), "utf-8");
                    String url = "http://www.google.com/search?q=" + query;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(bottomSheet.getContext(), intent, Bundle.EMPTY);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        button3 = (ImageButton) bottomSheet.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = bottomSheetTitle.getText().toString().toUpperCase() + "\n Địa chỉ: " + bottomSheetSubTitle.getText().toString() + "\n SĐT: " + info_button2.getText().toString() ;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "-- Travel Map - Share Location --");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(bottomSheet.getContext(), sharingIntent, Bundle.EMPTY);
            }
        });


        button1text = (TextView) bottomSheet.findViewById(R.id.button1text);
        button2text = (TextView) bottomSheet.findViewById(R.id.button2text);
        button3text = (TextView) bottomSheet.findViewById(R.id.button3text);

        info_button1 = (Button) bottomSheet.findViewById(R.id.info_text1);

        info_button2 = (Button) bottomSheet.findViewById(R.id.info_text2);
        info_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info_button2.getText() != bottomSheet.getResources().getString(R.string.NoInformation)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info_button2.getText()));
                    startActivity(bottomSheet.getContext(), intent, Bundle.EMPTY);
                }
            }
        });


        info_button3 = (Button) bottomSheet.findViewById(R.id.info_text3);
        info_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info_button3.getText() != bottomSheet.getResources().getString(R.string.NoInformation)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info_button3.getText().toString()));
                    startActivity(bottomSheet.getContext(),intent, Bundle.EMPTY);
                }
            }
        });

    }
    BottomInformationSheetController(View v)
    {

    }

    public void UpdateImageAdapter(BottomSheetInformation bottomSheetInformation)
    {
        adapter = new ItemPagerAdapter(bottomSheetTitle.getContext(),bottomSheetInformation.getListImage() );
        viewPager.setAdapter(adapter);
    }
    public void SetBottomSheetTitle(String value)
    {
        bottomSheetTitle.setText(value);


    }

    public void SetBottomSheetSubTitle(String value)
    {
        bottomSheetSubTitle.setText(value);
    }

    public void SetInfoText1(String value)
    {
        info_button1.setText(value);

        UpdateFavoriteButton();
    }


    public void SetInfoText2(String value)
    {
        info_button2.setText(value);
    }

    public void SetInfoText3(String value)
    {
        info_button3.setText(value);
    }


    public void UpdateFavoriteButton()
    {
        if(FavoritesDA.ifExists(new FavoritesDTO(mainActivity.getSelectPlaceMenuFragment().getMarkerTagObject().getLatLng(),mainActivity.getSelectPlaceMenuFragment().getMarkerTagObject().getPlaceID())))
        {
            button1.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_favourite_scaled));
            button1.setTag("PLACE_SELECT_REMOVE");
        }
        else
        {
            button1.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_add_favorite_scaled));
            button1.setTag("PLACE_SELECT");
        }
    }
}
