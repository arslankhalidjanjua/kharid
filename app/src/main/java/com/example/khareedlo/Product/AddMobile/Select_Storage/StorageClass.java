package com.example.khareedlo.Product.AddMobile.Select_Storage;

public class StorageClass {


     public StorageClass(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public StorageClass(String id,String storage) {
        this.storage = storage;
        this.id=id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    String storage;

}
