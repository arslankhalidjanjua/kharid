package com.example.khareedlo.Verify_Email_Phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khareedlo.Login_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.Seller_other_Detail_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.TimeUnit;

public class OTP_Phone_Fragment extends Fragment {
String phone;
    ImageView imback;
    FirebaseAuth auth;
    private String verificationCode,otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    EditText editOTP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_phoneno, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imback=getView().findViewById(R.id.idbackemail);
        editOTP=getView().findViewById(R.id.idedittextphoneno);
        Bundle args = getArguments();
        if (args  != null) {
            phone = args.getString("phone");
        }

        Button btnotpphone;
        btnotpphone=getView().findViewById(R.id.idbtnotpphonenumb);
        btnotpphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        getActivity(),        // Activity (for callback binding)
                        mCallback);

                //StartFirebaseLogin();

                otp=editOTP.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithPhone(credential);


            }
        });


        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Verify_Fragment());
                fragmentTransaction.commit();
            }
        });

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

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FragmentManager fragmentManager;
                            FragmentTransaction fragmentTransaction;
                            fragmentManager = getFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            Seller_other_Detail_Fragment seller_other_detail_fragment=new Seller_other_Detail_Fragment();
                            Bundle bundle= new Bundle();
                            bundle.putString("username",phone);
                            seller_other_detail_fragment.setArguments(bundle);
                            fragmentTransaction.replace(android.R.id.content,seller_other_detail_fragment );
                            fragmentTransaction.commit();

                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(),"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
