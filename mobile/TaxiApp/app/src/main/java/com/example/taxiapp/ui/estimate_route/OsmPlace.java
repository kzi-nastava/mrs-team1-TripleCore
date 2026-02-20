package com.example.taxiapp.ui.estimate_route;

import com.google.gson.annotations.SerializedName;

public class OsmPlace {
    public String display_name;
    public String lat;
    public String lon;

    public String getDisplayName() { return display_name; }
    public String getLat() { return lat; }
    public String getLon() { return lon; }

    @Override
    public String toString() {
        return display_name;
    }

    public void setDisplayName(String display_name) { this.display_name = display_name; }
    public void setLat(String lat) { this.lat = lat; }
    public void setLon(String lon) { this.lon = lon; }

}
