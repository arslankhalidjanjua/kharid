package com.example.khareedlo.Product.AddMobile.EnterPrice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.SelectBrand.Select_Brand_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Product.FramelayoutProduct_Fragment;
import com.example.khareedlo.Product.Product_Management_Fragment;
import com.example.khareedlo.Product.UsedProducts_More_Screens.Options_Predict_Price_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Enter_Price_Fragment extends Fragment {



    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    FloatingActionButton floatingActionButton;
    ImageView imageViewhome;
    SweetAlertDialog pDialog;

    TextView tvbrand,tvmodel,tvstorage,tvcolor,tvpta,headerbrandplusmodel;
    EditText edprice;
    String seller_id,seller_name;
    int key;
    String brandName,ModelName,Storageofdevice,Colorofdevice,modelID,brandID,colorID,storageID,ptaapproved,price,warranty_type,sim,accidentalWarranty;
    Spinner spwarrantyType;
    RadioGroup rgSim,rgAccidentalWarranty;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.select_price_fragment, container, false);
        imageViewbackbtn=view.findViewById(R.id.idbackprice);
        imageViewhome=view.findViewById(R.id.idhomeprice);
        floatingActionButton=view.findViewById(R.id.idfloatbtnprice);
        spwarrantyType= view.findViewById(R.id.warranty_type);
        tvbrand=view.findViewById(R.id.idtvbrandpricefrag);
        tvmodel=view.findViewById(R.id.idtvmodelpricefrag);
        tvstorage=view.findViewById(R.id.idtvramrompricefrag);
        tvcolor=view.findViewById(R.id.idtvcolorpricefrag);
        tvpta=view.findViewById(R.id.idtvptapricefrag);
        headerbrandplusmodel=view.findViewById(R.id.idheaderbrandplusmodel);
        edprice= view.findViewById(R.id.idedprice);
        rgSim= view.findViewById(R.id.idradioroupsim);
        rgAccidentalWarranty=view.findViewById(R.id.idradioroupaccidental);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");
        seller_name=sharedPreferences.getString("seller_name","0");
        String[] warrantyTypes={"None","Local","International"};
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, warrantyTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spwarrantyType.setAdapter(arrayAdapter);

        spwarrantyType.setSelection(0);

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

            tvbrand.setText("Brand : "+brandName);
            tvmodel.setText("Model : "+ModelName);
            tvstorage.setText("Ram/Rom : "+Storageofdevice);
            tvcolor.setText("Color : "+Colorofdevice);
            tvpta.setText("PTA Approved : "+ptaapproved);
            headerbrandplusmodel.setText(brandName +" "+ ModelName);
            Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();


        }


        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Select_Used_or_New_Fragment());
                fragmentTransaction.commit();
            }
        });


        imageViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sim= ((RadioButton) getView().findViewById(rgSim.getCheckedRadioButtonId()))
                        .getText().toString();
                accidentalWarranty= ((RadioButton)getView().findViewById(rgAccidentalWarranty.getCheckedRadioButtonId())).getText().toString();

                warranty_type= spwarrantyType.getSelectedItem().toString();
                price= edprice.getText().toString();

                if (price.isEmpty())
                {
                    Toast.makeText(getContext(), "Enter Price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (key==1)
                {String url;
                url=getString(R.string.url)+
                        "add_seller_product_new.php?" +
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
                        "&warranty_type="+warranty_type+
                        "&accidental_warranty="+accidentalWarranty+
                        "&sim="+sim+
                "&isAccessory="+"mobile";

                    new JsonTask().execute(url.replace(" ","%20"));
                    Toast.makeText(getContext(), "Added new ", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "One: "+seller_id+seller_name+brandName+brandID+modelID+ModelName
                            +storageID+Storageofdevice+Colorofdevice
                            +colorID+ptaapproved+price+warranty_type+accidentalWarranty+sim, Toast.LENGTH_SHORT).show();


                }
                else{

                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("keybtn", 0);
                    bundle.putString("brandname",brandName);
                    bundle.putString("brandID",brandID);
                    bundle.putString("modelofbrand",ModelName);
                    bundle.putString("modelID",modelID);
                    bundle.putString("storage",Storageofdevice);
                    bundle.putString("storageID",storageID);
                    bundle.putString("color",Colorofdevice);
                    bundle.putString("colorID",colorID);
                    bundle.putString("Ptaapproved",ptaapproved);
                    bundle.putString("price",price);
                    bundle.putString("warranty_type",warranty_type);
                    bundle.putString("accidental_warranty",accidentalWarranty);
                    bundle.putString("sim",sim);

                    Options_Predict_Price_Fragment obj=new Options_Predict_Price_Fragment();
                    obj.setArguments(bundle);
                    fragmentTransaction.replace(android.R.id.content, obj);
                    fragmentTransaction.commit();
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
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();


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
