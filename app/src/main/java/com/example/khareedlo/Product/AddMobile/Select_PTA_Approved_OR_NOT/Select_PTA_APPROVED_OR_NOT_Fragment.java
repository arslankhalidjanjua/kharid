package com.example.khareedlo.Product.AddMobile.Select_PTA_Approved_OR_NOT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.EnterPrice.Enter_Price_Fragment;
import com.example.khareedlo.Product.AddMobile.SelectBrand.Select_Brand_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Select_PTA_APPROVED_OR_NOT_Fragment extends Fragment {



    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    FloatingActionButton floatingActionButton;
    ImageView imageViewhome;
    int key;
    String brandName,ModelName,Storageofdevice,Colorofdevice,modelID,brandID,colorID,storageID;
     String ptavalue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.select_pta_approved_fragment, container, false);
        imageViewbackbtn=view.findViewById(R.id.idbackpta);
        imageViewhome=view.findViewById(R.id.idhomepta);
        floatingActionButton=view.findViewById(R.id.idfloatbtnpta);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args  != null){
            key = args.getInt("keybtn");
            brandName= args.getString("brandname");
            ModelName= args.getString("modelofbrand");
            Storageofdevice= args.getString("storage");
            storageID= args.getString("storageID");
            modelID= args.getString("modelID");
            Colorofdevice= args.getString("color");
            colorID=args.getString("colorID");
            brandID=args.getString("brandID");
            Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();

        }


        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Select_Used_or_New_Fragment());
                fragmentTransaction.commit();
            }
        });


        imageViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup rg = (RadioGroup) getView().findViewById(R.id.idradiorouppta);
                ptavalue = ((RadioButton) getView().findViewById(rg.getCheckedRadioButtonId()))
                        .getText().toString();
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Toast.makeText(getContext(), ptavalue, Toast.LENGTH_SHORT).show();
                    }
                });
                if (key==1)
                {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("keybtn", 1);
                    bundle.putString("brandname",brandName);
                    bundle.putString("brandID",brandID);
                    bundle.putString("modelofbrand",ModelName);
                    bundle.putString("modelID",modelID);
                    bundle.putString("storage",Storageofdevice);
                    bundle.putString("storageID",storageID);
                    bundle.putString("color",Colorofdevice);
                    bundle.putString("colorID",colorID);
                    bundle.putString("Ptaapproved",ptavalue);
                    Enter_Price_Fragment obj=new Enter_Price_Fragment();
                    obj.setArguments(bundle);
                    fragmentTransaction.replace(android.R.id.content, obj);
                    fragmentTransaction.commit();
                }
                else{
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("keybtn", 0);
                    bundle.putString("brandname",brandName);
                    bundle.putString("brandID",brandID);
                    bundle.putString("modelofbrand",ModelName);
                    bundle.putString("modelID",modelID);
                    bundle.putString("storage",Storageofdevice);
                    bundle.putString("storageID",storageID);
                    bundle.putString("color",Colorofdevice);
                    bundle.putString("colorID",colorID);
                    bundle.putString("Ptaapproved",ptavalue);
                    Enter_Price_Fragment obj=new Enter_Price_Fragment();
                    obj.setArguments(bundle);
                    fragmentTransaction.replace(android.R.id.content, obj);
                    fragmentTransaction.commit();
                }
            }
        });
    }


}
