package com.example.khareedlo.Orders;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.khareedlo.Dashboard;
import com.example.khareedlo.Login_Fragment;
import com.example.khareedlo.R;


public class fragment_orders extends Fragment {
    CardView cvapproved,cvpending,cvdispatch;
    ImageView imageViewbackbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment_orders, container, false);
        // Inflate the layout for this fragment
        cvapproved=view.findViewById(R.id.idcvapproved);
        cvpending=view.findViewById(R.id.idcvpending);
        cvdispatch=view.findViewById(R.id.idcvdispatch);
        imageViewbackbtn=view.findViewById(R.id.idbackorder);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Dashboard());
            }
        });

        cvapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage(new Fragment_Approved_Orders());
            }
        });
        cvpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage(new Fragment_Pending_Orders());
            }
        });
        cvdispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage(new Fragment_Dispatch_Orders());
            }
        });
    }


    private boolean loadPage(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.idframelayoutproduct, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
