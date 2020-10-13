package com.example.khareedlo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khareedlo.otp.OnOtpCompletionListener;
import com.example.khareedlo.otp.OtpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OTPVerify extends AppCompatActivity implements OnOtpCompletionListener {
    private OtpView otpView;
    Button btnverify,btnRegister;

    String phoneNumber, username, password, salesmanno, city,shopname, email, address,cnic;
    SweetAlertDialog pDialog;
    CountDownTimer countDownTimer;


    ImageView backArrow;
    String code="";
    TextView tvResend;
    private String phoneVerificationId;
    private Context context;
    private FirebaseAuth fbAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otpverify);

        FirebaseApp.initializeApp(getApplicationContext());
        context=OTPVerify.this;
        fbAuth = FirebaseAuth.getInstance();
        phoneNumber =getIntent().getStringExtra("phone");
//        username=getIntent().getStringExtra("username");
//        shopname=getIntent().getStringExtra("shopname");
//        password=getIntent().getStringExtra("password");
//        email=getIntent().getStringExtra("email");
//        city=getIntent().getStringExtra("city");
//        address=getIntent().getStringExtra("address");
//        salesmanno=getIntent().getStringExtra("saleNo");
//        cnic=getIntent().getStringExtra("cnic");

        btnverify = findViewById(R.id.btnverify);
        btnRegister=findViewById(R.id.button_register_verification);
        otpView = findViewById(R.id.otp_view);
        tvResend=findViewById(R.id.tvResend);
        otpView.setOtpCompletionListener(this);

        setUpVerificatonCallbacks();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Seller_other_Detail_Fragment seller_other_detail_fragment=new Seller_other_Detail_Fragment();
                fragmentTransaction.replace(android.R.id.content,seller_other_detail_fragment );
                fragmentTransaction.commit();
                finishAffinity();
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpVerificatonCallbacks();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        OTPVerify.this,
                        verificationCallbacks,
                        resendToken);
            }
        });

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber+"",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OTPVerify.this,               // Activity (for callback binding)
                verificationCallbacks);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otpView.getText().toString().equals(""))
                {
                    try {
                        PhoneAuthCredential credential =
                                PhoneAuthProvider.getCredential(phoneVerificationId,otpView.getText().toString() );
                        signInWithPhoneAuthCredential(credential);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast toast = Toast.makeText(OTPVerify.this, "Verification Code is wrong", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }

                }else {
                    Toast.makeText(OTPVerify.this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void setUpVerificatonCallbacks() {
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
//                        code= credential.getSmsCode();
//                        Toast.makeText(OtpVerification.this, credential.getSmsCode()+" ", Toast.LENGTH_SHORT).show();

                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {


                        Log.d("responce",e.toString());
                        Toast.makeText(OTPVerify.this, e.getMessage()+" ", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            e.printStackTrace();
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            e.printStackTrace();
                            // SMS quota exceeded
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        phoneVerificationId = verificationId;
                        resendToken = token;
                        Toast.makeText(OTPVerify.this, "Code Sent to ( "+phoneNumber+" )", Toast.LENGTH_SHORT).show();
                        startTimer(1);

                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            countDownTimer.cancel();

                            //Dialogs.succesDialog(OTPVerify.this,"Great!","Verified Successfully!",true,false,"",null,null);
                            //Toast.makeText(OTPVerify.this,"Successfully Verified",Toast.LENGTH_LONG).show();
                            //finish();



//                            pDialog = new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.PROGRESS_TYPE);
//                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                            pDialog.setTitleText("Uploading Data");
//                            pDialog.setCancelable(false);
//                            pDialog.show();
//
//
//                            String url = getString(R.string.url) + "register_seller.php";
//
//                            RequestQueue queue = Volley.newRequestQueue(OTPVerify.this);
//                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                                    new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            // response on Success
//                                            pDialog.dismissWithAnimation();
//                                            if (response.equals("success")) {
//                                                pDialog = new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.SUCCESS_TYPE);
//                                                pDialog.setTitleText("Account sent for Approval");
//                                                pDialog.setContentText("Will be approved in next 24Hours");
//                                                pDialog.show();
////                                                finish();
//                                                FragmentManager fragmentManager;
//                                                FragmentTransaction fragmentTransaction;
//                                                fragmentManager = getSupportFragmentManager();
//                                                fragmentTransaction = fragmentManager.beginTransaction();
//                                                fragmentTransaction.replace(android.R.id.content, new Login_Fragment());
//                                                fragmentTransaction.addToBackStack(null);
//                                                fragmentTransaction.commit();
//
//                                            } else {
//                                                pDialog = new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.ERROR_TYPE);
//                                                pDialog.setTitleText("Some Error Occured");
//                                                pDialog.show();
//                                                finish();
//                                            }
//                                        }
//                                    },
//                                    new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            // error
//                                            error.printStackTrace();
//                                            Log.d("Error.Response", error.toString());
//                                            pDialog.dismiss();
//                                            Toast.makeText(OTPVerify.this, "An error occurred while saving data..", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                            ) {
//                                @Override
//                                protected Map<String, String> getParams() {
//
//                                    Map<String, String> params = new HashMap<String, String>();
//                                    params.put("username", username);
//                                    params.put("password", password);
//                                    params.put("phone", phoneNumber);
//                                    params.put("cnic", cnic);
////                params.put("bank_cheque", encodedString_bankcheck);
////                params.put("cnic_back", encodedString_cnic_back);
////                params.put("cnic_front", encodedString_cnic_front);
//                                    params.put("address", address);
//                                    params.put("city", city);
//                                    params.put("prorietername", username);
//                                    params.put("shopname", shopname);
//                                    params.put("salesmannumber", salesmanno);
//                                    params.put("email", email);
//                                    return params;
//                                }
//
//                            };
//                            queue.add(postRequest);

                            FragmentManager fragmentManager;
                            FragmentTransaction fragmentTransaction;
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            Seller_other_Detail_Fragment seller_other_detail_fragment=new Seller_other_Detail_Fragment();
                            Bundle bundle= new Bundle();
                            bundle.putString("phone",phoneNumber);
                            seller_other_detail_fragment.setArguments(bundle);
                            fragmentTransaction.replace(android.R.id.content,seller_other_detail_fragment );
                            fragmentTransaction.commit();

                            //Toast.makeText(OTPVerify.this, "name: "+username+" country:"+country+" email: "+email+" clientId: "+clientId+" fullname: "+fullname+" password: "+password, Toast.LENGTH_SHORT).show();

                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);

//                            ActivityCompat.finishAffinity(OTPVerify.this);


                            // get the user info to know that user is already login or not

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPVerify.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }


    @Override
    public void onOtpCompleted(String otp) {
        btnverify.setEnabled(true);


//        Toast.makeText(this, "OnOtpCompletionListener called", Toast.LENGTH_SHORT).show();
    }

    private void startTimer(int noOfMinutes) {

        countDownTimer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResend.setEnabled(false);
                tvResend.setText("Resend in "+ millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                //Dialogs.errorDialog(OTPVerify.this,getString(R.string.oops),"Couldn't Verify, Check your mobile number again!",true,false,"",getString(R.string.ok2),null);
                pDialog = new SweetAlertDialog(OTPVerify.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Couldn't Verify! Check your mobile number again.");
                pDialog.show();
                tvResend.setEnabled(true);
                tvResend.setText("Resend");
            }

        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
