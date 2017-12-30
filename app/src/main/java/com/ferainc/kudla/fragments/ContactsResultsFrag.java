package com.ferainc.kudla.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.interfaces.FirstPageFragmentListener;
import com.ferainc.kudla.itemclasses.ContactItem;
import com.ferainc.kudla.adapters.ContactItemAdapter;
import com.ferainc.kudla.R;

import java.util.ArrayList;


public class ContactsResultsFrag extends Fragment implements ContactItem.DataListener {

    private ListView mListView;
    private ContactItemAdapter adapter;
    static FirstPageFragmentListener firstPageListener;
    private String contactType="";
    private LinearLayout progressCircle;
    Button btnAddBuss;

    public ContactsResultsFrag() {
        // Required empty public constructor
    }

    public static ContactsResultsFrag createInstance(FirstPageFragmentListener listener) {
        ContactsResultsFrag frag = new ContactsResultsFrag();
        frag.firstPageListener = listener;
        return frag;
    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment(contactType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MyApp", "I am here1");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.contacts_results_list, container, false);

        progressCircle = (LinearLayout) view.findViewById(R.id.progressHeader);
        progressCircle.setVisibility(View.VISIBLE);

        mListView = (ListView) view.findViewById(R.id.item_list_view);
        mListView.setAdapter(adapter);
        mListView.addFooterView(inflater.inflate(R.layout.contacts_results_footer, null));

        contactType = getArguments().getString("TYPE", "Doctors");
        ((MainActivity) getActivity()).setToolBarTitle(contactType);
        //final String DBReference = "contacts/" + contactType;
        final String DBReference = "cities/Mangalore/" + contactType;
        ContactItem.getContactsFromDB(ContactsResultsFrag.this, DBReference, contactType);

        btnAddBuss = (Button) view.findViewById(R.id.AddBuss);
        btnAddBuss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                contactType = "ADD";
                firstPageListener.onSwitchToNextFragment(contactType);
            }
        });

        return view;
    }

    @Override
    public void newDataReceived(ArrayList<ContactItem> itemList) {

        if (isAdded()) {
            progressCircle.setVisibility(View.GONE);

            adapter = new ContactItemAdapter(this.getActivity(), itemList, contactType);
            mListView.setAdapter(adapter); //Replace with notifyDataSetChanged - not working
            Log.d("dccs", String.valueOf((adapter.getCount())));

            if (adapter.getCount() == 0) {
                Toast.makeText(this.getContext(), "There are no entries found for " + contactType, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) this.getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
                //return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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
        }
    }
}
