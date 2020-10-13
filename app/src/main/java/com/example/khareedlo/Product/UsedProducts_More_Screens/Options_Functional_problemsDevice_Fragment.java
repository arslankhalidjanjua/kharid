package com.example.khareedlo.Product.UsedProducts_More_Screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khareedlo.R;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Options_Functional_problemsDevice_Fragment extends Fragment {

CheckBox cb_bluetooth,cb_gps,cb_wifi,cb_backcamera,cb_frontcamera,cb_volumeup,cb_volumedown,cb_touch,cb_vibration,cb_flashlight,cb_speaker,
    cb_network,cb_charging,cb_battery,cb_fingerprint;
String bluetooth,gps,wifi,backcamera,frontcamera,volumeup,volumedown,touch,vibration,flashlight,speaker,network,charging,battery,fingerprint;
SweetAlertDialog pDialog;
    String brandName,ModelName,Storageofdevice,Colorofdevice,modelID,brandID,colorID,storageID,ptaapproved,price;
    String patches,scratches,damage,originalCharger,ageOfDevice,overAllPhoneCondition;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String seller_id,seller_name;
    int key;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.options_device_functions_problems_fragment, container, false);
        cb_bluetooth=view.findViewById(R.id.cb_bluetooth);
        cb_gps=view.findViewById(R.id.cb_gps);
        cb_wifi=view.findViewById(R.id.cb_wifi);
        cb_backcamera= view.findViewById(R.id.cb_backcamera);
        cb_frontcamera=view.findViewById(R.id.cb_frontcamera);
        cb_volumeup=view.findViewById(R.id.cb_volumeup);
        cb_volumedown=view.findViewById(R.id.cb_volumedown);
        cb_touch= view.findViewById(R.id.cb_touch);
        cb_vibration= view.findViewById(R.id.cb_vibration);
        cb_flashlight= view.findViewById(R.id.cb_flashlight);
        cb_speaker= view.findViewById(R.id.cb_speaker);
        cb_network= view.findViewById(R.id.cb_network);
        cb_charging= view.findViewById(R.id.cb_charging);
        cb_battery= view.findViewById(R.id.cb_battery);
        cb_fingerprint= view.findViewById(R.id.cb_fingerprint);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
seller_id= sharedPreferences.getString("seller_id","0");
seller_name= sharedPreferences.getString("seller_name","0");
        Bundle args = getArguments();
        if (args  != null){
            key = args.getInt("keybtn");

            brandName= args.getString("brandname");
            ModelName= args.getString("modelofbrand");
            Storageofdevice= args.getString("storage");
            storageID= args.getString("storageID");
            modelID= args.getString("modelID");
            Colorofdevice= args.getString("color");
            colorID=args.getString("colorID");
            brandID=args.getString("brandID");
            ptaapproved=args.getString("Ptaapproved");
            price=args.getString("price");

            patches=args.getString("patches");
            scratches=args.getString("scratches");
            damage=args.getString("damage");
            originalCharger=args.getString("originalCharger");
            ageOfDevice=args.getString("ageOfDevice");
            overAllPhoneCondition=args.getString("overAllPhoneCondition");
            Toast.makeText(getContext(), ""+args.getString("patches"), Toast.LENGTH_SHORT).show();

        }
        ImageView imageViewbackbtn;
        imageViewbackbtn=getView().findViewById(R.id.idbackfunctional);
        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Options_Predict_Price_Fragment());
                fragmentTransaction.commit();
            }
        });
        ImageView imageViewhome;
        imageViewhome=getView().findViewById(R.id.idhomefunctional);
        imageViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });
        FloatingActionButton floatingActionButton;
        floatingActionButton=getView().findViewById(R.id.idfloatbtnfunctionl);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key ==0)
                {

                    bluetooth=cb_bluetooth.isChecked()? "No" :"Yes";
                    wifi=cb_wifi.isChecked()? "No" :"Yes";
                    gps=cb_gps.isChecked()? "No" :"Yes";
                    backcamera=cb_backcamera.isChecked()? "No" :"Yes";
                    frontcamera=cb_frontcamera.isChecked()? "No" :"Yes";
                    volumeup=cb_volumeup.isChecked()? "No" :"Yes";
                    volumedown=cb_volumedown.isChecked()? "No" :"Yes";
                    touch=cb_touch.isChecked()? "No" :"Yes";
                    vibration=cb_vibration.isChecked()? "No" :"Yes";
                    flashlight=cb_flashlight.isChecked()? "No" :"Yes";
                    speaker=cb_speaker.isChecked()? "No" :"Yes";
                    network=cb_network.isChecked()? "No" :"Yes";
                    charging=cb_charging.isChecked()? "No" :"Yes";
                    battery=cb_battery.isChecked()? "No" :"Yes";
                    fingerprint=cb_fingerprint.isChecked()? "No" :"Yes";

                    String url= getString(R.string.url)+
                            "add_seller_product_used.php?"+
                            "seller_id="+seller_id+
                            "&seller_name="+seller_name+
                            "&brand_name="+brandName+
                            "&brand_id="+brandID+
                            "&model_id="+modelID+
                            "&model_name="+ModelName+
                            "&storage_id="+storageID+
                            "&storage_name="+Storageofdevice+
                            "&color_name="+Colorofdevice+
                            "&color_id="+colorID+
                            "&is_pta_approved="+ptaapproved+
                            "&price="+price+
                            "&patches="+patches+"&scratches="+scratches+"&damage="+damage+
                            "&original_charger="+originalCharger+"&age_of_device="+ageOfDevice+
                            "&overall_phone_condition="+overAllPhoneCondition+
                            "&bluetooth="+ bluetooth+"&gps="+gps+"&wifi="+wifi+
                            "&backcamera="+backcamera+"&frontcamera="+frontcamera+"&volumeup="+volumeup+
                            "&volumedown="+volumedown+"&touch="+touch+"&vibration="+vibration+"&flashlight="+flashlight+
                            "&speaker="+speaker+"&network="+network+"&charging="+charging+"&battery="+battery+
                            "&fingerprint="+fingerprint;
                    url=url.replaceAll(" ","%20");
                    new JsonTask().execute(url);

                    Toast.makeText(getContext(), "Used added", Toast.LENGTH_SHORT).show();
                }
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(android.R.id.content, new ());
//                fragmentTransaction.commit();
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please Wait ...");
            pDialog.setCancelable(false);
            pDialog.show();



        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                //connection.setDoInput(true);




                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(result.contains("Success"))
            {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        // .setContentText(ex.getLocalizedMessage())
                        .setContentText("Your device is sent for approval")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                fragmentManager = getFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                                fragmentTransaction.commit();

                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();



            }
            else if (result.contains("Error"))
            {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        // .setContentText(ex.getLocalizedMessage())
                        .setContentText("Your device couldn't be sent for approval.Try Again")
                        .show();
            }
            else
            {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        // .setContentText(ex.getLocalizedMessage())
                        .setContentText("Unable to get Response from server")
                        .show();
            }


        }


    }

}
