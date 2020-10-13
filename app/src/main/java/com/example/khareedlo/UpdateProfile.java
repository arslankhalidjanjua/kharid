package com.example.khareedlo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Profile.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfile extends Fragment {
    EditText ed_pass, ed_confirmpass, ed_address,edtcnic, edtproprietername,edtshopname,edtsalesmannumber,edtemail;
    ImageView imback;
    Button btnsave;
    Spinner city_spinner,province_spinner;
    ArrayList<String> aList,provinceList;
    String seller_id;
    SweetAlertDialog pDialog;
    String password,address,p_name,shop_name,p_phone;
    public UpdateProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");

        imback=getView().findViewById(R.id.idbackemail);
        aList= new ArrayList<>();
        provinceList=new ArrayList<>();
        city_spinner=getView().findViewById(R.id.edit_city);
        province_spinner=getView().findViewById(R.id.edit_province_spinner);
        ed_pass = getView().findViewById(R.id.new_password);
        ed_confirmpass= getView().findViewById(R.id.new_password_confirm);
        ed_address= getView().findViewById(R.id.idedittextaddress);
        edtcnic=getView().findViewById(R.id.edit_cnic);
        btnsave=getView().findViewById(R.id.idbtnotherdetailsave);
        edtproprietername=getView().findViewById(R.id.edit_proprietername);
        edtshopname=getView().findViewById(R.id.edit_shopname);
        edtsalesmannumber=getView().findViewById(R.id.edit_salesmannumber);
        edtemail=getView().findViewById(R.id.edit_selleremail);

        getprofile_data();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_profile();
            }
        });
        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new ProfileFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    public void getprofile_data()
    {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_seller_details.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            JSONArray jsonArray=response.getJSONArray("seller");
                            for (int i=0 ;i<jsonArray.length();i++)
                            {


                                JSONObject profileobj= jsonArray.getJSONObject(i);
                                Log.i(TAG, "onResponse: "+profileobj);

                                edtshopname.setText(profileobj.getString("shop_name"));
//                                ed.setText("Contact Number : " +profileobj.getString("phone"));
                                ed_address.setText(profileobj.getString("address"));
                                edtsalesmannumber.setText(profileobj.getString("used_mobile_salesman"));
//                                ed.setText("City : " +profileobj.getString("city"));
                                edtproprietername.setText(profileobj.getString("proprietor_name"));
                                ed_pass.setText(profileobj.getString("password"));
//                                tvopen.setText("Shop Opening Time  : " +profileobj.getString("opening_time"));
//                                tvclose.setText("Shop Closing Time  : " +profileobj.getString("closing_time"));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    void update_profile() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Updating Time");
        pDialog.setCancelable(false);
        pDialog.show();

//        SharedPreferences sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
//        seller_id=sharedPreferences.getString("seller_id","0");
        p_name=edtproprietername.getText().toString();
        p_phone=edtsalesmannumber.getText().toString();
        address=ed_address.getText().toString();
        shop_name=edtshopname.getText().toString();
        password=ed_pass.getText().toString();

        String url = getString(R.string.url) + "update_seller.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Profile Updated");
                    pDialog.show();
                } else {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Some Error Occurred.");
                    pDialog.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",seller_id);
                params.put("p_name",p_name);
                params.put("p_address",address);
                params.put("p_phone",p_phone);
                params.put("password",password);
                params.put("shop_name",shop_name);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }

}
