package com.example.hakathon.advaceddigi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Hakathon on 10/11/2017.
 */

public class MySingleton {
    public static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;


    private MySingleton(Context con){
        context=con;
        requestQueue=getRequestQueue();
    }




    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context con){
        if(mInstance==null){
            mInstance=new MySingleton(con);
        }
        return mInstance;
    }

    public <T> void addtorequestQueue(Request<T> request){
        requestQueue.add(request);
    }

}
