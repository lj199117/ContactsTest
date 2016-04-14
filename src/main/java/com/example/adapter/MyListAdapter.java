package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.contactstest.R;
import com.example.pojo.Contacts;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14 0014.
 */
public class MyListAdapter extends ArrayAdapter<Contacts> {

    private Context context;
    private int resource;
    public MyListAdapter(Context context, int resource, List<Contacts> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contacts contacts = getItem(position);
        View view = null;
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resource,null,false);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView number = (TextView)view.findViewById(R.id.number);
            viewHolder.name = name;
            viewHolder.number = number;
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.name.setText(contacts.getName());
        viewHolder.number.setText(contacts.getNumber());
        return view;

    }

    class ViewHolder{
        TextView name;
        TextView number;
    }
}
