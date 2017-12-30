package com.ferainc.kudla.adapters;

import com.ferainc.kudla.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactGridAdapter extends BaseAdapter {
    private Context mContext;
    private int mIconSize;
    private final String[] displayStr;
    private final int[] Imageid;

    private int[] imageId = {
            //R.mipmap.ic_movie2, R.mipmap.ic_doctor2, R.mipmap.ic_dentist2,
            R.mipmap.cblue, R.mipmap.ic_doctor2, R.mipmap.ic_dentist2,
            R.mipmap.ic_restaurant2, R.mipmap.ic_rental2, R.mipmap.ic_electrician2,
            R.mipmap.ic_car2, R.mipmap.ic_bike2, R.mipmap.ic_tyre2,
            R.mipmap.ic_tourist2, R.mipmap.ic_lodging2, R.mipmap.ic_resort2,
            R.mipmap.ic_tailor2, R.mipmap.ic_florist2, R.mipmap.ic_salon2,
            R.mipmap.ic_hospital2, R.mipmap.ic_pharmacy2, R.mipmap.ic_atm2,
            R.mipmap.ic_travel2, R.mipmap.ic_school2, R.mipmap.ic_college2
    };

    public ContactGridAdapter(Context mContext, String[] displayStr,int[] Imageid, int mIconSize) {
        super();
        this.mContext = mContext;
        this.displayStr = displayStr;
        this.Imageid = Imageid;
        this.mIconSize = mIconSize;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.contacts_main_grid_single, null);
        } else {
            grid = (View) convertView;
        }

        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        textView.setText(displayStr[position]);
        imageView.setImageResource(Imageid[position]);
        return grid;
    }

}