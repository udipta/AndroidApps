package com.example.basicservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MySingleton {
    private RequestQueue requestQueue;
    private final Context ctx;

    MySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void postLog(JSONObject jsonObject){
        String url = "http://fks-analytics-dev.adcuratio.net:8080/events/app/test";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> Log.v("LOG_VOLLEY", "Success"),
                error -> Log.e("LOG_VOLLEY", error.toString())
        );

        // Access the RequestQueue through your singleton class.
        this.addToRequestQueue(jsonObjectRequest);
    }
}

