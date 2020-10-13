package com.example.khareedlo;


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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.khareedlo.Orders.fragment_orders;
import com.example.khareedlo.PrefManager;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Product.ApprovedProducts.Approved_Product_Fragment;
import com.example.khareedlo.Product.PendingProducts.Pending_Product_Fragment;
import com.example.khareedlo.Product.Product_Management_Fragment;
import com.example.khareedlo.Product.RejectedProducts.Rejected_Product_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.SubscriptionMain;
import com.example.khareedlo.WidgetService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;

public class Dashboard extends Fragment {


    CardView cvProduct,cvOrder,cvReport;
    String seller_id;
    String status,plan,method,statusM="Trial",planStatus,days_left;
    SweetAlertDialog pDialog;
    ImageView productImg,OrderImg,ReportImg;
    private SimpleDateFormat dateFormat,timeFormat;
    private String date,time;
    String REgDAte,planN,approved_on;
    TextView txtDay;
    private Calendar calendar;
    long differenceT;
    //    Intent mServiceIntent;
//    private WidgetService mWidgetService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_dashboard, container, false);
        cvProduct=view.findViewById(R.id.idcvProduct);
        cvOrder=view.findViewById(R.id.idcvOrder);
        cvReport=view.findViewById(R.id.idcvReport);
        productImg=view.findViewById(R.id.imgProduct);
        OrderImg=view.findViewById(R.id.imgOrder);
        ReportImg=view.findViewById(R.id.imgReport);
        txtDay=view.findViewById(R.id.txtDay1);


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


        Glide.with(getContext()).load(R.drawable.order_mng).into(OrderImg);
        Glide.with(getContext()).load(R.drawable.product_mng).into(productImg);
        Glide.with(getContext()).load(R.drawable.report).into(ReportImg);

        seller_id= getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");

        checkSubscription();
        load_subscription();

   //    Toast.makeText(getContext(), ""+days_left, Toast.LENGTH_SHORT).show();



        cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadPage(new Product_Management_Fragment());

            }
        });

        cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new fragment_orders());

            }
        });

        cvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new ReportFragment());
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


    public void checkSubscription()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getString(R.string.url) + "fetch_seller_details.php?id=" + seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("seller");
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject profileobj = jsonArray.getJSONObject(i);
                                Log.i(TAG, "onResponse: " + profileobj);

                                REgDAte = profileobj.getString("reg_date");
                                final String Time = profileobj.getString("reg_time");


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
                                                        planN=jsonArray.getJSONObject(i).getString("plan");
                                                        method=jsonArray.getJSONObject(i).getString("paid_via");
                                                        planStatus=jsonArray.getJSONObject(i).getString("plan_status");
                                                        approved_on=jsonArray.getJSONObject(i).getString("approved_on");

                                                        if (status.equals("Approved") && planN.equals("monthly") && planStatus.equals("Active"))
                                                        {
                                                            DateTimeFormatter format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTimeFormatter formatN= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime startDate = format.parseDateTime(Time);
                                                            DateTime endDate = new DateTime(startDate);
                                                            endDate=endDate.plusDays(29);

                                                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime dateTimeNow= DateTime.now();
                                                            dateTimeNow=dateTimeNow.plusHours(5);
                                                            String str = String.valueOf(dateTimeNow);

                                                            DateTimeFormatter f1= DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            DateTime CurrentDate=DateTime.now();
                                                            String newCDate=CurrentDate.toString(f1);

                                                            DateTimeFormatter f2= DateTimeFormat.forPattern("HH:mm:ss");
                                                            DateTime CurrentTime=DateTime.now();
                                                            CurrentTime=CurrentTime.plusHours(5);
                                                            String newCTime=CurrentTime.toString(f2);

                                                            DateTimeFormatter formatterN = DateTimeFormat.forPattern("HH:mm:ss");
                                                            String StartTimeNew=formatterN.print(startDate);
                                                            String EndTimeNew=formatterN.print(endDate);

                                                            DateTimeFormatter formatterNN = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            String EndDateNew=formatterNN.print(endDate);

                                                            // Toast.makeText(getContext(), "EndNewTime: "+EndTimeNew+" newCTime: "+newCTime+" newCDate: "+newCDate+"EndNewDate: "+EndDateNew, Toast.LENGTH_SHORT).show();
                                                            if (dateTimeNow.isAfter(endDate) && planStatus.equals("Expired"))
                                                            {
                                                                txtDay.setText("0");
                                                            }
                                                            else if (f1.parseDateTime(newCDate).isEqual(formatterNN.parseDateTime(EndDateNew)) && f2.parseDateTime(newCTime).isAfter(formatterN.parseDateTime(EndTimeNew)))
                                                            {
                                                                Toast.makeText(getContext(), "Equal", Toast.LENGTH_SHORT).show();
                                                                txtDay.setText("0");
                                                            }
                                                            else
                                                            {
                                                                txtDay.setText(String.valueOf(Days.daysBetween(dateTimeNow,endDate).getDays()));
                                                            }
                                                            //Toast.makeText(getContext(), "start: "+startDate+" end: "+endDate, Toast.LENGTH_SHORT).show();
                                                            updateDays(Integer.parseInt(txtDay.getText().toString()));

                                                        }
                                                        else if (status.equals("Approved") && planN.equals("yearly") && planStatus.equals("Active"))
                                                        {
                                                            DateTimeFormatter format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTimeFormatter formatN= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime startDate = format.parseDateTime(Time);
                                                            DateTime endDate = new DateTime(startDate);
                                                            endDate=endDate.plusDays(359);

                                                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime dateTimeNow= DateTime.now();
                                                            dateTimeNow=dateTimeNow.plusHours(5);
                                                            String str = String.valueOf(dateTimeNow);

                                                            DateTimeFormatter f1= DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            DateTime CurrentDate=DateTime.now();
                                                            String newCDate=CurrentDate.toString(f1);

                                                            DateTimeFormatter f2= DateTimeFormat.forPattern("HH:mm:ss");
                                                            DateTime CurrentTime=DateTime.now();
                                                            CurrentTime=CurrentTime.plusHours(5);
                                                            String newCTime=CurrentTime.toString(f2);

                                                            DateTimeFormatter formatterN = DateTimeFormat.forPattern("HH:mm:ss");
                                                            String StartTimeNew=formatterN.print(startDate);
                                                            String EndTimeNew=formatterN.print(endDate);

                                                            DateTimeFormatter formatterNN = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            String EndDateNew=formatterNN.print(endDate);

                                                            //Toast.makeText(getContext(), "EndNewTime: "+EndTimeNew+" newCTime: "+newCTime+" newCDate: "+newCDate+"EndNewDate: "+EndDateNew, Toast.LENGTH_SHORT).show();
                                                            if (dateTimeNow.isAfter(endDate) && planStatus.equals("Expired"))
                                                            {
                                                                txtDay.setText("0");
                                                            }
                                                            else if (f1.parseDateTime(newCDate).isEqual(formatterNN.parseDateTime(EndDateNew)) && f2.parseDateTime(newCTime).isAfter(formatterN.parseDateTime(EndTimeNew)))
                                                            {
                                                                Toast.makeText(getContext(), "Equal", Toast.LENGTH_SHORT).show();
                                                                txtDay.setText("0");
                                                            }
                                                            else
                                                            {
                                                                txtDay.setText(String.valueOf(Days.daysBetween(dateTimeNow,endDate).getDays()));
                                                            }

                                                            updateDays(Integer.parseInt(txtDay.getText().toString()));

                                                        }
                                                        else if (status.equals("Approved") && planN.equals("quarterly") && planStatus.equals("Active"))
                                                        {
                                                            DateTimeFormatter format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTimeFormatter formatN= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime startDate = format.parseDateTime(Time);
                                                            DateTime endDate = new DateTime(startDate);
                                                            endDate=endDate.plusDays(89);

                                                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime dateTimeNow= DateTime.now();
                                                            dateTimeNow=dateTimeNow.plusHours(5);
                                                            String str = String.valueOf(dateTimeNow);

                                                            DateTimeFormatter f1= DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            DateTime CurrentDate=DateTime.now();
                                                            String newCDate=CurrentDate.toString(f1);

                                                            DateTimeFormatter f2= DateTimeFormat.forPattern("HH:mm:ss");
                                                            DateTime CurrentTime=DateTime.now();
                                                            CurrentTime=CurrentTime.plusHours(5);
                                                            String newCTime=CurrentTime.toString(f2);

                                                            DateTimeFormatter formatterN = DateTimeFormat.forPattern("HH:mm:ss");
                                                            String StartTimeNew=formatterN.print(startDate);
                                                            String EndTimeNew=formatterN.print(endDate);

                                                            DateTimeFormatter formatterNN = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            String EndDateNew=formatterNN.print(endDate);

                                                            //Toast.makeText(getContext(), "EndNewTime: "+EndTimeNew+" newCTime: "+newCTime+" newCDate: "+newCDate+"EndNewDate: "+EndDateNew, Toast.LENGTH_SHORT).show();
                                                            if (dateTimeNow.isAfter(endDate) && planStatus.equals("Expired"))
                                                            {
                                                                txtDay.setText("0");
                                                            }
                                                            else if (f1.parseDateTime(newCDate).isEqual(formatterNN.parseDateTime(EndDateNew)) && f2.parseDateTime(newCTime).isAfter(formatterN.parseDateTime(EndTimeNew)))
                                                            {
                                                                Toast.makeText(getContext(), "Equal", Toast.LENGTH_SHORT).show();
                                                                txtDay.setText("0");
                                                            }
                                                            else
                                                            {
                                                                txtDay.setText(String.valueOf(Days.daysBetween(dateTimeNow,endDate).getDays()));
                                                            }

                                                            updateDays(Integer.parseInt(txtDay.getText().toString()));

                                                        }
                                                        else
                                                        {
                                                            DateTimeFormatter format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime startDate = format.parseDateTime(Time);
                                                            DateTime endDate = new DateTime(startDate);
                                                            endDate=endDate.plusDays(14);

                                                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                            DateTime dateTimeNow= DateTime.now();
                                                            dateTimeNow=dateTimeNow.plusHours(5);
                                                            String str = String.valueOf(dateTimeNow);

                                                            DateTimeFormatter f1= DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            DateTime CurrentDate=DateTime.now();
                                                            String newCDate=CurrentDate.toString(f1);

                                                            DateTimeFormatter f2= DateTimeFormat.forPattern("HH:mm:ss");
                                                            DateTime CurrentTime=DateTime.now();
                                                            CurrentTime=CurrentTime.plusHours(5);
                                                            String newCTime=CurrentTime.toString(f2);

                                                            DateTimeFormatter formatterN = DateTimeFormat.forPattern("HH:mm:ss");
                                                            String StartTimeNew=formatterN.print(startDate);
                                                            String EndTimeNew=formatterN.print(endDate);

                                                            DateTimeFormatter formatterNN = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                            String EndDateNew=formatterNN.print(endDate);

                                                            //Toast.makeText(getContext(), "EndNewTime: "+EndTimeNew+" newCTime: "+newCTime+" newCDate: "+newCDate+"EndNewDate: "+EndDateNew, Toast.LENGTH_SHORT).show();
                                                            if (dateTimeNow.isAfter(endDate) && planStatus.equals("Expired"))
                                                            {
                                                                txtDay.setText("0");
                                                            }
                                                            else if (f1.parseDateTime(newCDate).isEqual(formatterNN.parseDateTime(EndDateNew)) && f2.parseDateTime(newCTime).isAfter(formatterN.parseDateTime(EndTimeNew)))
                                                            {
                                                                Toast.makeText(getContext(), "Equal", Toast.LENGTH_SHORT).show();
                                                                txtDay.setText("0");
                                                            }
                                                            else
                                                            {
                                                                txtDay.setText(String.valueOf(Days.daysBetween(dateTimeNow,endDate).getDays()));
                                                            }

                                                            updateDays(Integer.parseInt(txtDay.getText().toString()));


                                                        }

                                                        //Toast.makeText(getContext(), "seller: "+seller_id+"status:"+ status+"method: "+method+"plan: "+plan, Toast.LENGTH_SHORT).show();
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

