package com.example.khareedlo.Product.AddMobile.SelectBrand;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.khareedlo.Product.AddMobile.SelectModel.Select_Model_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Storage.Select_Storage_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Product.Category_Product_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Select_Brand_Fragment extends Fragment {
SweetAlertDialog pDialog;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    ImageView imageViewhome;
    FloatingActionButton floatingActionButton;
    int key;

    RecyclerView recyclerView;
    ArrayList<BrandClass> arrayList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.select_brand_fragment, container, false);

        imageViewhome=view.findViewById(R.id.idhomeibrand);
        imageViewbackbtn=view.findViewById(R.id.idbackbrandd);
        recyclerView=view.findViewById(R.id.rvbrands);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle args = getArguments();
        if (args  != null) {
            key = args.getInt("keybtn");
            Toast.makeText(getContext(), "" + key, Toast.LENGTH_SHORT).show();

        }
        String url=getString(R.string.url)+"fetch_brands.php";
        new JsonTask().execute(url);

        BrandAdapter brandAdapter=new BrandAdapter(arrayList,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(brandAdapter);

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
    }

    public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder>{


        Dialog myDialog;
        Context context;
        ArrayList<BrandClass> aList;



        public BrandAdapter(ArrayList<BrandClass> aList, Context context)
        {
            this.aList=aList;
            this.context=context;
        }
        @NonNull
        @Override
        public BrandAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_brand,viewGroup,false);
            return new BrandAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  BrandAdapter.MyViewHolder myViewHolder, final int i) {


            RequestOptions requestOptions= new RequestOptions().error(R.drawable.ic_home_black_24dp);
            myViewHolder.brandname.setText(aList.get(i).getBrand());

            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(getString(R.string.url)+aList.get(i).getImage().replaceAll("\\/","/"))
                    .into(myViewHolder.imageView);

            myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (key==1)
                    {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 1);
                        bundle.putString("brandname",aList.get(i).getBrand());
                        bundle.putString("brandID",aList.get(i).getID());
                        Select_Model_Fragment obj=new Select_Model_Fragment();
                        obj.setArguments(bundle);

                        fragmentTransaction.replace(android.R.id.content, obj);
                        fragmentTransaction.commit();
                    }
                    else{
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 0);
                        bundle.putString("brandname",aList.get(i).getBrand());
                        bundle.putString("brandID",aList.get(i).getID());
                        Select_Model_Fragment obj=new Select_Model_Fragment();
                        obj.setArguments(bundle);
                        fragmentTransaction.replace(android.R.id.content, obj);
                        fragmentTransaction.commit();

                    }
                }
            });




        }
        @Override
        public int getItemCount() {
            return aList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView brandname;
            CardView cardView;
            ImageView imageView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.idrvimageviewbrand);
                brandname=itemView.findViewById(R.id.idrvbrandname);
                cardView=itemView.findViewById(R.id.idcvrvbrand);
            }
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please Wait ...");
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

            try {
                //Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("brands")) {
                    arrayList=new ArrayList<>();

                    JSONArray brands_array = jsonObject.getJSONArray("brands");
                    BrandClass brandClass;

                    for(int i=0;i<brands_array.length();i++)
                    { String brandid,brandName,brandImage;
                        JSONObject brand= brands_array.getJSONObject(i);
                        brandid=brand.getString("id");
                        brandName= brand.getString("brand_name");
                        brandImage= brand.getString("brand_image");

                        brandClass=new BrandClass(brandid,brandName,brandImage);

                        arrayList.add(brandClass);



                    }
                    BrandAdapter obj= new BrandAdapter(arrayList,getContext());
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                    recyclerView.setAdapter(obj);
                }




            }catch (Exception ex)
            {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        // .setContentText(ex.getLocalizedMessage())
                        .setContentText("Some Error Occured")
                        .show();
                if(pDialog.isShowing())
                {
                    pDialog.dismiss();

                }

            }
        }


    }

}
