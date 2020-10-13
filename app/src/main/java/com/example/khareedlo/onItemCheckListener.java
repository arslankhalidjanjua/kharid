package com.example.khareedlo;

import com.example.khareedlo.Product.AddMobile.Select_Color.ColorClass;

public interface onItemCheckListener {
    void onItemCheck(ColorClass item);
    void onItemUncheck(ColorClass item);
    void onColorCheck(String colorsT);
    void onColorUncheck(String colorU);
}
