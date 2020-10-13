package com.example.khareedlo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.Select_PTA_Approved_OR_NOT.Select_PTA_APPROVED_OR_NOT_Fragment;
import com.example.khareedlo.Product.UsedProducts_More_Screens.Options_Predict_Price_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsPredictAcc extends Fragment {

    SweetAlertDialog pDialog;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int key;
    public String brandName,ModelName,Storageofdevice,storageID,modelID,colorID,Colorofdevice,brandID,ptaapproved,price,warranty_type,accidentalWarranty,sim;
    CheckBox patches_c,scratches_c,damage_c,originalCharger_c;
    Spinner ageOfDevice_ed;
    RatingBar overAllPhoneCondition_rating;
    String patches,scratches,damage,originalCharger,ageOfDevice,overAllPhoneCondition;
    public OptionsPredictAcc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options_predict_acc, container, false);
        patches_c=view.findViewById(R.id.patches_cb);
        scratches_c=view.findViewById(R.id.scratches_cb);
        damage_c=view.findViewById(R.id.damage_cb);
        originalCharger_c=view.findViewById(R.id.original_charger_cb);
        ageOfDevice_ed=view.findViewById(R.id.age_of_phone_dd);
        overAllPhoneCondition_rating= view.findViewById(R.id.rating1);
        List<String> count = new ArrayList<String>();

        for (int i=1;i<=15;i++)
        {
            count.add(i + " Month(s)");
        }
        count.add("> 1 Year");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, count);
        ageOfDevice_ed.setAdapter(dataAdapter);

        ageOfDevice_ed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ageOfDevice=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        overAllPhoneCondition_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Toast.makeText(getContext(), ""+rating, Toast.LENGTH_SHORT).show();//r.getRating();
                overAllPhoneCondition=""+rating;
            }
        });



        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            warranty_type=args.getString("warranty_type");
            accidentalWarranty=args.getString("accidental_warranty");
            sim=args.getString("sim");

        }

        ImageView imageViewbackbtn;
        imageViewbackbtn=getView().findViewById(R.id.idbackdeviceage);
        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new SelectPTAApprovedAcc());
                fragmentTransaction.commit();
            }
        });
        ImageView imageViewhome;
        imageViewhome=getView().findViewById(R.id.idhomedeviceage);
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
        floatingActionButton=getView().findViewById(R.id.idfloatbtnage);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (key==0)
//                {
//                    fragmentManager = getFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("keybtn", 0);
//
//
//                    //Pass checkboxes value to the next activity
//
//                    bundle.putString("brandname",brandName);
//                    bundle.putString("brandID",brandID);
//                    bundle.putString("modelofbrand",ModelName);
//                    bundle.putString("modelID",modelID);
//                    bundle.putString("storage",Storageofdevice);
//                    bundle.putString("storageID",storageID);
//                    bundle.putString("color",Colorofdevice);
//                    bundle.putString("colorID",colorID);
//                    bundle.putString("Ptaapproved",ptaapproved);
//
//                    bundle.putString("price",price);
//
//                    patches = patches_c.isChecked()? "Yes" : "No";
//                    scratches = scratches_c.isChecked()? "Yes" : "No";
//                    damage = damage_c.isChecked()? "Yes" : "No";
//                    originalCharger = originalCharger_c.isChecked()? "Yes" : "No";
//
//                    overAllPhoneCondition=""+overAllPhoneCondition_rating.getRating();
//                    bundle.putString("patches",patches);
//                    bundle.putString("scratches",scratches);
//                    bundle.putString("damage",damage);
//                    bundle.putString("originalCharger",originalCharger);
//                    bundle.putString("ageOfDevice",ageOfDevice);
//                    bundle.putString("overAllPhoneCondition",overAllPhoneCondition);
//
//                    Toast.makeText(getContext(), ""+patches, Toast.LENGTH_SHORT).show();
//                    Options_Functional_problemsDevice_Fragment obj=new Options_Functional_problemsDevice_Fragment();
//                    obj.setArguments(bundle);
//                    fragmentTransaction.replace(android.R.id.content, obj);
//                    fragmentTransaction.commit();
//               }




                if (key ==0)
                {

                    String seller_id=getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");
                    String seller_name= getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_name","0");

                    String url= getString(R.string.url)+

                            "add_seller_product_used_access.php?"+
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
                            "&bluetooth=NA&gps=NA&wifi=NA&backcamera=NA&frontcamera=NA&volumeup=NA"+
                            "&volumedown=NA&touch=NA&vibration=NA&flashlight=NA"+
                            "&speaker=NA&network=NA&charging=NA&battery=NA"+
                            "&fingerprint=NA"+
                            "&warranty_type="+warranty_type+
                            "&accidental_warranty="+accidentalWarranty+
                            "&sim="+sim;
//                    Toast.makeText(getContext(), ""+seller_id+sim+accidentalWarranty+warranty_type+overAllPhoneCondition+
//                            ageOfDevice+originalCharger+damage+scratches+patches+price+ptaapproved+colorID+Colorofdevice+
//                            Storageofdevice+storageID+ModelName+modelID+brandID+brandName+seller_name, Toast.LENGTH_SHORT).show();

                    url=url.replaceAll(" ","%20");
                    new OptionsPredictAcc.JsonTask().execute(url);

                    Toast.makeText(getContext(), "USed added", Toast.LENGTH_SHORT).show();
                }

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
