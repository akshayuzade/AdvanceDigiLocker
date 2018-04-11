package com.example.hakathon.advaceddigi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

//import static com.example.hakathon.advaceddigi.R.id.imageView;
import static com.example.hakathon.advaceddigi.R.id.imgdocument;

/**
 * Created by Akshay on 10/12/2017.
 */

public class DocumentAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> documenturl;
    ArrayList<String> documentname;

    public DocumentAdapter(Context context, ArrayList<String> documenturl, ArrayList<String> documentname) {
        this.context = context;
        this.documenturl = documenturl;
        this.documentname = documentname;
    }


    @Override
    public int getCount() {
        return documentname.size();
    }


    @Override
    public Object getItem(int position) {
        return documentname.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_item, parent, false);
        TextView tvdocumentname = (TextView) view.findViewById(R.id.tvdocumentname);
        final ImageView imgdocument = (ImageView) view.findViewById(R.id.imgdocument);

        String docname = documentname.get(position);
        String docurl = documenturl.get(position);

        tvdocumentname.setText(docname);

        ImageRequest imageRequest = new ImageRequest(docurl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgdocument.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();

            }
        });
        MySingleton.getInstance(context).addtorequestQueue(imageRequest);

        return view;
    }

}









