package com.n.travelmap.Library.PlaceAPI.AutoCompleteAPI;

import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public class SearchPlaceObject {
    private String Name;
    private String subTitle;
    private String PlaceID;
    private List<String> listTypes;

    SearchPlaceObject()
    {

    }

    public SearchPlaceObject(String name,String subtitle,List<String> listtype, String placeid)
    {
        this.Name = name;
        this.PlaceID = placeid;
        this.listTypes = listtype;
        this.subTitle = subtitle;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }


    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getListTypes() {
        return listTypes;
    }

    public void setListTypes(List<String> listTypes) {
        this.listTypes = listTypes;
    }
}