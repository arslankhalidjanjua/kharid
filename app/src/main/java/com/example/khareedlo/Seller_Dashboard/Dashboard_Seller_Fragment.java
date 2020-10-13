package com.example.khareedlo.Seller_Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.khareedlo.Fragment_More;
import com.example.khareedlo.Orders.FramelayoutOrders_Fragment;
import com.example.khareedlo.Orders.fragment_orders;
import com.example.khareedlo.PrefManager;
import com.example.khareedlo.Product.FramelayoutProduct_Fragment;
import com.example.khareedlo.Product.Product_Management_Fragment;
import com.example.khareedlo.Profile.ProfileFragment;
import com.example.khareedlo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Dashboard_Seller_Fragment extends Fragment {

    BottomNavigationView navigationView;
    PrefManager prefManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.dashboard_seller_fragment, container, false);
        loadPage(new FramelayoutProduct_Fragment());
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefManager=new PrefManager(getContext());
        navigationView=getView().findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.idproducttab:
                       // Toast.makeText(getContext(), "Products", Toast.LENGTH_SHORT).show();
                        loadPage(new FramelayoutProduct_Fragment());

                        return true;
                    case R.id.idorders:
                      //  Toast.makeText(getContext(), "order tab", Toast.LENGTH_SHORT).show();
                       loadPage(new FramelayoutOrders_Fragment());
                        return true;
                    case R.id.idprofile:
                       // Toast.makeText(getContext(), "order tab", Toast.LENGTH_SHORT).show();
                        loadPage(new ProfileFragment());
                        return true;
                    case R.id.idmore:
                        // Toast.makeText(getContext(), "order tab", Toast.LENGTH_SHORT).show();
                        loadPage(new Fragment_More());
                        return true;


                }
                return false;
            }
        });
    }

    private boolean loadPage(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ifframesellerdashboard, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}