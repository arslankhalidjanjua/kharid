package com.example.khareedlo.Product.RejectedProducts;

public class RejectedProducts_Class {

    String brandname;
    String modelname;
    String storage;
    String color;
    String price;

    public String getImage() {
        return image;
    }

    String image;

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

    String id;
    String used_status;

    public String getIs_pta_approved() {
        return is_pta_approved;
    }

    String is_pta_approved;
    public  RejectedProducts_Class(){}
    public RejectedProducts_Class(String id,String brandname, String modelname, String storage,String color, String price,String used_status,String is_pta_approved,String image) {
        this.brandname = brandname;
        this.modelname = modelname;
        this.price = price;
        this.storage=storage;
        this.color=color;
        this.id= id;
        this.used_status= used_status;
        this.image=image;
        this.is_pta_approved=is_pta_approved;
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
