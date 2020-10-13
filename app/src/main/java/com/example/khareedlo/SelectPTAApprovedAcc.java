package com.example.khareedlo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.EnterPrice.Enter_Price_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectPTAApprovedAcc extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    FloatingActionButton floatingActionButton;
    ImageView imageViewhome;
    int key;
    String brandName,ModelName,Storageofdevice,Colorofdevice,modelID,brandID,colorID,storageID;
    String ptavalue;
    public SelectPTAApprovedAcc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_p_t_a_approved_acc, container, false);
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
                fragmentTransaction.replace(android.R.id.content, new SelectUsedOrNewFragmentAcc());
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
                    EnterPriceFragmentAcc obj=new EnterPriceFragmentAcc();
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
                    EnterPriceFragmentAcc obj=new EnterPriceFragmentAcc();
                    obj.setArguments(bundle);
                    fragmentTransaction.replace(android.R.id.content, obj);
                    fragmentTransaction.commit();
                }
            }
        });
    }
}
