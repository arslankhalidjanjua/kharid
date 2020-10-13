package com.example.khareedlo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
TextView total,completed,sales;
String seller_id;
    ImageView imgbck;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");

        imgbck=getView().findViewById(R.id.idbackREport);

       // Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();

        total=getView().findViewById(R.id.ordersTotal);
        completed=getView().findViewById(R.id.ordersCompleted);
        sales=getView().findViewById(R.id.totalValue);

        loadTotalOrders();
        loadCompletedOrders();

        imgbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Dashboard());
            }
        });

    }

    public void loadCompletedOrders()
    {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_completed_orders.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
//                            aList.clear();
//                            OrderClass orderClass;
                            JSONArray jsonArray=response.getJSONArray("orders");
                            Log.i(TAG, "onResponse: "+jsonArray);
                            for (int i=0 ;i<jsonArray.length();i++)
                            {
                                String rows,valueSale,brandname,color,modelname,storage,used_status,price,is_pta_approved,image,order_status;
                                String model_id,brand_id;
                                JSONObject product= jsonArray.getJSONObject(i);
                                rows=product.getString("count");
                                valueSale=product.getString("price");
                                completed.setText(rows);
                                sales.setText(valueSale);
                                //Toast.makeText(getContext(), ""+rows, Toast.LENGTH_SHORT).show();
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

    public void loadTotalOrders()
    {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_total_orders.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
//                            aList.clear();
//                            OrderClass orderClass;
                            JSONArray jsonArray=response.getJSONArray("orders");
                            Log.i(TAG, "onResponse: "+jsonArray);
                            for (int i=0 ;i<jsonArray.length();i++)
                            {
                                String rows,brandname,color,modelname,storage,used_status,price,is_pta_approved,image,order_status;
                                String model_id,brand_id;
                                JSONObject product= jsonArray.getJSONObject(i);
                                rows=product.getString("count");
                                total.setText(rows);
                               // Toast.makeText(getContext(), ""+rows, Toast.LENGTH_SHORT).show();
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
}
