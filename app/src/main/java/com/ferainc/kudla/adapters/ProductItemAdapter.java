package com.ferainc.kudla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferainc.kudla.itemclasses.ContactItem;
import com.ferainc.kudla.itemclasses.ProductItem;
import com.ferainc.kudla.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mehaboobmohamed on 11/09/2016.
 */
public class ProductItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProductItem> mDataSource;
    private ArrayList<ProductItem> mDataSourceCopy;

    public ProductItemAdapter(Context context, ArrayList<ProductItem> items) {
        mContext = context;
        mDataSource = items;
        mDataSourceCopy = new ArrayList<ProductItem>(mDataSource);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View rowView = mInflater.inflate(R.layout.buy_results_list_expand, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_title);

// Get subtitle element
        TextView line2TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line2);

        TextView line3TextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_line3);

// Get detail element
        TextView priceTextView =
                (TextView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_price);

// Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(com.ferainc.kudla.R.id.item_list_thumbnail);

        // 1
        ProductItem item = (ProductItem) getItem(position);

// 2
        titleTextView.setText(item.title);

        if (item.category.equals("Vehicles") &&
                (item.subCategory.equals("Cars") || item.subCategory.equals("Bikes") ||
                        item.subCategory.equals("Vans and Jeeps") || item.subCategory.equals("Lorries and Trucks")))
            line2TextView.setText(item.year + "   |   " + item.kms +  "kms   |   " + item.mileage + "kms per litre");
        else
            line2TextView.setText(item.description);

        line3TextView.setText(item.address);
        priceTextView.setText("Rs " + item.price);

        String imageURLs = item.getImageURL0();

        if(imageURLs != null && imageURLs.trim().isEmpty()) {
            //This never gets executed
            imageURLs = "https://firebasestorage.googleapis.com/v0/b/kudla-53b1f.appspot.com/o/images%2F1466831822883.jpg?alt=media&token=5db632d0-1244-4e50-96ad-7caf23ba8329";
        }

// 3
        Picasso.with(mContext).load(imageURLs).placeholder(R.mipmap.noimage).into(thumbnailImageView);

        return rowView;
    }

    public void filter(String text) {
        mDataSource.clear();


        if(text.isEmpty()){
            mDataSource.addAll(mDataSourceCopy);
        } else{
            text = text.toLowerCase();
            for(ProductItem item: mDataSourceCopy){
                if(item.title.toLowerCase().contains(text) || item.description.toLowerCase().contains(text)
                        ||  item.address.toLowerCase().contains(text)){
                    mDataSource.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
