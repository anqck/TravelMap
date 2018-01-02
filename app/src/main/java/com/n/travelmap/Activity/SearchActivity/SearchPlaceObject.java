package com.n.travelmap.Activity.SearchActivity;

import java.util.ArrayList;
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

    public SearchPlaceObject(String name, String subtitle, List<String> listtype, String placeid)
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

    public List<String> GetTypesTrans()
    {
        List<String> res = new ArrayList<>();
        for(String str : listTypes)
        {
            switch (str)
            {
                case "country":
                    res.add("Quốc gia");
                    break;
                case "political":
                    res.add("Chính trị");
                    break;
                case "locality":
                    res.add("Vùng");
                    break;
                case "route":
                    res.add("Đường");
                    break;
                case "establishment":
                    res.add("Tổ chức");
                    break;
                case "sublocality_level_1":
                case "sublocality_level_2":
                case "sublocality_level_3":
                case "sublocality_level_4":
                case "sublocality_level_5":
                    break;
                case "sublocality":
                    res.add("Sublocality");
                    break;
                case "geocode":
                    break;
                case "administrative_area_level_1":
                case "administrative_area_level_2":
                case "administrative_area_level_3":
                case "administrative_area_level_4":
                case "administrative_area_level_5":
                    res.add("Khu vực hành chính");
                    break;
                default:
                    res.add(str);
                    break;
            }
        }
        return res;
    }
}