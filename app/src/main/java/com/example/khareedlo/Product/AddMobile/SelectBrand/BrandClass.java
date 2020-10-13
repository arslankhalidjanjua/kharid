package com.example.khareedlo.Product.AddMobile.SelectBrand;

import android.graphics.drawable.Drawable;

public class BrandClass {

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String ID;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String Image;


    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }


    public BrandClass(String ID,String brand,String image) {
        this.ID=ID;
        Brand = brand;
Image=image;
    }

    String Brand;

}
