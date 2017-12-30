package com.ferainc.kudla.fragments;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.itemclasses.ProductItem;
import com.ferainc.kudla.adapters.ProductItemAdapter;
import com.ferainc.kudla.interfaces.SecondPageFragmentListener;
import com.ferainc.kudla.R;

import java.util.ArrayList;


public class BuyResultsFrag extends Fragment implements ProductItem.DataListener {

    private ListView mListView;
    private ProductItemAdapter adapter;
    static SecondPageFragmentListener secondPageListener;
    private String category, subCategory;
    private ProductItem listItem;
    private ProgressDialog progressBar;
    private LinearLayout progressCircle;
    private TextView emptyText;

    public BuyResultsFrag() {
        // Required empty public constructor
    }

    public static BuyResultsFrag createInstance(SecondPageFragmentListener listener) {
        BuyResultsFrag frag = new BuyResultsFrag();
        frag.secondPageListener = listener;
        return frag;
    }

    /*public ListItemMainFragment(FirstPageFragmentListener listener) {
        firstPageListener = listener;
    }*/

    public void backPressed() {
        secondPageListener.onSwitchToNextFragment(category, subCategory, listItem, true);
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
        View view = inflater.inflate(R.layout.buy_results_list, container, false);

        progressCircle = (LinearLayout) view.findViewById(R.id.progressHeader);
        progressCircle.setVisibility(View.VISIBLE);

        mListView = (ListView) view.findViewById(R.id.recipe_list_view);
        emptyText = (TextView) view.findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyText);
        emptyText.setVisibility(View.GONE);

        category = getArguments().getString("CATEGORY", "Vehicles");
        subCategory = getArguments().getString("SUBCATEGORY", "Cars");
        final String DBReference = "products/" + category + "/" + subCategory;
        //final String DBReference = "products/Vehicles/Cars";
        ProductItem.getProductsFromDB(BuyResultsFrag.this, DBReference);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItem = (ProductItem) mListView.getItemAtPosition(position);
                secondPageListener.onSwitchToNextFragment(category, subCategory, listItem, false);
                //replaceWithItemDetails(listItem);
            }
        });

        return view;
    }

    @Override
    public void newDataReceived(ArrayList<ProductItem> itemList) {

        if (isAdded()) {
            progressCircle.setVisibility(View.GONE);

            adapter = new ProductItemAdapter(this.getActivity(), itemList);
            mListView.setAdapter(adapter);
            Log.d("dccs", String.valueOf((adapter.getCount())));

            if (adapter.getCount() == 0) {
                emptyText.setVisibility(View.VISIBLE);
                //Toast.makeText(this.getContext(), "There are no entries found for the selected Category. Please choose a different Category", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void replaceWithItemDetails(ProductItem listItem) {
        final BuyItemDetailsFrag nextFrag = new BuyItemDetailsFrag();
        Bundle args = new Bundle();
        args.putString("CATEGORY", category);
        args.putString("SUBCATEGORY", subCategory);
        ProductItem item = listItem;
        args.putSerializable("ITEM", listItem);
        nextFrag.setArguments(args);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.buy_content, nextFrag); //id of ViewPager
        transaction.addToBackStack(null);
        transaction.commit();
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
                //return false;
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
            ((MainActivity) getActivity()).setToolBarTitle("BUY");
        }
    }
}
