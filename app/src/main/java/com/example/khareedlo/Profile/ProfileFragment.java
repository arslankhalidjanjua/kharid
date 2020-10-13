package com.example.khareedlo.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.example.khareedlo.Constants;
import com.example.khareedlo.R;
import com.example.khareedlo.Subscription.Subscription;
import com.example.khareedlo.TimeFragment;
import com.example.khareedlo.UpdateProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    ImageView imgedit;
    TextView tvshopname,tvaddress,tvsalemannumb,tvphone,tvcity,tvproprietername,tvopen,tvclose,sellerRate,sellerFollow;
    String seller_id,rating,followers,oneStar,twoStar,threeStar,fourStar,fiveStar;
    AnimateHorizontalProgressBar ProgressBar5,progressBar4,progressBar3,progressBar2,progressBar1;
//    ImageView imgSubscribe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sellerRate=getView().findViewById(R.id.sellerRate);
        sellerFollow=getView().findViewById(R.id.sellerFollow);

        ProgressBar5=getView().findViewById(R.id.animate_progress_bar_5);
        progressBar4=getView().findViewById(R.id.animate_progress_bar_4);
        progressBar3=getView().findViewById(R.id.animate_progress_bar_3);
        progressBar2=getView().findViewById(R.id.animate_progress_bar_2);
        progressBar1=getView().findViewById(R.id.animate_progress_bar_1);

        imgedit=getView().findViewById(R.id.idimgedit);
        tvshopname=getView().findViewById(R.id.idtvshopname);
        tvaddress=getView().findViewById(R.id.idtvaddress);
        tvsalemannumb=getView().findViewById(R.id.idtvsalesmannumber);
        tvphone=getView().findViewById(R.id.idtvphonenumb);
        tvcity=getView().findViewById(R.id.idtvcity);
        tvproprietername=getView().findViewById(R.id.idtvpropname);
        tvopen=getView().findViewById(R.id.idtvopentime);
        tvclose=getView().findViewById(R.id.idtvclose);
// imgSubscribe=getView().findViewById(R.id.idimgsubscribe);

        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");

        //Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();
        getprofile_data();

        ProgressBar5.setMax(100);
        progressBar4.setMax(100);
        progressBar3.setMax(100);
        progressBar2.setMax(100);
        progressBar1.setMax(100);

        getSeller();
        getPercent();



//        imgSubscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(android.R.id.content, new Subscription()).addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new UpdateProfile()).addToBackStack(null);
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

                                tvshopname.setText("Shope Name : "+profileobj.getString("shop_name"));
                                tvphone.setText("Contact Number : " +profileobj.getString("phone"));
                                tvaddress.setText("Address : " + profileobj.getString("address"));
                                tvsalemannumb.setText("Salesman Number : " +profileobj.getString("used_mobile_salesman"));
                                tvcity.setText("City : " +profileobj.getString("city"));
                                tvproprietername.setText("Proprietor Name : " +profileobj.getString("proprietor_name"));
                                tvopen.setText("Shop Opening Time  : " +profileobj.getString("opening_time"));
                                tvclose.setText("Shop Closing Time  : " +profileobj.getString("closing_time"));
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

    private void getSeller() {



        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= Constants.ROOT_URL+"fetch_seller.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("seller");

                            JSONObject seller= jsonArray.getJSONObject(0);

                            rating=seller.getString("rating");
                            followers=seller.getString("followers");
                            sellerRate.setText(String.valueOf(Float.parseFloat(rating)));
                            sellerFollow.setText(followers);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    private void getPercent() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= Constants.ROOT_URL+"fetch_percent.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), ""+response+"seller: "+seller_id, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray=response.getJSONArray("rating");

                            JSONObject seller= jsonArray.getJSONObject(0);

                            oneStar=seller.getString("POne");
                            twoStar=seller.getString("PTwo");
                            threeStar=seller.getString("PThree");
                            fourStar=seller.getString("PFour");
                            fiveStar=seller.getString("PFive");

                            ProgressBar5.setProgress(Integer.parseInt(fiveStar));
                            progressBar4.setProgress(Integer.parseInt(fourStar));
                            progressBar3.setProgress(Integer.parseInt(threeStar));
                            progressBar2.setProgress(Integer.parseInt(twoStar));
                            progressBar1.setProgress(Integer.parseInt(oneStar));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }

}
