package com.example.khareedlo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.Select_PTA_Approved_OR_NOT.Select_PTA_APPROVED_OR_NOT_Fragment;
import com.example.khareedlo.Product.Category_Product_Fragment;
import com.example.khareedlo.Product.Product_Management_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;

public class MainActivity extends AppCompatActivity {

    int DELAY = 2000;
    int RESULT_LOAD_IMG=1;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager=new PrefManager(MainActivity.this);
//        FragmentManager fragmentManager;
//        FragmentTransaction fragmentTransaction;
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        Seller_other_Detail_Fragment seller_other_detail_fragment=new Seller_other_Detail_Fragment();
//        Bundle bundle= new Bundle();
//        bundle.putString("username","00923426868856");
//        seller_other_detail_fragment.setArguments(bundle);
//        fragmentTransaction.replace(android.R.id.content,seller_other_detail_fragment );
//        fragmentTransaction.commit();
if (prefManager.isLogin())
{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
    fragmentTransaction.commit();
}
else
{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(android.R.id.content, new Login_Fragment());
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
}


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getSupportFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(android.R.id.content, new Login_Fragment());
//                fragmentTransaction.commit();
//            }
//        }, DELAY);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, requestCode, Toast.LENGTH_SHORT).show();

        //check for null
            if (data != null) {
                super.onActivityResult(requestCode, resultCode, data);
                //                if (requestCode != 1) {
//                    //Toast.makeText(getApplicationContext(), "No Image selected", Toast.LENGTH_LONG).show();
//
//                    Toast.makeText(this, "We are in IF", Toast.LENGTH_SHORT).show();
//
//                } else {
                    //show dialogue with result
                    //showResultDialogue(result.getContents());
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "We are in ELSE", Toast.LENGTH_SHORT).show();

                    Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
                    if (fragment != null) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                //}
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
    }
}
