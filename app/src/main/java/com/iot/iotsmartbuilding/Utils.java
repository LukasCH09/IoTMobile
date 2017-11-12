package com.iot.iotsmartbuilding;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by lukas.bitter on 09.11.2017.
 */

class Utils {

    private static final String TAG = "testResponse";

    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result);
    }

    static void processRequest(final Context ctxt, String ressource, int method,
                               JSONObject jsonValue, final VolleyCallback callback){
        String server = "192.168.1.1";
        String url = server + "/" + ressource;

        String testurl = "https://postman-echo.com/get?test=123";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (method, testurl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "error: " + error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        SingletonRequestQueue singletonRequestQueue = new SingletonRequestQueue(ctxt);
        SingletonRequestQueue.getInstance(ctxt).addToRequestQueue(jsObjRequest);
    }
}
