package com.example.khareedlo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.iwgang.countdownview.CountdownView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionMain extends Fragment {

    public SubscriptionMain() {
        // Required empty public constructor
    }
    Date currentTime;
    private Calendar calendar;
    private SimpleDateFormat dateFormat,timeFormat;
    private String date,time;
CardView btnMonth,btnYear,btnQuarter;
    TextView txtMain,txtOld,txtNew,txtOff;
    String plan="monthly";
    ImageView imgBack,tick1,tick2,tick3;
    Button txtContinue;
    String method,planN,planStatus,status,approved_on;

    //CountdownView mCvCountdownView;
    long timeRemained;
    PrefManager prefManager;
    String REgDAte,seller_id;
    Date dt1;
    DateTime startDate;
    TextView txtDay,txtH;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar = Calendar.getInstance();
        seller_id = getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id", "0");
        prefManager = new PrefManager(getContext());
        //mCvCountdownView = (CountdownView)getView().findViewById(R.id.countdownView);
        btnMonth = getView().findViewById(R.id.btnMonthly);
        btnYear = getView().findViewById(R.id.btnYear);
        btnQuarter = getView().findViewById(R.id.btnQuarter);
        txtMain = getView().findViewById(R.id.txtPlanName);
        txtOld = getView().findViewById(R.id.txtPriceOld);
        txtNew = getView().findViewById(R.id.txtPrice);
        txtContinue = getView().findViewById(R.id.txtCont);
        imgBack = getView().findViewById(R.id.idback);
        tick1 = getView().findViewById(R.id.imgTick1);
        tick2 = getView().findViewById(R.id.imgTick2);
        tick3 = getView().findViewById(R.id.imgTick3);
        txtDay=getView().findViewById(R.id.txtDay);
        txtH=getView().findViewById(R.id.txtH);
        txtOff=getView().findViewById(R.id.txtPriceOff);

        tick3.bringToFront();
        tick2.bringToFront();
        tick1.bringToFront();


        load_plan("Monthly");
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


        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_plan("Monthly");
                plan = "monthly";
                tick1.setVisibility(View.VISIBLE);
                tick2.setVisibility(View.INVISIBLE);
                tick3.setVisibility(View.INVISIBLE);
            }
        });
        btnQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_plan("Quarterly");
                plan = "quarterly";
                tick1.setVisibility(View.INVISIBLE);
                tick2.setVisibility(View.VISIBLE);
                tick3.setVisibility(View.INVISIBLE);
            }
        });
        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_plan("Yearly");
                plan = "yearly";
                tick1.setVisibility(View.INVISIBLE);
                tick2.setVisibility(View.INVISIBLE);
                tick3.setVisibility(View.VISIBLE);
            }
        });

        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtDay.getText().toString().equals("0"))
                {
                    updateStatus();
                    FragmentManager fragmentManager;
                    FragmentTransaction fragmentTransaction;
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
                    Bundle bundle = new Bundle();
                    bundle.putString("plan", plan);
                    subscriptionPlan.setArguments(bundle);
                    fragmentTransaction.replace(android.R.id.content, subscriptionPlan);
                    fragmentTransaction.commit();
                }
                else
                {
                    final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE).setContentText("Your subscription/Trial is not finished yet..");
                    progressDialog.show();
                }


            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Fragment_More()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    }
    @Override
    public void onStop() {
        super.onStop();

//        Toast.makeText(getContext(), "time:"+mCvCountdownView.getRemainTime()+"day: "+mCvCountdownView.getDay(), Toast.LENGTH_SHORT).show();
//        prefManager.setTime(mCvCountdownView.getRemainTime());
    }

    public void load_plan(String plan)
    {

        //Toast.makeText(getContext(), "province: "+province, Toast.LENGTH_SHORT).show();
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_plan.php?getData=";
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+plan, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        try {
                            String planName;
                            int discount, origPrice;
                            JSONArray jsonArray=response.getJSONArray("MyData");
                            for (int i=0 ;i<jsonArray.length();i++) {
                                planName=jsonArray.getJSONObject(i).getString("plan_name");
                                discount=jsonArray.getJSONObject(i).getInt("discount");
                                origPrice=jsonArray.getJSONObject(i).getInt("orig_price");
                                txtOld.setPaintFlags(txtOld.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                                txtOld.setText(String.valueOf("RS: "+origPrice));
                                txtMain.setText(planName);
                                String discountS=String.valueOf(discount)+".0";
                                double discountN=Double.parseDouble(discountS);
                                double discounted_price=(origPrice - (origPrice * (discountN/100.0)));
                                //Toast.makeText(getContext(), ""+discounted_price+"orig: "+origPrice+"discount: "+discount, Toast.LENGTH_SHORT).show();
                                txtNew.setText(String.valueOf("RS: "+discounted_price));
                                txtOff.setText(String.valueOf("OFF: "+discount+"%"));
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

    void updateStatus() {

        String url = getString(R.string.url) + "update_plan_status.php";
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
                params.put("plan_status","Expired");

                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

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
