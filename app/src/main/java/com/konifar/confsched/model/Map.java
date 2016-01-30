package com.konifar.confsched.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.konifar.confsched.R;

import java.util.ArrayList;
import java.util.List;

public class Map {

    @StringRes
    public int nameRes;

    @StringRes
    public int buildingNameRes;

    @DrawableRes
    public int markerRes;

    public double latitude;

    public double longitude;

    public Map(int nameRes, int buildingNameRes, int markerRes, double latitude, double longitude) {
        this.nameRes = nameRes;
        this.buildingNameRes = buildingNameRes;
        this.markerRes = markerRes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<Map> createList() {
        List<Map> list = new ArrayList<>();

        list.add(new Map(R.string.map_main_name, R.string.map_main_building,
                R.drawable.ic_place_red_500_36dp, 35.605899, 139.683541));
        list.add(new Map(R.string.map_ab_name, R.string.map_ab_building,
                R.drawable.ic_place_green_500_36dp, 35.603012, 139.684206));
        list.add(new Map(R.string.map_cd_name, R.string.map_cd_building,
                R.drawable.ic_place_blue_500_36dp, 35.603352, 139.684249));
        list.add(new Map(R.string.map_party_name, R.string.map_party_building,
                R.drawable.ic_place_purple_500_36dp, 35.607513, 139.684689));

        return list;
    }

}
