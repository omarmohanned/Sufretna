package com.example.sufretna;

public class upload_image {
    private String mname;
    private String mimagetrl;

    public upload_image(String item_name_public) {
        ///empty constructer needed
    }

    public upload_image(String name, String imageurl) {
        if (name.trim().equals("")){
            name="no name";
        }

        mname=name;
        mimagetrl=imageurl;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMimagetrl() {
        return mimagetrl;
    }

    public void setMimagetrl(String mimagetrl) {
        this.mimagetrl = mimagetrl;
    }
}
