package com.example.khareedlo.Subscription;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Fragment_More;
import com.example.khareedlo.PrefManager;
import com.example.khareedlo.Profile.ProfileFragment;
import com.example.khareedlo.R;
import com.example.khareedlo.SubscriptionMain;
import com.example.khareedlo.SubscriptionPlan;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Subscription extends Fragment implements AdapterView.OnItemSelectedListener {
    ImageView imageView;
SweetAlertDialog pDialog;
    Spinner spnPaymentMethod;
    EditText edTrxID;
    TextView edMobile,edPrice;
    Button btnSubscribe;
    String seller_id;
    String planN,plan,price;
    Date currentTime;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    String status,method,priceN;
    ArrayList<String> aList;
    DateTimeFormatter format;
    DateTime CurrentDate ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        planN=getArguments().getString("plan1");
        price=getArguments().getString("price");
        return inflater.inflate(R.layout.fragment_subscription, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toast.makeText(getContext(), "plan: "+planN, Toast.LENGTH_SHORT).show();


        format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        CurrentDate=DateTime.now();
        CurrentDate=CurrentDate.plusHours(5);
        final String newCDate=CurrentDate.toString(format);
        aList= new ArrayList<>();

        calendar = Calendar.getInstance();
        imageView=getView().findViewById(R.id.idback);
        spnPaymentMethod= getView().findViewById(R.id.payment_method_spinner);
        edTrxID= getView().findViewById(R.id.edTrxID);
        edMobile=getView().findViewById(R.id.edMobile);
        edPrice=getView().findViewById(R.id.edPrice);
        currentTime = Calendar.getInstance().getTime();

        edPrice.setText(price);

        seller_id= getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");

//        aList.add("Jazz Cash");
//        aList.add("Easy Paisa");
        btnSubscribe= getView().findViewById(R.id.idbtnsubscription);
//        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, aList);
//        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//        spnPaymentMethod.setAdapter(arrayAdapter);

        spnPaymentMethod.setOnItemSelectedListener(this);
        load_method();
        load_account("JazzCash");
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        //Toast.makeText(getContext(), "date: "+date, Toast.LENGTH_SHORT).show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new SubscriptionPlan());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url= getString(R.string.url)+"fetch_subscription.php?getData=";
                final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+seller_id, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
                                //progressDialog.dismiss();
                                try {
                                    JSONArray jsonArray=response.getJSONArray("MyData");
                                    for (int i=0 ;i<jsonArray.length();i++) {
                                        status=jsonArray.getJSONObject(i).getString("approval_status");
                                        plan=jsonArray.getJSONObject(i).getString("plan");
                                        method=jsonArray.getJSONObject(i).getString("paid_via");
                                        priceN=jsonArray.getJSONObject(i).getString("price");



                                        //Toast.makeText(getContext(), "seller: "+seller_id+"status:"+ status+"method: "+method+"plan: "+plan, Toast.LENGTH_SHORT).show();

                                        if (status.equals("Pending") && !(method.equals("Trial")) )
                                        {
                                            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                            pDialog.setTitleText("Please Wait..");
                                            pDialog.setContentText("One of your subscription is already pending..");
                                            pDialog.setCancelable(false);
                                            pDialog.show();
                                            pDialog.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                                        }
                                        else
                                        {

                                            if (TextUtils.isEmpty(edTrxID.getText()) || TextUtils.isEmpty(edMobile.getText())) {
                                                Toast.makeText(getContext(), "Please input all the data", Toast.LENGTH_SHORT).show();
                                            } else {

                                                final String method = spnPaymentMethod.getSelectedItem().toString();

                                                final String trx_id = edTrxID.getText().toString();
                                                final String mobile = edMobile.getText().toString();


                                                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                pDialog.setTitleText("Uploading Data");
                                                pDialog.setCancelable(false);
                                                pDialog.show();


                                                String url1 = getString(R.string.url) + "add_subscription.php";
                                                StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        pDialog.dismissWithAnimation();
                                                        if (response.equals("success")) {
                                                            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                                            pDialog.setTitleText("Your subscription is sent for approval!");

                                                            PrefManager prefManager=new PrefManager(getContext());
                                                            prefManager.setFirstTime(true);
                                                            pDialog.show();

                                                            FragmentManager fragmentManager;
                                                            FragmentTransaction fragmentTransaction;
                                                            fragmentManager = getFragmentManager();
                                                            fragmentTransaction = fragmentManager.beginTransaction();
                                                            fragmentTransaction.replace(android.R.id.content, new Fragment_More()).addToBackStack(null);
                                                            fragmentTransaction.commit();

                                                        } else {
                                                            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                                            pDialog.setTitleText("Some Error Occurred");
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
                                                        params.put("seller_id", seller_id);
                                                        params.put("tx_id", trx_id);
                                                        params.put("paid_via", method);
                                                        params.put("mobile", mobile);
                                                        params.put("plan", planN);
                                                        params.put("paid_on", date);
                                                        params.put("plan_status","Active");
                                                        params.put("price",price);

                                                        return params;

                                                    }

                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                                requestQueue.add(request);
                                                //Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();

                                            }
                                        }

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
                                //progressDialog.dismiss();
                                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                // add it to the RequestQueue
                queue.add(getRequest);

            }
        });

    }


    public void load_method()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_method_only.php";
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            aList.clear();
                            String t_city,methods;
                            JSONArray jsonArray=response.getJSONArray("methods");
                            for (int i=0 ;i<jsonArray.length();i++) {
                                methods=jsonArray.getJSONObject(i).getString("method");
                                aList.add(methods);

                            }
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, aList);
                            arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            spnPaymentMethod.setAdapter(arrayAdapter);
                            String methodFromSpinner=spnPaymentMethod.getSelectedItem().toString().trim();
                            load_account(methodFromSpinner);
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

        //getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add it to the RequestQueue
        queue.add(getRequest);




    }

    public void load_account(String spinnerItem)
    {

        //Toast.makeText(getContext(), "province: "+province, Toast.LENGTH_SHORT).show();
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_account_method.php?getData=";
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+spinnerItem, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        try {
                            String accountN,t_province;
                            JSONArray jsonArray=response.getJSONArray("MyData");
                            for (int i=0 ;i<jsonArray.length();i++) {
                                accountN=jsonArray.getJSONObject(i).getString("acc_no");
                                //Toast.makeText(getContext(), "account: "+accountN, Toast.LENGTH_SHORT).show();
                                edMobile.setText(accountN);
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String label = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getContext(), "label: "+label, Toast.LENGTH_SHORT).show();
        load_account(label);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
