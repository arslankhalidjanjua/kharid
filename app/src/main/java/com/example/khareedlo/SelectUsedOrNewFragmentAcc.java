package com.example.khareedlo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.khareedlo.Product.AddMobile.SelectBrand.Select_Brand_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectUsedOrNewFragmentAcc extends Fragment {
    ImageView imageViewbackbtn;
    ImageView imageViewhome;
    Button btnused,btnnew;
    public SelectUsedOrNewFragmentAcc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_used_or_new_acc, container, false);
        imageViewbackbtn=view.findViewById(R.id.idbacktype);
        imageViewhome=view.findViewById(R.id.idhometype);

        btnused=view.findViewById(R.id.idbtnused);
        btnnew=view.findViewById(R.id.idbtnnew);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("keybtn", 0);
                SelectBrandFragmentAcc select_brand_fragment=new SelectBrandFragmentAcc();
                select_brand_fragment.setArguments(bundle);

                fragmentTransaction.replace(android.R.id.content, select_brand_fragment);
                fragmentTransaction.commit();

            }
        });


        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("keybtn", 1);
                SelectBrandFragmentAcc select_b_m_v_c_my_fragment=new SelectBrandFragmentAcc();
                select_b_m_v_c_my_fragment.setArguments(bundle);

                fragmentTransaction.replace(android.R.id.content, select_b_m_v_c_my_fragment);
                fragmentTransaction.commit();

            }
        });


        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });

        imageViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });


    }
}
