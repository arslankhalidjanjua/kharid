package com.example.khareedlo.Product.AddMobile.Select_Storage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.SelectModel.ModelClass;
import com.example.khareedlo.Product.AddMobile.Select_Color.Select_Color_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Select_Storage_Fragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    ImageView imageViewhome;

    SweetAlertDialog pDialog;
    int key;
    String ModelName,brandName,ModelID,brandID;
    RecyclerView recyclerView;
    ArrayList<StorageClass> arrayList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.select_storage_fragment, container, false);

        imageViewhome=view.findViewById(R.id.idhomeistorage);
        imageViewbackbtn=view.findViewById(R.id.idbackstorage);
        recyclerView=view.findViewById(R.id.rvstorage);
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
            ModelID= args.getString("modelID");
            brandID= args.getString("brandID");

            Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();


        }
        Toast.makeText(getContext(), "model: "+ModelName, Toast.LENGTH_SHORT).show();
        String url=getString(R.string.url)+"fetch_storages.php?getData=";
        new JsonTask().execute(url+ModelName);




        StorageAdapter brandAdapter=new StorageAdapter(arrayList,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
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






    public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.MyViewHolder>{


        Dialog myDialog;
        Context context;
        ArrayList<StorageClass> aList;



        public StorageAdapter(ArrayList<StorageClass> aList, Context context)
        {
            this.aList=aList;
            this.context=context;
        }
        @NonNull
        @Override
        public StorageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_storage,viewGroup,false);
            return new StorageAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  StorageAdapter.MyViewHolder myViewHolder, final int i) {



            myViewHolder.storage.setText(aList.get(i).getStorage());
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (key==1)
                    {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 1);
                        bundle.putString("brandID",brandID);
                        bundle.putString("modelID",ModelID);

                        bundle.putString("brandname",brandName);
                        bundle.putString("modelofbrand",ModelName);
                        bundle.putString("storage",aList.get(i).getStorage());
                        bundle.putString("storageID",aList.get(i).getId());
                        Select_Color_Fragment obj=new Select_Color_Fragment();
                        obj.setArguments(bundle);

                        fragmentTransaction.replace(android.R.id.content, obj);
                        fragmentTransaction.commit();
                    }
                    else{
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 0);
                        bundle.putString("brandID",brandID);
                        bundle.putString("modelID",ModelID);

                        bundle.putString("brandname",brandName);
                        bundle.putString("modelofbrand",ModelName);
                        bundle.putString("storage",aList.get(i).getStorage());
                        bundle.putString("storageID",aList.get(i).getId());

                        Select_Color_Fragment obj=new Select_Color_Fragment();
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

            TextView storage;
            LinearLayout linearLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                storage=itemView.findViewById(R.id.idrvstoragetv);
                linearLayout=itemView.findViewById(R.id.idllrvstorage);
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
                if(jsonObject.has("MyData")) {
                    arrayList=new ArrayList<>();

                    JSONArray storages_array = jsonObject.getJSONArray("MyData");
                    StorageClass storageClass;

                    for(int i=0;i<storages_array.length();i++)
                    { String storageid,storagename;
                        JSONObject model= storages_array.getJSONObject(i);
                        storageid=model.getString("id");
                        storagename= model.getString("storage");

                        storageClass=new StorageClass(storageid,storagename);

                        arrayList.add(storageClass);



                    }
                    StorageAdapter obj= new StorageAdapter(arrayList,getContext());
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
