package com.ferainc.kudla.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferainc.kudla.FullScreenViewActivity;
import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.R;
import com.ferainc.kudla.Utilities.AppConstant;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.adapters.BuyItemIGAdapter;
import com.ferainc.kudla.interfaces.SecondPageFragmentListener;
import com.ferainc.kudla.itemclasses.ProductItem;

import java.util.ArrayList;

/**
 * Created by mehaboobmohamed on 16/10/2016.
 */
public class BuyItemDetailsFrag extends Fragment {

    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private BuyItemIGAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private TextView title, description, condition, price, year, kms, mileage, fuel, colour, aircon, name, phone, email, address;
    private ImageView phoneIcon;
    static SecondPageFragmentListener secondPageListener;
    private String category, subCategory;
    private ProductItem item;

    public BuyItemDetailsFrag() {
        // Required empty public constructor
    }

    public static BuyItemDetailsFrag createInstance(SecondPageFragmentListener listener) {
        BuyItemDetailsFrag frag = new BuyItemDetailsFrag();
        frag.secondPageListener = listener;
        return frag;
    }

    public void backPressed() {
        secondPageListener.onSwitchToNextFragment(category, subCategory, item, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolBarTitle("BUY");
        Log.d("MyApp", "I am here1");
        View view = inflater.inflate(R.layout.buy_item_details, container, false);

        category = getArguments().getString("CATEGORY", "Vehicles");
        subCategory = getArguments().getString("SUBCATEGORY", "Cars");

        item = (ProductItem) getArguments().getSerializable("ITEM");

        gridView = (GridView) view.findViewById(R.id.grid_view);
        utils = new Utils(this.getContext());

        // Initilizing Grid View
        InitilizeGridLayout();

        String[] stringset = {item.getImageURL0(), item.getImageURL1(), item.getImageURL2(),
                                item.getImageURL3(), item.getImageURL4() };
        for (String str : stringset){
                if(str != null && !str.trim().isEmpty()) {
                    imagePaths.add(str);
                }
            }

        // Gridview adapter
        adapter = new BuyItemIGAdapter(this.getActivity(), imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), FullScreenViewActivity.class);
                i.putExtra("position", position);
                i.putExtra("PATHS", imagePaths);
                view.getContext().startActivity(i);
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        condition = (TextView) view.findViewById(R.id.condition);
        price = (TextView) view.findViewById(R.id.price);
        year = (TextView) view.findViewById(R.id.year);
        kms = (TextView) view.findViewById(R.id.kms);
        mileage = (TextView) view.findViewById(R.id.mileage);
        fuel = (TextView) view.findViewById(R.id.fuel);
        colour = (TextView) view.findViewById(R.id.colour);
        aircon = (TextView) view.findViewById(R.id.aircon);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        phoneIcon = (ImageView) view.findViewById(R.id.phone_icon);
        email = (TextView) view.findViewById(R.id.email);
        address = (TextView) view.findViewById(R.id.address);

        title.setText(item.getTitle());
        description.setText(item.getDescription());
        condition.setText(item.getCondition());
        price.setText(item.getPrice());
        year.setText(item.getYear());
        kms.setText(item.getKms());
        mileage.setText(item.getMileage());
        fuel.setText(item.getFuel());
        colour.setText(item.getColour());
        aircon.setText(item.getAircon());
        name.setText(item.getName());
        phone.setText(item.getPhone());
        email.setText(item.getEmail());
        address.setText(item.getAddress());

        if(item.getPhone().trim().isEmpty()) { phone.setVisibility(view.GONE); phoneIcon.setVisibility(view.GONE);  }
        if(item.getEmail().trim().isEmpty()) { email.setVisibility(view.GONE); }
        if(item.getAddress().trim().isEmpty()) { address.setVisibility(view.GONE); }

        if (category.equals("Vehicles") &&
                (subCategory.equals("Cars") || subCategory.equals("Bikes") ||
                        subCategory.equals("Vans and Jeeps") || subCategory.equals("Lorries and Trucks"))) {
            //do nothing
        } else {
            view.findViewById(R.id.year_label).setVisibility(view.GONE); year.setVisibility(view.GONE);
            view.findViewById(R.id.year_divider).setVisibility(view.GONE);

            view.findViewById(R.id.kms_label).setVisibility(view.GONE); kms.setVisibility(view.GONE);
            view.findViewById(R.id.kms_suffix).setVisibility(view.GONE);
            view.findViewById(R.id.kms_divider).setVisibility(view.GONE);

            view.findViewById(R.id.mileage_label).setVisibility(view.GONE); mileage.setVisibility(view.GONE);
            view.findViewById(R.id.mileage_suffix).setVisibility(view.GONE);
            view.findViewById(R.id.mileage_divider).setVisibility(view.GONE);

            view.findViewById(R.id.fuel_label).setVisibility(view.GONE); fuel.setVisibility(view.GONE);
            view.findViewById(R.id.fuel_label).setVisibility(view.GONE);
            view.findViewById(R.id.fuel_divider).setVisibility(view.GONE);

            view.findViewById(R.id.colour_label).setVisibility(view.GONE); colour.setVisibility(view.GONE);
            view.findViewById(R.id.colour_divider).setVisibility(view.GONE);

            view.findViewById(R.id.aircon_label).setVisibility(view.GONE); aircon.setVisibility(view.GONE);
            view.findViewById(R.id.aircon_divider).setVisibility(view.GONE);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.fromParts("tel", item.getPhone(), null));
                getContext().startActivity(callIntent);
            }
        });

        return view;
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).setToolBarLogo(View.GONE);
            ((MainActivity) getActivity()).setToolBarTitle("BUY");
        }
    }
}
