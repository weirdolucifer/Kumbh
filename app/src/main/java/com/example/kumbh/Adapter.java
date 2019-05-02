package com.example.kumbh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends ArrayAdapter<Road> {

    private int layoutResource;

    public Adapter(Context context, int layoutResource, List<Road> threeStringsList) {
        super(context, layoutResource, threeStringsList);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;



        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Road threeStrings = getItem(position);

        if (threeStrings != null) {
            TextView leftTextView = (TextView) view.findViewById(R.id.topic);
            TextView rightTextView = (TextView) view.findViewById(R.id.valid);
            TextView centreTextView = (TextView) view.findViewById(R.id.mess);
            TextView xViewtView = (TextView) view.findViewById(R.id.road);
            TextView constraint = (TextView) view.findViewById(R.id.constraint);
            Date x = threeStrings.exp;
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str1 = sfd.format(new Date(x.getTime()));
            if (leftTextView != null) {
                leftTextView.setText(threeStrings.Title);
            }
            if (rightTextView != null) {
                rightTextView.setText(str1);
            }
            if (centreTextView != null) {
                centreTextView.setText(threeStrings.Text);
            }
            if (xViewtView != null) {
                xViewtView.setText(threeStrings.Road_name);
            }
            if (constraint != null) {
                constraint.setText(threeStrings.Constraint);
            }
        }

        return view;
    }
}
