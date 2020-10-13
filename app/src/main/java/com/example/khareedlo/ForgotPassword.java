package com.example.khareedlo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword extends Fragment implements  View.OnClickListener {

    SweetAlertDialog pDialog;
    public static final String RESET_PASSWORD_INITIATE = "resPassReq";
    public static final String RESET_PASSWORD_FINISH = "resPass";
    private String code;
    TextInputLayout textInputLayout;
    ImageView imgBack;

    private Button btn_reset;
    private EditText et_email,et_code,et_password;
    private TextView tv_timer;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;

    public ForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_reset=getView().findViewById(R.id.btnReset);
        et_email=getView().findViewById(R.id.editEmailReset);
        et_code=getView().findViewById(R.id.editCodeReset);
        tv_timer=getView().findViewById(R.id.timer);
        et_password=getView().findViewById(R.id.editPasswordReset);
        textInputLayout=getView().findViewById(R.id.textinputdesignid);
        imgBack=getView().findViewById(R.id.imgB);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        tv_timer.setVisibility(View.GONE);
        btn_reset.setOnClickListener(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Login_Fragment login_fragment=new Login_Fragment();
                fragmentTransaction.replace(android.R.id.content,login_fragment);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnReset:

                if(!isResetInitiated) {

                    email = et_email.getText().toString();
                    if (!email.isEmpty()) {
                        send_password();
                    } else {

                        Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    String code = et_code.getText().toString();
                    String password = et_password.getText().toString();

                    if(!code.isEmpty() && !password.isEmpty()){
                        if (et_password.getText().toString().trim().length()<6)
                        {
                            Toast.makeText(getContext(), "Enter at least 6 characters!", Toast.LENGTH_SHORT).show();
                            et_password.requestFocus();
                            return;
                        }
                        else
                        {
                            update_password();
                        }

                    } else {

                        Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }

                }

                break;
        }

    }

    public void send_password() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.show();

//        SharedPreferences sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
//        seller_id=sharedPreferences.getString("seller_id","0");
        final String email=et_email.getText().toString();

        String url = getString(R.string.url) + "Verfication.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Verification Code Sent to your Email");
                    pDialog.show();

                    et_email.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.GONE);
                    et_code.setVisibility(View.VISIBLE);
                    et_password.setVisibility(View.VISIBLE);
                    tv_timer.setVisibility(View.VISIBLE);
                    btn_reset.setText("Change Password");
                    isResetInitiated = true;
                    startCountdownTimer();

                }
                else if (response.equals("email not found"))
                {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Email doesn't exists in our database.");
                    pDialog.show();
                }
                else {
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
    public void update_password() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please Wait..");
        pDialog.setCancelable(false);
        pDialog.show();

//        SharedPreferences sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
//        seller_id=sharedPreferences.getString("seller_id","0");
        final String code=et_code.getText().toString();
        final String password=et_password.getText().toString();
        //Toast.makeText(getContext(), ""+password, Toast.LENGTH_SHORT).show();

        String url = getString(R.string.url) + "ChngPassword.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Password Changed");
                    pDialog.show();

                    pDialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager fragmentManager;
                            FragmentTransaction fragmentTransaction;
                            fragmentManager = ForgotPassword.this.getFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            Login_Fragment login_fragment=new Login_Fragment();
                            fragmentTransaction.replace(android.R.id.content,login_fragment);
                            fragmentTransaction.commit();
                            pDialog.dismissWithAnimation();
                        }
                    });

                    countDownTimer.cancel();
                    isResetInitiated = false;

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
                params.put("coder",code);
                params.put("password",password);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Snackbar.make(getView(), "Time Out ! Request again to reset password.", Snackbar.LENGTH_LONG).show();
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Login_Fragment login_fragment=new Login_Fragment();
                fragmentTransaction.replace(android.R.id.content,login_fragment);
                fragmentTransaction.commit();
            }
        }.start();
    }

}