//                                    System.out.println(formatter.print(period));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }


    public void load_subscription()
    {
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
                                planStatus=jsonArray.getJSONObject(i).getString("plan_status");
                                days_left=jsonArray.getJSONObject(i).getString("days_left");

                                //Toast.makeText(getContext(), "seller: "+seller_id+"status:"+ status+"method: "+method+"plan: "+plan, Toast.LENGTH_SHORT).show();

                                if (planStatus.equals("Expired") && days_left.equals("0") && method.equals("Trial"))
                                {
                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    pDialog.setTitleText("Trial Ended..");
                                    pDialog.setContentText("Your 15 days trial has been ended.. Kindly Subscribe for Monthly, Quarterly or Yearly plan to continue enjoying this app..");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    pDialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FragmentManager fragmentManager;
                                            FragmentTransaction fragmentTransaction;
                                            fragmentManager = getFragmentManager();
                                            fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(android.R.id.content, new SubscriptionMain()).addToBackStack(null);
                                            fragmentTransaction.commit();
                                            pDialog.dismissWithAnimation();
                                        }
                                    });
                                }

                                else if (status.equals("Pending") && !(method.equals("Trial")) )
                                {
                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    pDialog.setTitleText("Please Wait..");
                                    pDialog.setContentText("Your request for subscribing "+plan+" plan is not verified yet..\n Kindly wait for the admin to verify your subscription..");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    pDialog.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                                }
                                else if (planStatus.equals("Expired") && !(method.equals("Trial")))
                                {
                                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    pDialog.setTitleText("Subscription Ended..");
                                    pDialog.setContentText("Kindly Subscribe for Monthly, Quarterly or Yearly plan to continue enjoying this app..");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    pDialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FragmentManager fragmentManager;
                                            FragmentTransaction fragmentTransaction;
                                            fragmentManager = getFragmentManager();
                                            fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(android.R.id.content, new SubscriptionMain()).addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }
                                    });
                                }
                                else if (status.equals("Approved") && planStatus.equals("Active"))
                                {
                                    PrefManager prefManager=new PrefManager(getContext());
                                    if (prefManager.isFirstTime()) {


                                        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                        pDialog.setTitleText("Thank You..");
                                        pDialog.setContentText("Dear User you are successfully subscribed to " + plan + " basic plan. Your trial ends in 29 Days 23 Hours.");
                                        pDialog.setCancelable(false);
                                        pDialog.show();
                                        prefManager.setFirstTime(false);
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

    void updateDays(final int days_left) {

        String url = getString(R.string.url) + "update_days_left.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {

                } else {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",seller_id);
                params.put("days_left",String.valueOf(days_left));

                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }


}

