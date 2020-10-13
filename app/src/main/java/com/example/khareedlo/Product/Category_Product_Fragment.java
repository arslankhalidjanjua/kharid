package com.example.khareedlo.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.SelectUsedOrNewFragmentAcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Category_Product_Fragment extends Fragment {


    ImageView imageViewbackbtn;
    CardView cvmobile,cvwatch,cvtab;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_category_product, container, false);
        imageViewbackbtn=view.findViewById(R.id.idbackcategory);
        cvmobile=view.findViewById(R.id.idcvmob);
        cvwatch=view.findViewById(R.id.idcvwatch);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageViewbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(new Product_Management_Fragment());
            }
        });

        cvmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Mobile", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Select_Used_or_New_Fragment());
                fragmentTransaction.commit();
            }
        });

        cvwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager;
//                FragmentTransaction fragmentTransaction;
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(android.R.id.content, new SelectUsedOrNewFragmentAcc());
//                fragmentTransaction.commit();
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
