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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khareedlo.Product.AddMobile.Select_Color.ColorClass;
import com.example.khareedlo.Product.AddMobile.Select_Color.Select_Color_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_PTA_Approved_OR_NOT.Select_PTA_APPROVED_OR_NOT_Fragment;
import com.example.khareedlo.Product.AddMobile.Select_Used_or_New_Fragment;
import com.example.khareedlo.Seller_Dashboard.Dashboard_Seller_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectColorFragmentAcc extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView imageViewbackbtn;
    ImageView imageViewhome;
    SweetAlertDialog pDialog;
    List<String> currentSelectedItems;
    int key;

    String ModelName,brandName,Storageofdevice,storageID,modelID,brandID,colors;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ColorClass> arrayList=new ArrayList<>();
    FloatingActionButton floatingActionButton;
    PrefManager prefManager;
    String str;
    public SelectColorFragmentAcc() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_color_acc, container, false);

        prefManager=new PrefManager(getContext());
        floatingActionButton=view.findViewById(R.id.idfloatbtn);
        imageViewhome=view.findViewById(R.id.idhomecolors);
        imageViewbackbtn=view.findViewById(R.id.idbackcolors);
        recyclerView=view.findViewById(R.id.rvcolors);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentSelectedItems = new ArrayList<>();

        Bundle args = getArguments();
        if (args  != null){
            key = args.getInt("keybtn");
            brandName= args.getString("brandname");
            ModelName= args.getString("modelofbrand");
            Storageofdevice= args.getString("storage");
            storageID= args.getString("storageID");
            modelID= args.getString("modelID");
            brandID=args.getString("brandID");

            Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();


        }

        String url=getString(R.string.url)+"fetch_colors_access.php?id="+modelID;
        new SelectColorFragmentAcc.JsonTask().execute(url);

        SelectColorFragmentAcc.ColorAdapter colorAdapter=new SelectColorFragmentAcc.ColorAdapter(arrayList, getContext(), new onItemCheckListener() {
            @Override
            public void onItemCheck(ColorClass item) {

            }

            @Override
            public void onItemUncheck(ColorClass item) {

            }

            @Override
            public void onColorCheck(String colorsT) {
                currentSelectedItems.add(colorsT);
                for (int i=0;i<currentSelectedItems.size();i++)
                {
                    StringWriter sw = new StringWriter();
                    sw.append(currentSelectedItems.toString());
                    colors=sw.toString();
                    String formattedString = currentSelectedItems.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();
                    prefManager.setColor(formattedString);
                }
                for (int i = 0; i < currentSelectedItems.size(); i++) {
                    int kk = currentSelectedItems.size();
                    Toast.makeText(getContext(), "splittting: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                    str += currentSelectedItems.get(i);
                    if (i + 1 != currentSelectedItems.size()) {
                        str+= ",";
                    }
                    Toast.makeText(getContext(), "result: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getContext(), "items: "+currentSelectedItems.toString()+"colors: "+colors, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onColorUncheck(String colorU) {
                currentSelectedItems.remove(colorU);
                for (int i=0;i<currentSelectedItems.size();i++)
                {
                    StringWriter sw = new StringWriter();
                    sw.append(currentSelectedItems.toString());
                    colors=sw.toString();
                    String formattedString = currentSelectedItems.toString()
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();
                    prefManager.setColor(formattedString);

                }
                for (int i = 0; i < currentSelectedItems.size(); i++) {
                    int kk = currentSelectedItems.size();
                    Toast.makeText(getContext(), "splittting: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                    str += currentSelectedItems.get(i);
                    if (i + 1 != currentSelectedItems.size()) {
                        str+= ",";
                    }
                    Toast.makeText(getContext(), "result: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(colorAdapter);


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


    }






    public class ColorAdapter extends RecyclerView.Adapter<SelectColorFragmentAcc.ColorAdapter.MyViewHolder> {

        Dialog myDialog;
        Context context;
        ArrayList<ColorClass> aList;


        @NonNull
        private onItemCheckListener onItemCheckListener;

        public ColorAdapter(ArrayList<ColorClass> aList, Context context,@NonNull onItemCheckListener onItemCheckListener)
        {
            this.aList=aList;
            this.context=context;
            this.onItemCheckListener = onItemCheckListener;
        }
        @NonNull
        @Override
        public SelectColorFragmentAcc.ColorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_colors,viewGroup,false);
            return new SelectColorFragmentAcc.ColorAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final  SelectColorFragmentAcc.ColorAdapter.MyViewHolder myViewHolder, final int i) {
            final ColorClass currentItem = aList.get(i);
            myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    myViewHolder.checkBox.setChecked(!myViewHolder.checkBox.isChecked());
                    if (myViewHolder.checkBox.isChecked()) {
                        onItemCheckListener.onItemCheck(currentItem);
                        onItemCheckListener.onColorCheck(currentItem.getColorname());

                    } else {
                        onItemCheckListener.onItemUncheck(currentItem);
                        onItemCheckListener.onColorUncheck(currentItem.getColorname());
                    }
                }
            });

            myViewHolder.colorname.setText(aList.get(i).getColorname());
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, ""+prefManager.getColor(), Toast.LENGTH_SHORT).show();
                    if (key==1)
                    {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("keybtn", 1);
                        bundle.putString("brandname",brandName);
                        bundle.putString("brandID",brandID);
                        bundle.putString("modelofbrand",ModelName);
                        bundle.putString("modelID",modelID);
                        bundle.putString("storage",Storageofdevice);
                        bundle.putString("storageID",storageID);
                        bundle.putString("color",prefManager.getColor());
                        bundle.putString("colorID",aList.get(i).getColorID());
                        SelectPTAApprovedAcc obj=new SelectPTAApprovedAcc();
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
                        bundle.putString("brandID",brandID);
                        bundle.putString("modelofbrand",ModelName);
                        bundle.putString("modelID",modelID);
                        bundle.putString("storage",Storageofdevice);
                        bundle.putString("storageID",storageID);
                        bundle.putString("color",prefManager.getColor());
                        bundle.putString("colorID",aList.get(i).getColorID());
                        SelectPTAApprovedAcc obj=new SelectPTAApprovedAcc();
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

            TextView colorname;
            RelativeLayout linearLayout;
            CheckBox checkBox;
            View itemView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                colorname=itemView.findViewById(R.id.idrvcolorstv);
                linearLayout=itemView.findViewById(R.id.idrvllcolors);
                checkBox=itemView.findViewById(R.id.chkbox);
                checkBox.setClickable(true);
            }
            public void setOnClickListener(View.OnClickListener onClickListener) {
                itemView.setOnClickListener(onClickListener);
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
                if(jsonObject.has("colors")) {
                    arrayList=new ArrayList<>();

                    JSONArray storages_array = jsonObject.getJSONArray("colors");
                    ColorClass colorClass;

                    for(int i=0;i<storages_array.length();i++)
                    { String colorID,colorName;
                        JSONObject model= storages_array.getJSONObject(i);
                        colorID=model.getString("id");
                        colorName= model.getString("name");

                        colorClass=new ColorClass(colorID,colorName);

                        arrayList.add(colorClass);



                    }
                    SelectColorFragmentAcc.ColorAdapter obj= new SelectColorFragmentAcc.ColorAdapter(arrayList, getContext(), new onItemCheckListener() {
                        @Override
                        public void onItemCheck(ColorClass item) {

                        }

                        @Override
                        public void onItemUncheck(ColorClass item) {

                        }

                        @Override
                        public void onColorCheck(String colorsT) {
                            currentSelectedItems.add(colorsT);
                            for (int i=0;i<currentSelectedItems.size();i++)
                            {
                                StringWriter sw = new StringWriter();
                                sw.append(currentSelectedItems.toString());
                                colors=sw.toString();
                                String formattedString = currentSelectedItems.toString()
                                        .replace("[", "")  //remove the right bracket
                                        .replace("]", "")  //remove the left bracket
                                        .trim();
                                prefManager.setColor(formattedString);

                            }
                            for (int i = 0; i < currentSelectedItems.size(); i++) {
                                int kk = currentSelectedItems.size();
                                Toast.makeText(getContext(), "splittting: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                                str += currentSelectedItems.get(i);
                                if (i + 1 != currentSelectedItems.size()) {
                                    str+= ",";
                                }
                                Toast.makeText(getContext(), "result: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onColorUncheck(String colorU) {
                            currentSelectedItems.remove(colorU);
                            for (int i=0;i<currentSelectedItems.size();i++)
                            {
                                StringWriter sw = new StringWriter();
                                sw.append(currentSelectedItems.toString());
                                String formattedString = currentSelectedItems.toString()
                                        .replace("[", "")  //remove the right bracket
                                        .replace("]", "")  //remove the left bracket
                                        .trim();
                                prefManager.setColor(formattedString);

                            }
                            for (int i = 0; i < currentSelectedItems.size(); i++) {
                                int kk = currentSelectedItems.size();
                                Toast.makeText(getContext(), "splittting: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                                str += currentSelectedItems.get(i);
                                if (i + 1 != currentSelectedItems.size()) {
                                    str+= ",";
                                }
                                Toast.makeText(getContext(), "result: "+currentSelectedItems.get(i), Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(getContext(), "items: "+currentSelectedItems.toString()+"colors: "+colors, Toast.LENGTH_SHORT).show();
                        }
                    });

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(obj);
                    //Toast.makeText(getContext(), ""+currentSelectedItems.toString(), Toast.LENGTH_SHORT).show();
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
