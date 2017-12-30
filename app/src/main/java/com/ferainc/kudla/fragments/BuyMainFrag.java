package com.ferainc.kudla.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.R;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.interfaces.SecondPageFragmentListener;
import com.ferainc.kudla.itemclasses.ProductItem;


public class BuyMainFrag extends Fragment{

    private Utils utils;
    Spinner spinnerCategory, spinnerSubCategory;
    private String category="Vehicles", subCategory="Cars";
    boolean Spinner1Select = false, Spinner2Select = false;
    private ProductItem notUsed;

    static SecondPageFragmentListener secondPageListener;

    public static BuyMainFrag createInstance(SecondPageFragmentListener listener) {
        BuyMainFrag frag = new BuyMainFrag();
        frag.secondPageListener = listener;
        return frag;
    }

    public BuyMainFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_main, container, false);
        utils = new Utils(this.getContext());

        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapterSC = ArrayAdapter
                .createFromResource(view.getContext(), R.array.category, R.layout.spinner_row);
        spinnerCategory.setAdapter(adapterSC);

        spinnerSubCategory = (Spinner) view.findViewById(R.id.spinnerSubCategory);
        ArrayAdapter<CharSequence> adapterSSC = ArrayAdapter
                .createFromResource(view.getContext(), R.array.cars_vehicles, R.layout.spinner_row);
        spinnerSubCategory.setAdapter(adapterSSC);

        Bundle args = getArguments();
        if (args  != null && args.containsKey("CATEGORY") && args.containsKey("SUBCATEGORY")) {
            category = args.getString("CATEGORY", "Vehicles");
            subCategory = args.getString("SUBCATEGORY", "Cars");
            int SCPos = adapterSC.getPosition(category);
            spinnerCategory.setSelection(SCPos);

            if (SCPos == 0) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.cars_vehicles, R.layout.spinner_row);
            } else if (SCPos == 1) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.electronis_computer, R.layout.spinner_row);
            } else if (SCPos == 2) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.baby_children, R.layout.spinner_row);
            } else if (SCPos == 3) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.home_furniture, R.layout.spinner_row);
            } else if (SCPos == 4) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.home_applicance, R.layout.spinner_row);
            } else if (SCPos == 5) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.sports_fitness, R.layout.spinner_row);
            } else if (SCPos == 6) {
                adapterSSC = ArrayAdapter.createFromResource(view.getContext(), R.array.cds_dvds, R.layout.spinner_row);
            }
            spinnerSubCategory.setAdapter(adapterSSC);
            int SSCPos = adapterSSC.getPosition(subCategory);
            spinnerSubCategory.setSelection(SSCPos);
        }


        spinnerCategory.setOnTouchListener(new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Spinner1Select = true;
                Spinner2Select = true;
                return false;
            }

        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long arg3) {
                if (Spinner1Select) {
                    category = parent.getItemAtPosition(pos).toString();
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter
                            .createFromResource(view.getContext(), R.array.cars_vehicles, R.layout.spinner_row);
                    parent.getItemAtPosition(pos);
                    if (pos == 1) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.electronis_computer, R.layout.spinner_row);
                    } else if (pos == 2) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.baby_children, R.layout.spinner_row);
                    } else if (pos == 3) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.home_furniture, R.layout.spinner_row);
                    } else if (pos == 4) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.home_applicance, R.layout.spinner_row);
                    } else if (pos == 5) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.sports_fitness, R.layout.spinner_row);
                    } else if (pos == 6) {
                        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.cds_dvds, R.layout.spinner_row);
                    }
                    spinnerSubCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerSubCategory.setOnTouchListener(new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Spinner2Select = true;
                return false;
            }

        });

        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long arg3) {
                if (Spinner2Select) {
                    subCategory = parent.getItemAtPosition(pos).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button button = (Button) view.findViewById(R.id.search_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("item", "button clicked");

                if (utils.isNetworkAvailable()) {
                    secondPageListener.onSwitchToNextFragment(category, subCategory, notUsed, false);
                }
                else
                {
                    utils.noInternetAlert(getActivity());
                }
            }
        });

        return view;
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
            ((MainActivity) getActivity()).setToolBarTitle("BUY");
        }
    }
}
