package com.example.khareedlo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {

    Context c;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public PrefManager(Context c)
    {
        this.c = c;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.c);
        this.editor = sharedPreferences.edit();
    }



    public void setDay(String day)
    {
        editor.putString("day" , day);
        editor.commit();

    }

    public String getDay()
    {
        return sharedPreferences.getString("day" , "14");
    }

    public void setColor(String hour)
    {
        editor.putString("color" , hour);
        editor.commit();

    }

    public String getColor()
    {
        return sharedPreferences.getString("color" , "");
    }

    public void setFirstTime(boolean firstTime)
    {
        editor.putBoolean("FirstTime" , firstTime);
        editor.commit();

    }


    public boolean isFirstTime()
    {
        return sharedPreferences.getBoolean("FirstTime" , false);
    }


    public void setLogin(boolean firstTime)
    {
        editor.putBoolean("FirstLogin" , firstTime);
        editor.commit();

    }


    public boolean isLogin()
    {
        return sharedPreferences.getBoolean("FirstLogin" , false);
    }



}

