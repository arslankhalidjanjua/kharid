package com.example.khareedlo;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TimeFragment extends Fragment {
    ImageView imgback;
    TextView tvshopopentime,tvshopclosetime;
    Button btnsettime, btnSubscribe;
    SweetAlertDialog pDialog;
    String seller_id,opening_time,closing_time,off_day;
    boolean opset=false;
    boolean clset=false;
    EditText editOffDay;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_time_setting, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvshopopentime=getView().findViewById(R.id.idtvshopopentime);
        tvshopclosetime=getView().findViewById(R.id.idtvshopclosetime);
        btnsettime=getView().findViewById(R.id.idbtnsetshoptime);
        imgback=getView().findViewById(R.id.idbackmoreproduct);
        editOffDay=getView().findViewById(R.id.edOffDay);

        seller_id= getActivity().getPreferences(Context.MODE_PRIVATE).getString("seller_id","0");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new Fragment_More());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvshopopentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setopentime();
            }
        });

        tvshopclosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setclosetime();
            }
        });


        btnsettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!opset || !clset )
                {
                    new SweetAlertDialog( getContext(),SweetAlertDialog.WARNING_TYPE).setContentText("Please Select Time First.").show();
                    return;

                }

                if (editOffDay.getText().toString().isEmpty())
                {
                    new SweetAlertDialog( getContext(),SweetAlertDialog.WARNING_TYPE).setContentText("Enter Off Day").show();
                    return;
                }

                update_time();
            }
        });
    }


    void update_time() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Updating Time");
        pDialog.setCancelable(false);
        pDialog.show();

//        SharedPreferences sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
//        seller_id=sharedPreferences.getString("seller_id","0");
        opening_time=tvshopopentime.getText().toString();
        closing_time=tvshopclosetime.getText().toString();
        off_day=editOffDay.getText().toString();

        String url = getString(R.string.url) + "update_time.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismissWithAnimation();
                if (response.equals("success")) {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Opening and Closing Times Updated");
                    pDialog.show();
                } else {
                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Some Error Occurred.");
                    pDialog.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",seller_id);
                params.put("opening_time",opening_time);
                params.put("closing_time",closing_time);
                params.put("off_day",off_day);

                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        //Toast.makeText(getContext(), " "+opening_time+" "+closing_time+" "+off_day+" "+seller_id, Toast.LENGTH_SHORT).show();

    }

    public void setopentime() {
        Calendar calendar=Calendar.getInstance();

        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvshopopentime.setText(hourOfDay+":"+minute);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

        timePickerDialog.show();
        opset=true;
    }
    public void setclosetime() {
        Calendar calendar=Calendar.getInstance();

        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvshopclosetime.setText(hourOfDay+":"+minute);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

        timePickerDialog.show();
        clset=true;

    }



}
