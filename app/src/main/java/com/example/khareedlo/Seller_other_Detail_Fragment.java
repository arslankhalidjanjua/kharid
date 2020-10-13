package com.example.khareedlo;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.example.khareedlo.Verify_Email_Phone.Verify_Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;


public class Seller_other_Detail_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText ed_pass, ed_confirmpass, ed_address,edtcnic,edit_name,edit_salesManNumber,edit_shopname;
    TextView tv_timer;
    private CountDownTimer countDownTimer;
    DateTimeFormatter format;
    DateTime CurrentDate ;
    Spinner city_spinner,province_spinner;
    ArrayList<String> aList,provinceList;
    String city,province,newToken;
    SweetAlertDialog pDialog;
    String encodedString_cnic_front, encodedString_cnic_back, encodedString_bankcheck;
    String shopname,password, username, address,strcnic,email,salesmanNo,phoneNumber;
    Bitmap bitmap;
    int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    ImageView btnBankCheque, btnCnicFront, btnCnicRear;
    ImageView imback;
    Button btnsave;
    private Calendar calendar;
    private SimpleDateFormat dateFormat,timeFormat;
    private String date,time,newCDate;

    EditText edtproprietername,edtshopname,edtsalesmannumber,edtemail;
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_other_detail, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Toast.makeText(getContext(), "We are infragment", Toast.LENGTH_SHORT).show();


        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte [] imageBytes;

        if (requestCode == 1) {
            //TODO: action

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                btnBankCheque.setImageBitmap(selectedImage);
                selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                imageBytes= baos.toByteArray();
                encodedString_bankcheck= Base64.encodeToString(imageBytes, Base64.DEFAULT);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 2) {
            //TODO: action

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                btnCnicFront.setImageBitmap(selectedImage);
                selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                imageBytes= baos.toByteArray();
                encodedString_cnic_front= Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 3) {
            //TODO: action

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                btnCnicRear.setImageBitmap(selectedImage);
                selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                imageBytes= baos.toByteArray();
                encodedString_cnic_back= Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                Toast.makeText(getContext(), ""+newToken, Toast.LENGTH_SHORT).show();

            }
        });

        format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        CurrentDate=DateTime.now();
        CurrentDate=CurrentDate.plusHours(5);
        newCDate=CurrentDate.toString(format);

        dialog=new Dialog(getContext(), R.style.AppTheme_NoActionBar);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM dd yyyy");
        timeFormat = new SimpleDateFormat("hh:mm");



        date = dateFormat.format(calendar.getTime());
        time =  timeFormat.format(calendar.getTime());
        Bundle args = getArguments();
        if (args  != null) {
            username = args.getString("username");
            phoneNumber=args.getString("phone");
        }
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
        btnBankCheque=getView().findViewById(R.id.bank_check_img);
        btnCnicRear=getView().findViewById(R.id.cnic_side_2);
        btnCnicFront=getView().findViewById(R.id.cnic_side_1);

        edtproprietername=getView().findViewById(R.id.edit_proprietername);
        edtshopname=getView().findViewById(R.id.edit_shopname);
        edtsalesmannumber=getView().findViewById(R.id.edit_salesmannumber);
        edtemail=getView().findViewById(R.id.edit_selleremail);

        province_spinner.setOnItemSelectedListener(this);

        load_province();

        edtcnic.addTextChangedListener(new MaskWatcher("#####-#######-#"));

//        load_cities();



        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                username = edtproprietername.getText().toString().trim();
                email = edtemail.getText().toString().trim();
                password = ed_pass.getText().toString().trim();
                shopname = edtshopname.getText().toString().trim();

                address = ed_address.getText().toString().trim();
                city = city_spinner.getSelectedItem().toString().trim();
                province=province_spinner.getSelectedItem().toString().trim();
                strcnic=edtcnic.getText().toString().trim();
                salesmanNo=edtsalesmannumber.getText().toString().trim();
                address=ed_address.getText().toString().trim();
                strcnic=edtcnic.getText().toString().trim();

                if ( TextUtils.isEmpty(edtcnic.getText()) || TextUtils.isEmpty(edtemail.getText()) ||TextUtils.isEmpty(ed_address.getText()) ||TextUtils.isEmpty(edtproprietername.getText()) || TextUtils.isEmpty(edtshopname.getText()) || TextUtils.isEmpty(edtsalesmannumber.getText())  || TextUtils.isEmpty(ed_pass.getText()) || TextUtils.isEmpty(ed_confirmpass.getText()))
                {
                    Toast.makeText(getContext(), "Please input all the data", Toast.LENGTH_SHORT).show();
                }
                else {


                    if (ed_pass.getText().toString().trim().length()<6 || ed_confirmpass.getText().toString().trim().length()<6)
                    {
                        Toast.makeText(getContext(), "Enter at least 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches()) {
                        edtemail.setError("Enter a valid email!");
                        edtemail.requestFocus();
                        return;
                    }


                    if (edtcnic.getText().toString().trim().length()<15)
                    {
                        Toast.makeText(getContext(), "Enter valid CNIC!", Toast.LENGTH_SHORT).show();
                        edtcnic.requestFocus();
                        return;
                    }
                    if (edtsalesmannumber.getText().toString().trim().length()<11)
                    {
                        Toast.makeText(getContext(), "Enter valid mobile number!", Toast.LENGTH_SHORT).show();
                        edtsalesmannumber.requestFocus();
                        return;
                    }

                    String pass = ed_confirmpass.getText().toString();
                    String conPass = ed_pass.getText().toString();
                    if (!pass.equals(conPass)) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Check Passwords")
                                // .setContentText(ex.getLocalizedMessage())
                                .setContentText("Your passwords dont match")
                                .show();

                        return;
                    }

                    send_password();
                    //fn();


            //}

        }
            }
                                   });


        btnBankCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);

            }
        });


        btnCnicFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);

            }
        });


        btnCnicRear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 3);

            }
        });

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Login_Fragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }

    public void send_password() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.show();

