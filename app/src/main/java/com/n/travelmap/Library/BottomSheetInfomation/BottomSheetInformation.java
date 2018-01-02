package com.n.travelmap.Library.BottomSheetInfomation;

import android.graphics.Bitmap;

import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 12/19/17.
 */

public class BottomSheetInformation {
    private String placeID;
    private  String mainTitle;
    private  String subTitle;

    PlacePhotoMetadataBuffer placePhotoMetadataBuffer;
    List<Bitmap> listImage;

    private  String info_text1;
    private  String info_text2;
    private  String info_text3;

    public BottomSheetInformation()
    {
        mainTitle = "";
        subTitle = "";

        listImage = new ArrayList<>();
        info_text1 = "";
        info_text2 = "";
        info_text3 = "";

    }

    public BottomSheetInformation(String MainTitle, String SubTitle, Bitmap DefaultImg)
    {
        mainTitle = MainTitle;
        subTitle = SubTitle;

        listImage = new ArrayList<>();
        listImage.add(DefaultImg);
        info_text1 = "";
        info_text2 = "";
        info_text3 = "";

    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void addImage(Bitmap img)
    {
        this.listImage.add(img);
    }

    public void setPlacePhotoMetadataBuffer(PlacePhotoMetadataBuffer buffer)
    {
        this.placePhotoMetadataBuffer = buffer;
    }

    public PlacePhotoMetadataBuffer getPlacePhotoMetadataBuffer()
    {
       return  this.placePhotoMetadataBuffer ;
    }

    public List<Bitmap> getListImage()
    {
        return this.listImage;
    }



}
