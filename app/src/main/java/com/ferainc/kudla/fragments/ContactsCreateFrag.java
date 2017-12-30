package com.ferainc.kudla.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.R;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.interfaces.FirstPageFragmentListener;
import com.ferainc.kudla.itemclasses.ContactItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactsCreateFrag extends Fragment {

    private Utils utils;
    static FirstPageFragmentListener firstPageListener;
    Button btnClose, btnSubmit;
    private TextInputLayout titleLayout, specialityLayout, contactLayout, emailLayout, addressLayout;
    private EditText title, speciality, contact, email, address;
    private TextView SpinnerError;
    private String contactType="", area = "", distance = "", fees = "", timings = "", status = "OFF", createdDate="", createdBy="SYS" ;
    String[] contactTypes = {"","Doctors","Dentists",
            "Restaurants","Rentals","Electricians",
            "Cars","Bikes","Tyres",
            "Tourists","Lodgings","Resorts",
            "Tailors","Florists","Salons",
            "Hospitals","Pharmacies","ATMs",
            "Travels","Schools","Colleges"};
    Spinner s1;
    private ProgressDialog progressBar;
    String FragTitle="Add Contact";

    public static ContactsCreateFrag createInstance(FirstPageFragmentListener listener) {
        ContactsCreateFrag frag = new ContactsCreateFrag();
        frag.firstPageListener = listener;
        return frag;
    }

    public ContactsCreateFrag() {
        // Required empty public constructor
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
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.contacts_create, container, false);
        utils = new Utils(this.getContext());

        s1 = (Spinner) view.findViewById(R.id.spinnerContactType);
        s1.setFocusable(true);
        s1.setFocusableInTouchMode(true);
        s1.requestFocus();
        SpinnerError = (TextView) view.findViewById(R.id.SpinnerError);
        SpinnerError.setVisibility(View.GONE);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long arg3) {
                contactType = contactTypes[pos];
                if (!contactType.equals("")) {
                    SpinnerError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        titleLayout = (TextInputLayout) view.findViewById(R.id.title_layout);
        specialityLayout = (TextInputLayout) view.findViewById(R.id.speciality_layout);
        contactLayout = (TextInputLayout) view.findViewById(R.id.phone_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.email_layout);
        addressLayout = (TextInputLayout) view.findViewById(R.id.address_layout);

        title = (EditText) view.findViewById(R.id.title);
        speciality = (EditText) view.findViewById(R.id.speciality);
        contact = (EditText) view.findViewById(R.id.phone);
        email = (EditText) view.findViewById(R.id.email);
        address = (EditText) view.findViewById(R.id.address);

        //title.addTextChangedListener(new MyTextWatcher(title));
        //speciality.addTextChangedListener(new MyTextWatcher(speciality));
        //contact.addTextChangedListener(new MyTextWatcher(contact));
        //email.addTextChangedListener(new MyTextWatcher(email));
        //address.addTextChangedListener(new MyTextWatcher(address));

        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment(contactType);
            }
        });

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit3);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (utils.isNetworkAvailable()) {
                    submitForm();
                }
                else
                {
                    utils.noInternetAlert(getActivity());
                }
            }
        });

        address.setOnKeyListener(new AdapterView.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (((EditText) v).getLineCount() >= 3) {
                        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(address.getWindowToken(), 0);
                        return true;
                    }
                }

                return false;
            }

        });

        return view;
    }

    private void submitForm() {
        if (!validateContactType()) {
            return;
        }
        if (!validateTitle()) {
            return;
        }
        if (!validateSpeciality()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }


        progressBar = new ProgressDialog(this.getContext());
        progressBar.setTitle("In progress");
        progressBar.setMessage("Wait while creation in progress...");
        progressBar.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressBar.show();

        ContactItem item = new ContactItem();

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
        createdDate = sdf.format(new Date());
        System.out.println("Date is" + createdDate);

        item.InsertContactIntoDB(contactType, title.getText().toString(), speciality.getText().toString(), contact.getText().toString(),
                email.getText().toString(), address.getText().toString(), area, distance, fees, timings, status, createdDate, createdBy);

        progressBar.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Thank you! New contact will appear in next 24hrs");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        firstPageListener.onSwitchToNextFragment(contactType);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validateContactType() {
        if (contactType.equals("")) {
            s1.requestFocus();
            SpinnerError.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private boolean validateTitle() {
        if (title.getText().toString().trim().isEmpty()) {
            titleLayout.setError(getString(R.string.err_msg_title));
            requestFocus(title);
            return false;
        } else {
            titleLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSpeciality() {
        return true;
    }

    private boolean validatePhone() {
        if (contact.getText().toString().trim().isEmpty()) {
            contactLayout.setError(getString(R.string.err_msg_phone));
            requestFocus(contact);
            return false;
        } else {
            contactLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String emailStr = email.getText().toString().trim();

        if (emailStr.isEmpty()) {
            return true;
        } else if (!isValidEmail(emailStr)) {
            emailLayout.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateAddress() {
        if (address.getText().toString().trim().isEmpty()) {
            addressLayout.setError(getString(R.string.err_msg_address));
            requestFocus(address);
            return false;
        } else {
            addressLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.title:
                    validateTitle();
                    break;
                case R.id.speciality:
                    validateSpeciality();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.address:
                    validateAddress();
                    break;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        title.addTextChangedListener(new MyTextWatcher(title));
        speciality.addTextChangedListener(new MyTextWatcher(speciality));
        contact.addTextChangedListener(new MyTextWatcher(contact));
        email.addTextChangedListener(new MyTextWatcher(email));
        address.addTextChangedListener(new MyTextWatcher(address));
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