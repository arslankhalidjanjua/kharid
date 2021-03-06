package com.example.khareedlo.Orders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.khareedlo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;

public class Fragment_Dispatch_Orders extends Fragment {

    SearchView searchView;
    String seller_id;
    RecyclerView recyclerView;
    ArrayList<OrderClass> aList=new ArrayList<>();
    MoblistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dispatch_orders, container, false);
        recyclerView=view.findViewById(R.id.idrvdispatchorders);
        searchView= view.findViewById(R.id.idsearchview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");
       loadOrders();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<OrderClass> filteredlist=new ArrayList<>();

                for (OrderClass item :aList) {
                    String restname=item.getModelname().toLowerCase();
                    String bname=item.getBrandname().toLowerCase();
                    if (restname.contains(newText) || bname.contains(newText)) {
                        filteredlist.add(item);
                    }
                }
                adapter=new MoblistAdapter(filteredlist,getContext());
                recyclerView.setAdapter(adapter);
                return false;
            }
        });


    }

    public class MoblistAdapter extends RecyclerView.Adapter<MoblistAdapter.MyViewHolder>{

        Dialog myDialog;
        Context context;
        ArrayList<OrderClass> aList;

        public MoblistAdapter(ArrayList<OrderClass> aList, Context context)
        {
            this.aList=aList;
            this.context=context;
        }
        @NonNull
        @Override
        public MoblistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_dispatch_orders,viewGroup,false);
            return new MoblistAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  MoblistAdapter.MyViewHolder myViewHolder, final int i) {

            myViewHolder.tvmenuname.setText(aList.get(i).getBrandname());
            myViewHolder.tvdescription.setText(aList.get(i).getModelname());
            myViewHolder.tvprice.setText("Rs:"+aList.get(i).getPrice());

//            byte[] decodedString = Base64.decode(aList.get(i).getImage(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            RequestOptions requestOptions=new RequestOptions().error(R.drawable.ic_launcher_foreground);
            Glide.with(context).setDefaultRequestOptions(requestOptions).load(getString(R.string.url)+aList.get(i).getImage().replaceAll("\\/","/"))
                    .into(myViewHolder.imageView);

//            myViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            //              @Override
//                public void onClick(View v) {
            //                   Bundle bundle=new Bundle();
//                    bundle.putString("model_id",aList.get(i).getId());
//                    bundle.putString("PTA",aList.get(i).getIs_ptaapproved());
//                    bundle.putString("Brand",aList.get(i).getBrandname());
//                    bundle.putString("Model",aList.get(i).getModelname());
//                    bundle.putString("color",aList.get(i).getColor());
//                    bundle.putString("Price",aList.get(i).getPrice());


            //            }
            //       });

        }
        @Override
        public int getItemCount() {
            return aList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tvmenuname,tvprice,tvdescription,tvcat,tvColor;
            CardView cv;
            ImageView imageView,imgdell,imgupdate;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                tvColor=itemView.findViewById(R.id.idrvmenucolor);
                tvmenuname=itemView.findViewById(R.id.idtrvmenuname);
                tvprice=itemView.findViewById(R.id.idrvmenuprice);

                tvdescription=itemView.findViewById(R.id.idrvmenudescription);
                imageView=itemView.findViewById(R.id.idrvimageviewmenulist);
                cv=itemView.findViewById(R.id.menucardId);
            }
        }
    }

    public void loadOrders()
    {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"fetch_orders_seller.php?id="+seller_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            aList.clear();
                            OrderClass orderClass;
                            JSONArray jsonArray=response.getJSONArray("orders");
                            for (int i=0 ;i<jsonArray.length();i++)
                            {
                                String id,brandname,color,modelname,storage,used_status,price,is_pta_approved,image,order_status;
                                String model_id,brand_id;
                                JSONObject product= jsonArray.getJSONObject(i);
                                Log.i(TAG, "onResponse: "+product);
                                id=product.getString("id");
                                brandname= product.getString("brand_name");
                                color=product.getString("color");
                                modelname= product.getString("model_name");
                                storage= product.getString("storage");
                                price= product.getString("price");
                                // used_status= product.getString("used_status");
                                is_pta_approved=product.getString("pta_approved_status");
                                image=product.getString("image");
                                order_status=product.getString("order_status");

                                if(order_status.contains("Dispatched")) {

                                orderClass=new OrderClass(id,brandname,modelname,color
                                        ,storage,price,image,is_pta_approved,1,order_status);

                                aList.add(orderClass);
                                }
                            }

                             adapter = new MoblistAdapter(aList, getContext());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }


}
