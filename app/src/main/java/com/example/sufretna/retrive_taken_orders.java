package com.example.sufretna;

public class retrive_taken_orders {
    private String geo;
    private String lat;
    private String lon;
    private String item_name;
    private String place_name;
    private String mimagetrl;
    private String stat;
    private String uid_order;
    private String price;

    public retrive_taken_orders() {
    }

    public retrive_taken_orders(String geo, String lat, String lon, String item_name, String place_name, String mimagetrl, String stat, String uid_order, String price) {
        this.geo = geo;
        this.lat = lat;
        this.lon = lon;
        this.item_name = item_name;
        this.place_name = place_name;
        this.mimagetrl = mimagetrl;
        this.stat = stat;
        this.uid_order = uid_order;
        this.price = price;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getMimagetrl() {
        return mimagetrl;
    }

    public void setMimagetrl(String mimagetrl) {
        this.mimagetrl = mimagetrl;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getUid_order() {
        return uid_order;
    }

    public void setUid_order(String uid_order) {
        this.uid_order = uid_order;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
