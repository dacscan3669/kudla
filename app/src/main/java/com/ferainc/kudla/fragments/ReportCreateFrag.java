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
import com.ferainc.kudla.itemclasses.ReportItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportCreateFrag extends Fragment {

    private Utils utils;
    static FirstPageFragmentListener firstPageListener;
    Button btnClose, btnSubmit;
    private TextInputLayout descriptionLayout, emailLayout;
    private EditText description, email;
    private String reportType="", createdDate="", createdBy="SYS";
    String[] reportTypes = {"Issue","Feedback"};
    Spinner s1;
    private ProgressDialog progressBar;
    String FragTitle="Contact Us";

    public static ReportCreateFrag createInstance(FirstPageFragmentListener listener) {
        ReportCreateFrag frag = new ReportCreateFrag();
        frag.firstPageListener = listener;
        return frag;
    }

    public ReportCreateFrag() {
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
        View view = inflater.inflate(R.layout.report_create, container, false);
        utils = new Utils(this.getContext());

        s1 = (Spinner) view.findViewById(R.id.spinnerReportType);
        s1.setFocusable(true);
        s1.setFocusableInTouchMode(true);
        s1.requestFocus();

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long arg3) {
                reportType = reportTypes[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        descriptionLayout = (TextInputLayout) view.findViewById(R.id.description_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.email_layout);

        description = (EditText) view.findViewById(R.id.description);
        email = (EditText) view.findViewById(R.id.email);

        //title.addTextChangedListener(new MyTextWatcher(title));
        //speciality.addTextChangedListener(new MyTextWatcher(speciality));
        //contact.addTextChangedListener(new MyTextWatcher(contact));
        //email.addTextChangedListener(new MyTextWatcher(email));
        //address.addTextChangedListener(new MyTextWatcher(address));

        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                firstPageListener.onSwitchToNextFragment("");
            }
        });

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit3);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (utils.isNetworkAvailable()) {
                    submitForm();
                } else {
                    utils.noInternetAlert(getActivity());
                }
            }
        });

        description.setOnKeyListener(new AdapterView.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (((EditText) v).getLineCount() >= 5) {
                        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(description.getWindowToken(), 0);
                        return true;
                    }
                }

                return false;
            }

        });

        return view;
    }

    private void submitForm() {
        if (!validateDescription()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        progressBar = new ProgressDialog(this.getContext());
        progressBar.setTitle("In progress");
        progressBar.setMessage("Wait while creation in progress...");
        progressBar.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressBar.show();

        ReportItem item = new ReportItem();

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
        createdDate = sdf.format(new Date());
        System.out.println("Date is" + createdDate);

        item.InsertReportIntoDB(reportType, description.getText().toString(), email.getText().toString(), createdDate, createdBy);

        progressBar.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Thank you contacting us! We shall process your feedback and contact you if necessary.");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        firstPageListener.onSwitchToNextFragment("");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validateDescription() {
        if (description.getText().toString().trim().isEmpty()) {
            descriptionLayout.setError(getString(R.string.err_msg_title));
            requestFocus(description);
            return false;
        } else {
            descriptionLayout.setErrorEnabled(false);
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
                    validateDescription();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        description.addTextChangedListener(new MyTextWatcher(description));
        email.addTextChangedListener(new MyTextWatcher(email));
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