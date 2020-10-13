package com.example.khareedlo.Verify_Email_Phone;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khareedlo.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class phonenumber_Fragment extends Fragment {
SweetAlertDialog pDialog;
    EditText phone_ed;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode,otp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phoneno, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btnphone;
        phone_ed= getView().findViewById(R.id.idedittextphoneNO);
        btnphone=getView().findViewById(R.id.idbtnphonenumb);
        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phone_ed.getText()))
                {
                    Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    try {
//                        String url = getString(R.string.url)+"OTP_generation.php?method=phone&contact="+ URLEncoder.encode(phone_ed.getText().toString(),"utf-8");
//                        new JsonTask().execute(url);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            phone_ed.getText().toString().trim(),                     // Phone number to verify
//                            60,                           // Timeout duration
//                            TimeUnit.SECONDS,                // Unit of timeout
//                            Objects.requireNonNull(getActivity()),        // Activity (for callback binding)
//                            mCallback);

                }


            }
        });

    }
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please Wait");
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
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle= new Bundle();
                bundle.putString("phone",phone_ed.getText().toString());
                OTP_Phone_Fragment otp_phone_fragment= new OTP_Phone_Fragment();
                otp_phone_fragment.setArguments(bundle);
                fragmentTransaction.replace(android.R.id.content,otp_phone_fragment );
                fragmentTransaction.commit();

                //Replace on implementation

//            if(result.contains("Success"))
//            {
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//
//                Bundle bundle= new Bundle();
//                bundle.putString("phone",phone_ed.getText().toString());
//                OTP_Phone_Fragment otp_phone_fragment= new OTP_Phone_Fragment();
//                otp_phone_fragment.setArguments(bundle);
//                fragmentTransaction.replace(android.R.id.content,otp_phone_fragment );
//                fragmentTransaction.commit();
//
//
//
//            }
//            else if (result.contains("Error"))
//            {
//                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Error")
//                        // .setContentText(ex.getLocalizedMessage())
//                        .setContentText("Some Error Occured")
//                        .show();
//            }
//            else
//            {
//                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Error")
//                        // .setContentText(ex.getLocalizedMessage())
//                        .setContentText("Unable to get Response from server")
//                        .show();
//            }
//

        }

        private void StartFirebaseLogin() {

            auth = FirebaseAuth.getInstance();
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Toast.makeText(getContext(),"verification completed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(getContext(),"verification failed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCode = s;
                    Toast.makeText(getContext(),"Code sent",Toast.LENGTH_SHORT).show();
                }
            };
        }


    }
}
