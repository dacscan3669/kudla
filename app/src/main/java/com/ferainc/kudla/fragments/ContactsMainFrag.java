package com.ferainc.kudla.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.adapters.ContactGridAdapter;
import com.ferainc.kudla.interfaces.FirstPageFragmentListener;
import com.ferainc.kudla.R;

public class ContactsMainFrag extends Fragment {

    private Utils utils;
    static FirstPageFragmentListener firstPageListener;


    public static ContactsMainFrag createInstance(FirstPageFragmentListener listener) {
        ContactsMainFrag frag = new ContactsMainFrag();
        frag.firstPageListener = listener;
        return frag;
    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment("");
    }

    /*public OneFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }*/

    /*public void onListItemClick (ListView l, View v, int position, long id) {
        firstPageListener.onSwitchToNextFragment();
    }*/

    public ContactsMainFrag() {
        // Required empty public constructor
    }

    //private ListView mListView;
    GridView grid;
    String[] contactType = {
            //"Movies","Doctors","Dentists",
            "ADD", "Doctors","Dentists",
            "Restaurants","Rentals","Electricians",
            "Cars","Bikes","Tyres",
            "Tourists","Lodgings","Resorts",
            "Tailors","Florists","Salons",
            "Hospitals","Pharmacies","ATMs",
            "Travels","Schools","Colleges"
    };

    String[] contactDisplayStr = {
            //"Movies","Doctors","Dentists",
            "Add Contact","Doctors","Dentists",
            "Restaurants","Car Rental","Electricians",
            "Car Repair","Bike Repair","Tyre Repair",
            "Tourist Spots","Lodging","Resorts",
            "Tailors","Florists","Beauty Salons",
            "Hospitals","Pharmacies","ATMs",
            "Travel Agents","Schools","Colleges",
    };

    int[] imageId = {
            //R.mipmap.ic_movie2, R.mipmap.ic_doctor2, R.mipmap.ic_dentist2,
            R.mipmap.cblue, R.mipmap.ic_doctor2, R.mipmap.ic_dentist2,
            R.mipmap.ic_restaurant2, R.mipmap.ic_rental2, R.mipmap.ic_electrician2,
            R.mipmap.ic_car2, R.mipmap.ic_bike2, R.mipmap.ic_tyre2,
            R.mipmap.ic_tourist2, R.mipmap.ic_lodging2, R.mipmap.ic_resort2,
            R.mipmap.ic_tailor2, R.mipmap.ic_florist2, R.mipmap.ic_salon2,
            R.mipmap.ic_hospital2, R.mipmap.ic_pharmacy2, R.mipmap.ic_atm2,
            R.mipmap.ic_travel2, R.mipmap.ic_school2, R.mipmap.ic_college2
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.contacts_main, container, false);
        int iconSize=getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
        utils = new Utils(this.getContext());

        ContactGridAdapter adapter = new ContactGridAdapter(view.getContext(), contactDisplayStr, imageId, iconSize);
        GridView gridview = (GridView) view.findViewById(R.id.grid);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(view.getContext(), "You Clicked at " + contactDisplayStr[+position], Toast.LENGTH_SHORT).show();

                if (utils.isNetworkAvailable()) {
                    firstPageListener.onSwitchToNextFragment(contactType[position]);
                    //replaceWithDetails(position);
                } else {
                    utils.noInternetAlert(getActivity());
                }
            }
        });

        return view;
    }

    private void replaceWithDetails(int position) {

        final ContactsResultsFrag nextFrag = new ContactsResultsFrag();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        args.putString("TYPE", contactType[position]);
        nextFrag.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contacts_content, nextFrag); //id of ViewPager
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.contacts_menu, menu);
        MenuItem item = menu.findItem(R.id.create_report);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).setToolBarLogo(View.VISIBLE);
            ((MainActivity) getActivity()).setToolBarTitle("CONTACTS");
        }
    }

}
