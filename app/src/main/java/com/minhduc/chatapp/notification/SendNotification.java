package com.minhduc.chatapp.notification;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.minhduc.chatapp.MainActivity;
import com.minhduc.chatapp.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {
    private String userFcmToken;
    private String message;
    Activity mActivity;


    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAUp_tEJM:APA91bFAQ4ZakypcdhNU-sy2XEuIvvw9ynly3KInMqQLczhBxPO9WwDVbXEnVVfNWFXBSOiqsoc-D3EKjqK31euOLmkqj6EehT7H7l1B4LeDzmpyAdKodj1ydPeDRowonU83VrdNpUQi";

    public SendNotification(String userFcmToken, String message, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.message = message;
        this.mActivity = mActivity;
    }

    public void sendNotificationToUser(){
        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("sender", MainActivity.user.getUsername());
            notiObject.put("message", message);
            mainObj.put("data", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
