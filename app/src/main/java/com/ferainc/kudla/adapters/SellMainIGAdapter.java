package com.ferainc.kudla.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ferainc.kudla.R;
import com.squareup.picasso.Picasso;

public class SellMainIGAdapter extends BaseAdapter{
    private Context mContext;
    private int mIconSize;
    private Uri[] uriList;

    public SellMainIGAdapter(Context mContext, int mIconSize, Uri[] uriList) {
        super();
        this.mContext = mContext;
        this.mIconSize = mIconSize;
        this.uriList = uriList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return uriList.length;
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
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mIconSize, mIconSize));
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(uriList[position]).fit().error(R.mipmap.noimage).into(imageView);

        return imageView;

    }
}