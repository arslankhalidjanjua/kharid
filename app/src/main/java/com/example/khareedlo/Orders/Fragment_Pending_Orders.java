package com.example.khareedlo.Orders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.khareedlo.Product.ApprovedProducts.ApprovedProducts_Class;
import com.example.khareedlo.Product.ApprovedProducts.Approved_Product_Fragment;
import com.example.khareedlo.R;

import java.security.KeyStore;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.volley.VolleyLog.TAG;

public class Fragment_Pending_Orders extends Fragment {

    Dialog dialog;
    SearchView searchView;
    RecyclerView recyclerView;
    String seller_id;
    ArrayList<OrderClass> aList=new ArrayList<>();
    MoblistAdapter adapter;
    TextView edtusercontact,editTextuseremail,editTextuseraddress,tvwait,textViewcity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_pending_orders, container, false);
        recyclerView=view.findViewById(R.id.idrvpendingorders);
        searchView= view.findViewById(R.id.idsearchview);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
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


    public class MoblistAdapter extends RecyclerView.Adapter< MoblistAdapter.MyViewHolder>{

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
        public  MoblistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_pending_orders,viewGroup,false);
            return new  MoblistAdapter.MyViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final   MoblistAdapter.MyViewHolder myViewHolder, final int i) {

            myViewHolder.tvmenuname.setText(aList.get(i).getBrandname());
            myViewHolder.tvdescription.setText(aList.get(i).getModelname());
            myViewHolder.tvprice.setText("Rs:"+aList.get(i).getPrice());

            myViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(i);
                    approveOrder(aList.get(i).getId());


                }
            });

            myViewHolder.btnUserDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Bundle bundle=new Bundle();
//                    bundle.putString("order_id",aList.get(i).getId());
                    dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.fragment_user_detail);
                    ImageView imageView=dialog.findViewById(R.id.idimgclosepopup);
                    edtusercontact=dialog.findViewById(R.id.idtvphone);
                    editTextuseremail=dialog.findViewById(R.id.idtvuseremail);
                    editTextuseraddress=dialog.findViewById(R.id.idtvaddressuserdetail);
                    tvwait=dialog.findViewById(R.id.idwaittv);
                    textViewcity=dialog.findViewById(R.id.idtvcity);

                    TextView editTextshortdetail=dialog.findViewById(R.id.idtvshortdetail);
                    TextView edtprice=dialog.findViewById(R.id.idtvprice);

                    edtprice.setText("Price : "+aList.get(i).getPrice());
                    editTextshortdetail.setText(aList.get(i).getBrandname() +" "+aList.get(i).getModelname()+" "+aList.get(i).getStorage());

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url= getString(R.string.url)+"fetch_user_detail.php?id="+aList.get(i).getId();
                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.dismiss();
                                    try {

                                        JSONArray jsonArray=response.getJSONArray("user_details");
                                        Log.i(TAG, "onResponse: "+jsonArray);
                                        for (int i=0 ;i<jsonArray.length();i++)
                                        {

                                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                                            String phonenumb=jsonObject.getString("phone");
                                            String email=jsonObject.getString("email");

                                            tvwait.setText("");
                                            editTextuseremail.setText(email);
                                            edtusercontact.setText(phonenumb);
                                            editTextuseraddress.setText("");
                                            textViewcity.setText("");


                                        }


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


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

              RequestOptions requestOptions=new RequestOptions().error(R.drawable.ic_launcher_foreground);
            Glide.with(context).setDefaultRequestOptions(requestOptions).load(getString(R.string.url)+aList.get(i).getImage().replaceAll("\\/","/"))
                    .into(myViewHolder.imageView);


        }
        @Override
        public int getItemCount() {
            return aList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tvmenuname,tvprice,tvdescription,tvcat,tvColor;
            CardView cv;
            Button btnAccept,btnUserDetail;
            ImageView imageView,imgupdate;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                btnAccept=itemView.findViewById(R.id.btnacceptid);
                btnUserDetail=itemView.findViewById(R.id.btnuserdetailid);
                tvColor=itemView.findViewById(R.id.idrvmenucolor);
                tvmenuname=itemView.findViewById(R.id.idtrvmenuname);
                tvprice=itemView.findViewById(R.id.idrvmenuprice);

                tvdescription=itemView.findViewById(R.id.idrvmenudescription);
                imageView=itemView.findViewById(R.id.idrvimageviewmenulist);
                cv=itemView.findViewById(R.id.menucardId);
            }
        }
    }


    public void approveOrder(String id)
    {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setContentText("PleaseWait..");

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url= getString(R.string.url)+"approve_order.php?id="+id;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.contains("success"))
                        {


                        }
                        else
                        {
                            new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE).setContentText("There was an Error in Approving item").show();
                            loadOrders();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE).setContentText(error.getMessage()).show();
                        loadOrders();
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);

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
                            Log.i(TAG, "onResponse: "+jsonArray);
                            for (int i=0 ;i<jsonArray.length();i++)
                            {
                                String id,brandname,color,modelname,storage,used_status,price,is_pta_approved,image,order_status;
                                String model_id,brand_id;
                                JSONObject product= jsonArray.getJSONObject(i);
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

                                //                          model_id=product.getString("model_id");
                                //                            seller_id=product.getString("seller_id");
                                //                              seller_name=product.getString("seller_name");
//                                brand_id=product.getString("brand_id");
                                if(order_status.contains("Pending")) {
                                    orderClass = new OrderClass(id, brandname, modelname, color
                                            , storage, price, image, is_pta_approved, 1, order_status);

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
