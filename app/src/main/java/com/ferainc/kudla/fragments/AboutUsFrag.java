package com.ferainc.kudla.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.R;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.interfaces.FirstPageFragmentListener;

public class AboutUsFrag extends Fragment {

    private Utils utils;
    static FirstPageFragmentListener firstPageListener;
    Button btnClose;
    String FragTitle="About Us";

    public static AboutUsFrag createInstance(FirstPageFragmentListener listener) {
        AboutUsFrag frag = new AboutUsFrag();
        frag.firstPageListener = listener;
        return frag;
    }

    public AboutUsFrag() {
        // Required empty public constructor
    }

    public void backPressed() {
        firstPageListener.onSwitchToNextFragment("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_us, container, false);

        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment("");
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
            ((MainActivity) getActivity()).setToolBarTitle(FragTitle);
        }
    }
}