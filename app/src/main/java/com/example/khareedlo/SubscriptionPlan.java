package com.example.khareedlo;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Subscription.Subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionPlan extends Fragment {

    public SubscriptionPlan() {
        // Required empty public constructor
    }

String strtext,planTxt="manual";
    TextView txtMain,txtOld,txtNew,txtOff;
    Button txtContinue;
    CardView btnCredit,btnEasypaisa,btnJazz,btnManual;
    ImageView imgBack,tick1,tick2,tick3,tick4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
if (strtext!=null)
    strtext=getArguments().getString("plan");
else
{
    strtext="monthly";
}
        return inflater.inflate(R.layout.fragment_subscription_plan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtOff=getView().findViewById(R.id.txtPriceOff);
        txtMain=getView().findViewById(R.id.txtPlanName);
        txtOld=getView().findViewById(R.id.txtPriceOld);
        txtNew=getView().findViewById(R.id.txtPrice);
        txtContinue=getView().findViewById(R.id.txtCont);
        btnManual=getView().findViewById(R.id.btnManual);
        imgBack=getView().findViewById(R.id.idback);
        tick1=getView().findViewById(R.id.imgTick1);
        tick2=getView().findViewById(R.id.imgTick2);
        tick3=getView().findViewById(R.id.imgTick3);
        tick4=getView().findViewById(R.id.imgTick4);

        tick3.bringToFront();
        tick2.bringToFront();
        tick1.bringToFront();
        tick4.bringToFront();


        if (strtext.equals("monthly"))
        {
            load_plan("Monthly");
        }
        else if (strtext.equals("yearly")){
         load_plan("Yearly");
        }
        else
        {
           load_plan("Quarterly");
        }
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planTxt="manual";

                tick1.setVisibility(View.VISIBLE);
                tick2.setVisibility(View.INVISIBLE);
                tick3.setVisibility(View.INVISIBLE);
                tick4.setVisibility(View.INVISIBLE);

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
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
        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Subscription subscription=new Subscription();
                Bundle bundle= new Bundle();
                bundle.putString("plan1",strtext);
                bundle.putString("price",txtNew.getText().toString());
                subscription.setArguments(bundle);
                fragmentTransaction.replace(android.R.id.content,subscription);
                fragmentTransaction.commit();
            }
        });
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
}
