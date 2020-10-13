package com.example.khareedlo.Product.AddMobile.Select_Color;

public class ColorClass {


     public ColorClass(){}

    public String getColorID() {
        return colorID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public String colorID;
    public ColorClass(String id,String colorname)
    {
        colorID=id;
        this.colorname = colorname;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    String colorname;

}
