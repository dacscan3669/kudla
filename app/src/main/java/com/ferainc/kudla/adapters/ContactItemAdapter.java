package com.ferainc.kudla.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ferainc.kudla.itemclasses.ContactItem;
import com.ferainc.kudla.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mehaboobmohamed on 25/07/2016.
 */
public class ContactItemAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ContactItem> mDataSource;
    private ArrayList<ContactItem> mDataSourceCopy;
    private String mType;
    private String today, tomorrow;

    public ContactItemAdapter(Context context, ArrayList<ContactItem> items, String contactType) {
        mContext = context;
        mDataSource = items;
        mDataSourceCopy = new ArrayList<ContactItem>(mDataSource);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mType = contactType;
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {

        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.contacts_results_list_expand, parent, false);

        TextView titlePrefixTextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_title_prefix);
        TextView titleTextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_title);
        TextView specialityTextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_speciality);
        final TextView line1TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line1);
        TextView line2TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line2);
        TextView line3TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line3);
        TextView line4TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line4);



// Get thumbnail element
        //ImageView thumbnailImageView =
        //      (ImageView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_thumbnail);

        final ContactItem item = (ContactItem) getItem(position);
        titlePrefixTextView.setText(String.valueOf(position+1) + ".");
        titleTextView.setText(item.title);

        specialityTextView.setText(item.speciality);
        if (item.speciality.trim().isEmpty()) {
            specialityTextView.setVisibility(View.GONE);
        }

        if (mType == "Movies") {

            SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
            String currentDate = sdf.format(new Date());
            System.out.println("Date is" + currentDate);
            System.out.println("day0 is" + item.day0);

                if (item.day0.toString().contains(currentDate))
                {
                    today = item.day0;
                    tomorrow = item.day1;
                } else if (item.day1.contains(currentDate))
                {
                    today = item.day1;
                    tomorrow = item.day2;
                } else if (item.day2.contains(currentDate))
                {
                    today = item.day2;
                    tomorrow = item.day3;
                } else if (item.day3.contains(currentDate))
                {
                    today = item.day3;
                    tomorrow = item.day4;
                } else if (item.day4.contains(currentDate))
                {
                    today = item.day4;
                    tomorrow = item.day5;
                } else if (item.day5.contains(currentDate))
                {
                    today = item.day5;
                    tomorrow = item.day6;
                } else if (item.day6.contains(currentDate))
                {
                    today = item.day6;
                    tomorrow = item.day0;
                }

            line1TextView.setText(item.lang);
            line2TextView.setText("Cast:  " + item.cast);
            line3TextView.setText("Today:  " + today);
            line4TextView.setText("Tomorrow:  " + tomorrow);
            rowView.findViewById(R.id.line1_icon).setVisibility(rowView.GONE);
        } else {
            line1TextView.setText(item.contact);
            line2TextView.setText("Address:  " + item.address);
            line3TextView.setText("Distance:  " + item.distance); line3TextView.setVisibility(View.GONE);
            line4TextView.setText("Timings:  " + item.timings);
            if (item.timings.trim().isEmpty()) {
                line4TextView.setVisibility(View.GONE);
            }

            line1TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.fromParts("tel", item.contact, null));
                    mContext.startActivity(callIntent);
                }
            });
        }

        //Picasso.with(mContext).load(recipe.image).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }

    public void filter(String text) {
        mDataSource.clear();


        if(text.isEmpty()){
            mDataSource.addAll(mDataSourceCopy);
        } else{
            text = text.toLowerCase();
            for(ContactItem item: mDataSourceCopy){
                if(item.title.toLowerCase().contains(text) || item.speciality.toLowerCase().contains(text)
                                                           ||  item.address.toLowerCase().contains(text)){
                    mDataSource.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