//        SharedPreferences sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
//        seller_id=sharedPreferences.getString("seller_id","0");
        final String email=edtemail.getText().toString();

        String url = getString(R.string.url) + "VerifyEmail.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Verification Code Sent to your Email");
                    pDialog.show();

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.email_verify);
                    dialog.setCancelable(true);
                    dialog.show();
                    final EditText editEmailverify=dialog.findViewById(R.id.editCodeE);
                    Button btnVerify=dialog.findViewById(R.id.btnVerify1);
                    tv_timer=dialog.findViewById(R.id.timer);
                    startCountdownTimer();
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!editEmailverify.getText().toString().isEmpty())
                            {
                                countDownTimer.cancel();
                                fn(editEmailverify.getText().toString());
                                //dialog.dismiss();
//                                ActivityCompat.finishAffinity(getActivity());
                            }
                         else
                            {
                                Toast.makeText(getContext(), "Enter Code to proceed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                params.put("email",email);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }

    void fn(final String code) {

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading Data");
        pDialog.setCancelable(false);
        pDialog.show();


        String url = getString(R.string.url) + "register_seller.php";
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
//                pDialog.dismissWithAnimation();
//                if (response.equals("success")) {
//                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
//                    pDialog.setTitleText("Account sent for Approval");
//                    pDialog.setContentText("Will be approved in next 24Hours");
//                    pDialog.show();
//                } else {
//                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
//                    pDialog.setTitleText("Some Error Occured");
//                    pDialog.show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
//                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", username);
//                params.put("password", password);
//                params.put("phone", username);
//                params.put("cnic", strcnic);
////                params.put("bank_cheque", encodedString_bankcheck);
////                params.put("cnic_back", encodedString_cnic_back);
////                params.put("cnic_front", encodedString_cnic_front);
//                params.put("town_code", townCode);
//                params.put("address", address);
//                params.put("city", city);
//                params.put("prorietername", edtproprietername.getText().toString());
//                params.put("shopname", edtshopname.getText().toString());
//                params.put("salesmannumber", edtsalesmannumber.getText().toString());
//                params.put("email", edtemail.getText().toString());
//                return params;
//
//            }
//
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response on Success
                        pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Account sent for Approval");
                    pDialog.setContentText("Will be approved in next 24Hours");
                    pDialog.show();

                    pDialog.dismissWithAnimation();
                    dialog.dismiss();

                    FragmentManager fragmentManager;
                    FragmentTransaction fragmentTransaction;
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, new Login_Fragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (response.equals("code not same")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Verification code is invalid!");
                    pDialog.show();
                } else if (response.equals("email exists")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Email Already Exists!");
                    pDialog.show();
                } else {
                    Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Some Error Occurred");
                    pDialog.show();
                }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                        pDialog.dismiss();
                        Toast.makeText(getContext(), "An error occurred while saving data..", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("phone", phoneNumber);
                params.put("cnic", strcnic);
//                params.put("bank_cheque", encodedString_bankcheck);
//                params.put("cnic_back", encodedString_cnic_back);
//                params.put("cnic_front", encodedString_cnic_front);
                params.put("address", address);
                params.put("city", city);
                params.put("prorietername", username);
                params.put("shopname", shopname);
                params.put("salesmannumber", salesmanNo);
                params.put("email", email);
                params.put("reg_date",date);
                params.put("reg_time", newCDate);
                params.put("code",code);
                params.put("tokenM",newToken);
                return params;
            }

        };
        queue.add(postRequest);
//        Toast.makeText(getContext(), "password: "+ed_pass.getText().toString().trim()+" cnic: "+ edtcnic.getText().toString().trim()+" address: "+ed_address.getText().toString().trim()+" city: "+city+" proprietorName: "+edtproprietername.getText().toString().trim()
//                +" shopname: "+edtshopname.getText().toString().trim()+" salesmanNo: "+edtsalesmannumber.getText().toString().trim()+" email: "+edtemail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), ""+phoneNumber, Toast.LENGTH_SHORT).show();
    }

    public void load_cities(String spinnerItem)
    {

        //Toast.makeText(getContext(), "province: "+province, Toast.LENGTH_SHORT).show();
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_cities.php?getData=";
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+spinnerItem, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        try {
                            aList.clear();
                            String t_city,t_province;
                            JSONArray jsonArray=response.getJSONArray("MyData");
                            for (int i=0 ;i<jsonArray.length();i++) {
                                t_city=jsonArray.getJSONObject(i).getString("city_name");
                                aList.add(t_city);

                            }
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, aList);
                            arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            city_spinner.setAdapter(arrayAdapter);
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

    public void load_province()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_provinces.php";
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            aList.clear();
                            String t_city,t_province;
                            JSONArray jsonArray=response.getJSONArray("province");
                            for (int i=0 ;i<jsonArray.length();i++) {
                                t_province=jsonArray.getJSONObject(i).getString("province_name");
                                provinceList.add(t_province);

                            }
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, provinceList);
                            arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                            province_spinner.setAdapter(arrayAdapter);
                            String provinceFromSpinner=province_spinner.getSelectedItem().toString().trim();
                            load_cities(provinceFromSpinner);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String label = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getContext(), "label: "+label, Toast.LENGTH_SHORT).show();
        load_cities(label);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Snackbar.make(getView(), "Time Out ! Request again for verification code.", Snackbar.LENGTH_LONG).show();
              dialog.dismiss();
            }
        }.start();
    }
}

