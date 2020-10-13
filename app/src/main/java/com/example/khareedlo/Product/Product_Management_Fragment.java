package com.example.khareedlo.Product;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Dashboard;
import com.example.khareedlo.PrefManager;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Product.ApprovedProducts.Approved_Product_Fragment;
import com.example.khareedlo.Product.PendingProducts.Pending_Product_Fragment;
import com.example.khareedlo.Product.RejectedProducts.Rejected_Product_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.SubscriptionMain;
import com.example.khareedlo.WidgetService;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;

public class Product_Management_Fragment extends Fragment {


    CardView cvaddproduct,cvapproved,cvreject,cvpending;
    String seller_id;
    String status,plan,method,statusM="Trial",planStatus,days_left;
    SweetAlertDialog pDialog;
    private SimpleDateFormat dateFormat,timeFormat;
    private String date,time;
    String REgDAte;
    private Calendar calendar;
    long differenceT;
    ImageView imageViewbackbtn;
//    Intent mServiceIntent;
//    private WidgetService mWidgetService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_product_management, container, false);
        cvaddproduct=view.findViewById(R.id.idcvaddproduct);
        cvapproved=view.findViewById(R.id.idcvapproved);
        cvpending=view.findViewById(R.id.idcvpending);
        cvreject=view.findViewById(R.id.idcvreject);
        imageViewbackbtn=view.findViewById(R.id.idbackproduct);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Handle the back pressed
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        seller_id= getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");

        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Dashboard());
            }
        });

     //   load_subscription();

       // Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();



        cvaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadPage(new Category_Product_Fragment());

            }
        });

        cvapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Approved_Product_Fragment());

            }
        });

        cvreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Rejected_Product_Fragment());
            }
        });
        cvpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
loadPage(new Pending_Product_Fragment());
            }
        });



    }

    private boolean loadPage(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.idframelayoutproduct, fragment)
                    .commit();
            return true;
        }
        return false;
    }

//    public void load_subscription()
//    {
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String url= getString(R.string.url)+"fetch_subscription.php?getData=";
//        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+seller_id, null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
//                        //progressDialog.dismiss();
//                        try {
//                            JSONArray jsonArray=response.getJSONArray("MyData");
//                            for (int i=0 ;i<jsonArray.length();i++) {
//                                status=jsonArray.getJSONObject(i).getString("approval_status");
//                                plan=jsonArray.getJSONObject(i).getString("plan");
//                                method=jsonArray.getJSONObject(i).getString("paid_via");
//                                planStatus=jsonArray.getJSONObject(i).getString("plan_status");
//                                days_left=jsonArray.getJSONObject(i).getString("days_left");
//
//                                //Toast.makeText(getContext(), "seller: "+seller_id+"status:"+ status+"method: "+method+"plan: "+plan, Toast.LENGTH_SHORT).show();
//
//                                if (planStatus.equals("Expired") && days_left.equals("0") && method.equals("Trial"))
//                                {
//                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                                    pDialog.setTitleText("Trial Ended..");
//                                    pDialog.setContentText("Your 15 days trial has been ended.. Kindly Subscribe for Monthly, Quarterly or Yearly plan to continue enjoying this app..");
//                                    pDialog.setCancelable(false);
//                                    pDialog.show();
//                                    pDialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            FragmentManager fragmentManager;
//                                            FragmentTransaction fragmentTransaction;
//                                            fragmentManager = getFragmentManager();
//                                            fragmentTransaction = fragmentManager.beginTransaction();
//                                            fragmentTransaction.replace(android.R.id.content, new SubscriptionMain()).addToBackStack(null);
//                                            fragmentTransaction.commit();
//                                            pDialog.dismissWithAnimation();
//                                        }
//                                    });
//                                }
//
//                                else if (status.equals("Pending") && !(method.equals("Trial")) )
//                                {
//                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                                    pDialog.setTitleText("Please Wait..");
//                                    pDialog.setContentText("Your request for subscribing "+plan+" plan is not verified yet..\n Kindly wait for the admin to verify your subscription..");
//                                    pDialog.setCancelable(false);
//                                    pDialog.show();
//                                    pDialog.findViewById(R.id.confirm_button).setVisibility(View.GONE);
//                                }
//                                else if (planStatus.equals("Expired") && !(method.equals("Trial")))
//                                {
//                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                                    pDialog.setTitleText("Subscription Ended..");
//                                    pDialog.setContentText("Kindly Subscribe for Monthly, Quarterly or Yearly plan to continue enjoying this app..");
//                                    pDialog.setCancelable(false);
//                                    pDialog.show();
//                                    pDialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            FragmentManager fragmentManager;
//                                            FragmentTransaction fragmentTransaction;
//                                            fragmentManager = getFragmentManager();
//                                            fragmentTransaction = fragmentManager.beginTransaction();
//                                            fragmentTransaction.replace(android.R.id.content, new SubscriptionMain()).addToBackStack(null);
//                                            fragmentTransaction.commit();
//                                        }
//                                    });
//                                }
//                                else if (status.equals("Approved") && planStatus.equals("Active"))
//                                {
//                                    PrefManager prefManager=new PrefManager(getContext());
//                                    if (prefManager.isFirstTime()) {
//
//
//                                        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
//                                        pDialog.setTitleText("Thank You..");
//                                        pDialog.setContentText("Dear User you are successfully subscribed to " + plan + " basic plan. Your trial ends in 29 Days 23 Hours.");
//                                        pDialog.setCancelable(false);
//                                        pDialog.show();
//                                        prefManager.setFirstTime(false);
//                                    }
//
//                                }
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //progressDialog.dismiss();
//                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//        // add it to the RequestQueue
//        queue.add(getRequest);
//
//    }


}
