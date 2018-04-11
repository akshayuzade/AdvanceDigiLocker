package com.example.hakathon.advaceddigi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akshay on 10/14/2017.
 */

public class IssueeDocumentAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> documenturl;
    ArrayList<String> documentname;

    public IssueeDocumentAdapter(Context context, ArrayList<String> documenturl, ArrayList<String> documentname) {
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
        View view = inflater.inflate(R.layout.issueedoc_item, parent, false);
        TextView issueetvdocname = (TextView) view.findViewById(R.id.issueetvdocname);
        TextView issueetvdocurl = (TextView) view.findViewById(R.id.issueetvdocurl);

        String docname = documentname.get(position);
        String docurl = documenturl.get(position);

        issueetvdocname.setText(docname);
        issueetvdocurl.setText(docurl);
        return view;
    }

}

