package com.example.khareedlo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.SelectModel.ModelClass;
import com.example.khareedlo.Product.AddMobile.SelectModel.Select_Model_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Storage.Select_Storage_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectModelFragmentAcc extends Fragment {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        ImageView imageViewbackbtn;
        ImageView imageViewhome;

        SweetAlertDialog pDialog;
        int key;
        String brandName;
        String brandID;

        RecyclerView recyclerView;
        ArrayList<ModelClass> arrayList=new ArrayList<>();
    public SelectModelFragmentAcc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_model_acc, container, false);

        imageViewhome=view.findViewById(R.id.idhomeimodel);
        imageViewbackbtn=view.findViewById(R.id.idbackmodel);
        recyclerView=view.findViewById(R.id.rvmodels);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args  != null){
            key = args.getInt("keybtn");
            brandName=args.getString("brandname");
            brandID= args.getString("brandID");
            Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();


        }

        String url=getString(R.string.url)+"fetch_models_access.php?id="+brandID;
        new SelectModelFragmentAcc.JsonTask().execute(url);
        Toast.makeText(getContext(), "brandId:"+brandID, Toast.LENGTH_SHORT).show();








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

        SelectModelFragmentAcc.ModelAdapter brandAdapter=new SelectModelFragmentAcc.ModelAdapter(arrayList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(brandAdapter);

    }

    public class ModelAdapter extends RecyclerView.Adapter<SelectModelFragmentAcc.ModelAdapter.MyViewHolder>{


        Dialog myDialog;
        Context context;
        ArrayList<ModelClass> aList;



        public ModelAdapter(ArrayList<ModelClass> aList, Context context)
        {
            this.aList=aList;
            this.context=context;
        }
        @NonNull
        @Override
        public SelectModelFragmentAcc.ModelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_models,viewGroup,false);
            return new SelectModelFragmentAcc.ModelAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  SelectModelFragmentAcc.ModelAdapter.MyViewHolder myViewHolder, final int i) {



            myViewHolder.modelname.setText(aList.get(i).getModelname());
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (key==1)
                    {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 1);
                        bundle.putString("brandname",brandName);
                        bundle.putString("modelofbrand",aList.get(i).getModelname());
                        bundle.putString("modelID",aList.get(i).getId());
                        bundle.putString("brandID",brandID);

                        SelectStorageFragmentAcc obj=new SelectStorageFragmentAcc();
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
                        bundle.putString("modelofbrand",aList.get(i).getModelname());
                        bundle.putString("modelID",aList.get(i).getId());
                        bundle.putString("brandID",brandID);

                        SelectStorageFragmentAcc obj=new SelectStorageFragmentAcc();
                        obj.setArguments(bundle);
                        fragmentTransaction.replace(android.R.id.content, obj);
                        fragmentTransaction.commit();

                    }
                }
            });
            //myViewHolder.imageView.setImageDrawable(aList.get(i).getDrawable());

//            RequestOptions requestOptions = new RequestOptions()
//                    .error(R.drawable.ic_launcher_background);
//
//            Glide.with(context)
//                    .setDefaultRequestOptions(requestOptions)
//                    .load(aList.get(i).getSongimage())
//                    .into(myViewHolder.imageView);




        }
        @Override
        public int getItemCount() {
            return aList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView modelname;
            LinearLayout linearLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                modelname=itemView.findViewById(R.id.idrvmodelname);
                linearLayout=itemView.findViewById(R.id.idllrvmpdels);
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
                if(jsonObject.has("models")) {
                    arrayList=new ArrayList<>();

                    JSONArray models_array = jsonObject.getJSONArray("models");
                    ModelClass modelClass;

                    for(int i=0;i<models_array.length();i++)
                    { String modelid,modelname;
                        JSONObject model= models_array.getJSONObject(i);
                        modelid=model.getString("id");
                        modelname= model.getString("name");

                        modelClass=new ModelClass(modelid,modelname);

                        arrayList.add(modelClass);



                    }
                    SelectModelFragmentAcc.ModelAdapter brandAdapter=new SelectModelFragmentAcc.ModelAdapter(arrayList,getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(brandAdapter);
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
