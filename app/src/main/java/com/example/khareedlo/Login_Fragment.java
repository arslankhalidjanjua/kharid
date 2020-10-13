package com.example.khareedlo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerWebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.example.khareedlo.Verify_Email_Phone.Verify_Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login_Fragment extends Fragment {

    SweetAlertDialog pDialog;
    TextView tvRegister;
    SharedPreferences sharedPreferences;
    Button btnlogin;
    EditText ed_username,ed_password;
    TextView txtForgot;
    String newToken;
    DateTimeFormatter format;
    DateTime CurrentDate ;
    private String newCDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.fragment_login, container, false);
        tvRegister=view.findViewById(R.id.idregistertextview1);
        btnlogin=view.findViewById(R.id.btnloginid);
        txtForgot=view.findViewById(R.id.txtForgot);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        CurrentDate= DateTime.now();
        CurrentDate=CurrentDate.plusHours(5);
        newCDate=CurrentDate.toString(format);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
            }
        });

        ed_password=getView().findViewById(R.id.editPassword);
        ed_username= getView().findViewById(R.id.editUsername);

        sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ForgotPassword forgot_password=new ForgotPassword();
                fragmentTransaction.replace(android.R.id.content,forgot_password);
                fragmentTransaction.commit();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                Seller_other_Detail_Fragment seller_other_detail_fragment=new Seller_other_Detail_Fragment();
//                fragmentTransaction.replace(android.R.id.content,seller_other_detail_fragment );
//                fragmentTransaction.commit();
                startActivity(new Intent(getContext(),MobileVerification.class));

            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();

                if ( TextUtils.isEmpty(ed_username.getText()) || TextUtils.isEmpty(ed_password.getText()))
                {
                    Toast.makeText(getContext(), "Please input all the data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_username.getText().toString()).matches()) {
                        ed_username.setError("Enter a valid email!");
                        ed_username.requestFocus();
                        return;
                    }
                    String url= getString(R.string.url)+"check_login_seller.php?username="+ed_username.getText().toString().trim()+"&password="+ed_password.getText().toString().trim();
                    url= url.replace(" ","%20");
                    new JsonTask().execute(url);
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
            //Toast.makeText(getContext(), "result: "+result, Toast.LENGTH_SHORT).show();
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            try {
                if (result.contains("unsuccessful")) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Unable to Login")
                            // .setContentText(ex.getLocalizedMessage())
                            .setContentText("Invalid Credentials")
                            .show();
                    return;
                }
                else if(result.contains("Pending")){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Unable to Login")
                            // .setContentText(ex.getLocalizedMessage())
                            .setContentText("Account approval pending by Admin")
                            .show();
                    return;
                }

                //Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                final String status, seller_id, seller_name,seller_city;
                String response[] = result.split(" ");
                status = response[0];
                seller_id = response[1];
                seller_name = response[2];
                seller_city=response[3];
                if (status.equals("Success")) {

                    PrefManager prefManager=new PrefManager(getContext());
                    prefManager.setLogin(true);

                    sharedPreferences.edit().putString("seller_id", seller_id).commit();
                    sharedPreferences.edit().putString("seller_name", seller_name.replace("\n","")).commit();
                    sharedPreferences.edit().putString("seller_city", seller_city).commit();
                    FirebaseApp.initializeApp(getActivity());
                   // sendTokenToServer(FirebaseInstanceId.getInstance().getToken());

                    ////////////////////////////////////////////

                    update_token(seller_id);

                    FragmentManager fragmentManager;
                    FragmentTransaction fragmentTransaction;
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                    fragmentTransaction.commit();

                }


            }catch (Exception ex) {


                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        // .setContentText(ex.getLocalizedMessage())
                        .setContentText("Some Error Occured")
                        .show();
            }
                if(pDialog.isShowing())
                {
                    pDialog.dismiss();

                }

            }


    }
    public void sendTokenToServer(final String s)
    {

        Log.e("NEW_TOKEN", s);
        format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        CurrentDate= DateTime.now();
        CurrentDate=CurrentDate.plusHours(5);
        newCDate=CurrentDate.toString(format);
      //  SharedPreferences sharedPreferences= getS("seller_id",Context.MODE_PRIVATE);
       final String seller_id=getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");

        //Toast.makeText(getContext(), ""+sellerID+newToken, Toast.LENGTH_SHORT).show();
        String url = getString(R.string.url) + "update_token.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    //Toast.makeText(getContext(), "Token updated", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Token not updated", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",seller_id);
                params.put("token",s);
                params.put("time_up",newCDate);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    void update_token(final String sellerID) {
        //Toast.makeText(getContext(), ""+sellerID+newToken, Toast.LENGTH_SHORT).show();
        String url = getString(R.string.url) + "update_token.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    //Toast.makeText(getContext(), "Token updated", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Token not updated", Toast.LENGTH_SHORT).show();
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
                params.put("id",sellerID);
                params.put("token",newToken);
                params.put("time_up",newCDate);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }
}
