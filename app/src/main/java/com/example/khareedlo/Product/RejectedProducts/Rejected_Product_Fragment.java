package com.example.khareedlo.Product.RejectedProducts;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.khareedlo.Product.AddMobile.SelectBrand.BrandClass;
import com.example.khareedlo.Product.Product_Management_Fragment;
import com.example.khareedlo.R;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;

public class Rejected_Product_Fragment extends Fragment {


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SweetAlertDialog pDialog;
    ImageView imgckrejectdproduct,imghomereject;
    String seller_id;

    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<RejectedProducts_Class> arrayList=new ArrayList<>();
    rejectedProductAdapter obj;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.rejected_products_fragment, container, false);

        searchView= view.findViewById(R.id.idsearchview);
       recyclerView=view.findViewById(R.id.rvrejected);
        imgckrejectdproduct=view.findViewById(R.id.idbackrejectproduct);
        imghomereject=view.findViewById(R.id.idhomerejectproducts);;
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id= sharedPreferences.getString("seller_id","0");

        String url= getString(R.string.url)+"fetch_rejected_products.php?id="+seller_id;
        new JsonTask().execute(url);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<RejectedProducts_Class> filteredlist=new ArrayList<>();

                for (RejectedProducts_Class item :arrayList) {
                    String restname=item.getModelname().toLowerCase();
                    if (restname.contains(newText)) {
                        filteredlist.add(item);
                    }
                }
                obj=new rejectedProductAdapter(filteredlist,getContext());
                recyclerView.setAdapter(obj);
                return false;
            }
        });

        imghomereject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
            }
        });

        imgckrejectdproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Dashboard_Seller_Fragment());
                fragmentTransaction.commit();
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
    public class rejectedProductAdapter extends RecyclerView.Adapter<rejectedProductAdapter.MyViewHolder>{


        Dialog myDialog;
        Context context;
        ArrayList<com.example.khareedlo.Product.RejectedProducts.RejectedProducts_Class> aList;



        public rejectedProductAdapter(ArrayList<RejectedProducts_Class> aList, Context context)
        {
            this.aList=aList;
            this.context=context;
        }
        @NonNull
        @Override
        public rejectedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_rejected_products,viewGroup,false);
            return new rejectedProductAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  rejectedProductAdapter.MyViewHolder myViewHolder, final int i) {



            myViewHolder.brandname.setText(aList.get(i).getBrandname());
            myViewHolder.tvmodel.setText(aList.get(i).getModelname());
            myViewHolder.tvstorage.setText(aList.get(i).getStorage());
            myViewHolder.tvcolor.setText(aList.get(i).getColor());
            myViewHolder.tvprice.setText("Price : "+aList.get(i).getPrice());
            myViewHolder.used_status.setText(aList.get(i).getUsed_status());
            myViewHolder.is_pta_approved.setText("PTA Approval: "+aList.get(i).getIs_pta_approved());

            RequestOptions requestOptions= new RequestOptions().error(R.drawable.ic_home_black_24dp);
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(getString(R.string.url)+aList.get(i).getImage().replaceAll("\\/","/"))
                    .into(myViewHolder.imageView);

            myViewHolder.icondell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(TAG, "Iiiidddddddd: "+aList.get(i).getId());
                    Log.i(TAG, "onClick: "+aList.get(i).getUsed_status());
                    final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
                    progressDialog.show();
                    String url= getString(R.string.url)+"delete_seller_product.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                            String url= getString(R.string.url)+"fetch_rejected_products.php?id="+seller_id;
                            new JsonTask().execute(url);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            //  Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE).setContentText(error.getMessage())
                                    .setTitleText("Some Error Occured").show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id",aList.get(i).getId());
                            params.put("status",aList.get(i).getUsed_status());
                            return params;

                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(request);
                }
            });

        }
        @Override
        public int getItemCount() {
            return aList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView brandname,tvmodel,tvprice,tvstorage,tvcolor,is_pta_approved,used_status;

            ImageView imageView,icondell;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                icondell=itemView.findViewById(R.id.idimdellproduct);
                brandname=itemView.findViewById(R.id.idtvabrand);
                tvmodel=itemView.findViewById(R.id.idtvamodel);
                tvstorage=itemView.findViewById(R.id.idtvastorage);
                tvcolor=itemView.findViewById(R.id.idtvacolor);
                tvprice=itemView.findViewById(R.id.idtvaprice);
                is_pta_approved=itemView.findViewById(R.id.id_pta_approved);
                used_status=itemView.findViewById(R.id.tv_used_status);
                imageView=itemView.findViewById(R.id.imageview);



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
                if(jsonObject.has("rejected_products")) {
                    arrayList=new ArrayList<>();

                    JSONArray rejected_products = jsonObject.getJSONArray("rejected_products");
                    RejectedProducts_Class rejectedProducts_class;

                    for(int i=0;i<rejected_products.length();i++)
                    { String id,brandname,color,modelname,storage,used_status,price,is_pta_approved,image;
                        JSONObject product= rejected_products.getJSONObject(i);
                        id=product.getString("id");
                        brandname= product.getString("brand_name");
                        color=product.getString("color_name");
                        modelname= product.getString("model_name");
                        storage= product.getString("storage_name");
                        price= product.getString("price");
                        used_status= product.getString("used_status");
                        is_pta_approved=product.getString("pta_approved_status");
                        image=product.getString("image");

                        rejectedProducts_class=new RejectedProducts_Class( id,brandname,  modelname,  storage, color,  price,used_status,is_pta_approved,image);

                        arrayList.add(rejectedProducts_class);



                    }
                     obj= new rejectedProductAdapter(arrayList,getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
