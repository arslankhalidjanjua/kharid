package com.example.khareedlo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    String message = "";
    String title = "";
    String newCDate,seller_id;
    DateTimeFormatter format;
    DateTime CurrentDate ;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sendTokenToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("remoteMessage", "FROM:" + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("remoteMessage", "Message data: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("remoteMessage", "Mesage body:" + remoteMessage.getNotification().getBody());

        }


        createNotification(remoteMessage);
    }

    private void createNotification(RemoteMessage mRemoteMsg) {
        title = mRemoteMsg.getData().get("title");
        message = mRemoteMsg.getData().get("message");

        Simple_notification_with_title();


    }

    private void Simple_notification_with_title() {

        int randomNo = 1;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        String NOTIFICATION_CHANNEL_ID = "my_channel_01";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setOngoing(false)
                    .setPriority(notificationManager.IMPORTANCE_MAX);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(randomNo, builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setOngoing(false)
                    .setPriority(notificationManager.IMPORTANCE_MAX);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(randomNo, builder.build());
        }
    }

    public void sendTokenToServer(final String s)
    {

        Log.e("NEW_TOKEN", s);
        format= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        CurrentDate= DateTime.now();
        CurrentDate=CurrentDate.plusHours(5);
        newCDate=CurrentDate.toString(format);
        SharedPreferences sharedPreferences= getSharedPreferences("seller_id",Context.MODE_PRIVATE);
        seller_id=sharedPreferences.getString("seller_id","0");

        //Toast.makeText(getContext(), ""+sellerID+newToken, Toast.LENGTH_SHORT).show();
        String url = getString(R.string.url) + "update_token.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    //Toast.makeText(getContext(), "Token updated", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Token not updated", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MessagingService.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",seller_id);
                params.put("token",s);
                params.put("time_up",newCDate);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MessagingService.this);
        requestQueue.add(request);
    }

}

