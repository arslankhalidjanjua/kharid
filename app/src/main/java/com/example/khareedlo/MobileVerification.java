package com.example.khareedlo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hbb20.CountryCodePicker;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MobileVerification extends AppCompatActivity {
SweetAlertDialog pDialog;
    ImageView imgBack;
    TextView ccp;
    Button nextIcon;
    EditText mobile_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobileverification);

        imgBack = findViewById(R.id.imgBack);
        ccp = findViewById(R.id.ccp);
        nextIcon = findViewById(R.id.nextIcon);
        mobile_no = findViewById(R.id.mobile_no);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAffinity(MobileVerification.this);
            }
        });
        nextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = ccp.getText().toString() + mobile_no.getText().toString();
                if (mobile_no.getText().toString().equals("") || mobile_no.getText().toString().equals(null)) {

                    //Dialogs.errorDialog(Register.this, getString(R.string.oops), "Enter your mobile number to proceed!", true, false, "", getString(R.string.ok2), null);
                    pDialog = new SweetAlertDialog(MobileVerification.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Enter your mobile number to proceed!");
                    pDialog.setCancelable(true);
                    pDialog.show();

                } else {
                    if (phone.charAt(0)!='+')
                    {
                        phone="+"+phone;
                    }

                    Toast.makeText(MobileVerification.this, ""+phone, Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();

                    Intent intent = new Intent(MobileVerification.this, OTPVerify.class);
                    intent.putExtra("phone", phone);

                    startActivity(intent);
                    ActivityCompat.finishAffinity(MobileVerification.this);
                }
            }
        });
    }
}


