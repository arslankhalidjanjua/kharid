package com.example.khareedlo.Product.AddMobile.SelectModel;

import android.view.Display;

public class ModelClass {


     public ModelClass(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;
    public ModelClass(String id,String modelname) {
        this.id=id;
        Modelname = modelname;
    }

    public String getModelname() {
        return Modelname;
    }

    public void setModelname(String modelname) {
        Modelname = modelname;
    }

    String Modelname;

}
