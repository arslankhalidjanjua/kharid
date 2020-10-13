package com.example.khareedlo.Product.ApprovedProducts;

public class ApprovedProducts_Class {
    String id;
    String used_status;
    String brandname;
    String modelname;
    String storage;
    String color;
    String price;
    String image;
    String is_ptaapproved;

    public String getIs_ptaapproved() {
        return is_ptaapproved;
    }

    public void setIs_ptaapproved(String is_ptaapproved) {
        this.is_ptaapproved = is_ptaapproved;
    }

    public String getImage() {
        return image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsed_status() {
        return used_status;
    }

    public void setUsed_status(String used_status) {
        this.used_status = used_status;
    }


    public  ApprovedProducts_Class(){}
    public ApprovedProducts_Class(String id,String brandname, String modelname, String storage,String color, String price,String used_status,String is_pta_approved,String image) {
        this.brandname = brandname;
        this.modelname = modelname;
        this.price = price;
        this.storage=storage;
        this.color=color;
        this.id= id;
        this.used_status= used_status;
        this.is_ptaapproved=is_pta_approved;
        this.image=image;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }



    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
