package com.ferainc.kudla.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ferainc.kudla.MainActivity;
import com.ferainc.kudla.Utilities.ImageActions;
import com.ferainc.kudla.Utilities.ImageUtility;
import com.ferainc.kudla.Utilities.Utils;
import com.ferainc.kudla.interfaces.ThirdPageFragmentListener;
import com.ferainc.kudla.itemclasses.ProductItem;
import com.ferainc.kudla.R;
import com.ferainc.kudla.adapters.SellMainIGAdapter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellMainFrag extends Fragment implements ImageActions.OnUploadCompleted {

    private Utils utils;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private GridView gridview;
    private SellMainIGAdapter adapter;
    private int clickPos=0, donePos=0;
    Spinner spinnerCategory, spinnerSubCategory;
    private RadioGroup ConditionGroup, AirConGroup, FuelGroup;
    private RadioButton ConditionButton, AirConButton, FuelButton;
    private EditText title, description, price, year, kms, mileage, colour, name, phone, email, address;
    private TextInputLayout titleLayout, descriptionLayout, priceLayout, yearLayout, kmsLayout, mileageLayout,
                                colourLayout, nameLayout, phoneLayout, emailLayout, addressLayout;
    private TextView ac_label, fuel_label;
    private View ac_divider, fuel_divider;
    private Button btnSubmit, btnClose;
    private String[] imageURL = {"", "", "", "", ""};
    private String category="Vehicles", subCategory="Cars", createdDate="", createdDateMs="", createdBy="SYS" ;
    private String fuel="Petrol", condition="Used", aircon="Yes";
    private ProgressDialog progressBar;
    boolean Spinner1Select = false, Spinner2Select = false;
    int[] imageId = {R.mipmap.add2, R.mipmap.add, R.mipmap.add, R.mipmap.add, R.mipmap.add };
    Uri[] uriList = new Uri[5];

    static ThirdPageFragmentListener thirdPageListener;

    public SellMainFrag() {
        // Required empty public constructor
    }

    public static SellMainFrag createInstance(ThirdPageFragmentListener listener) {
        SellMainFrag frag = new SellMainFrag();
        frag.thirdPageListener = listener;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_main, container, false);

        int iconSize=getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);

        utils = new Utils(this.getContext());
        for (int i=0; i<=4; i++) {
            uriList[i] = utils.getUriToDrawable(imageId[i]);
        }

        adapter = new SellMainIGAdapter(view.getContext(), iconSize, uriList);
        gridview = (GridView) view.findViewById(R.id.gridView1);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickPos = position;

                if (clickPos == donePos) {
                    selectImage();
                }
            }
        });

        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapterSC = ArrayAdapter
                .createFromResource(view.getContext(), R.array.category, R.layout.spinner_row);
        spinnerCategory.setAdapter(adapterSC);

        spinnerSubCategory = (Spinner) view.findViewById(R.id.spinnerSubCategory);
        ArrayAdapter<CharSequence> adapterSSC = ArrayAdapter
                .createFromResource(view.getContext(), R.array.cars_vehicles, R.layout.spinner_row);
        spinnerSubCategory.setAdapter(adapterSSC);

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
                    parent.getItemAtPosition(pos);
                    if (pos == 0) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.cars_vehicles, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 1) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.electronis_computer, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 2) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.baby_children, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 3) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.home_furniture, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 4) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.home_applicance, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 5) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.sports_fitness, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    } else if (pos == 6) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                                .createFromResource(view.getContext(), R.array.cds_dvds, R.layout.spinner_row);
                        spinnerSubCategory.setAdapter(adapter);
                    }

                    setVisibility();
                    Spinner1Select = false;
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
                    setVisibility();
                    Spinner2Select = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ConditionGroup = (RadioGroup) view.findViewById(R.id.condition);
        ConditionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioUsed:
                        condition = "Used";
                        break;
                    case R.id.radioNew:
                        condition = "New";
                        break;
                }
            }
        });

        AirConGroup = (RadioGroup) view.findViewById(R.id.airCon);
        AirConGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Yes:
                        aircon = "Yes";
                        break;
                    case R.id.No:
                        aircon = "No";
                        break;
                }
            }
        });

        FuelGroup = (RadioGroup) view.findViewById(R.id.fuel);
        FuelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radioPetrol:
                        fuel="Petrol";
                        break;
                    case R.id.radioDiesel:
                        fuel="Diesel";
                        break;
                    case R.id.radioGas:
                        fuel="Gas";
                        break;
                    case R.id.radioDual:
                        fuel="Dual";
                        break;
                }
            }
        });

        titleLayout = (TextInputLayout) view.findViewById(R.id.title_layout);
        descriptionLayout = (TextInputLayout) view.findViewById(R.id.description_layout);
        priceLayout = (TextInputLayout) view.findViewById(R.id.price_layout);
        yearLayout = (TextInputLayout) view.findViewById(R.id.year_layout);
        kmsLayout = (TextInputLayout) view.findViewById(R.id.kms_layout);
        mileageLayout = (TextInputLayout) view.findViewById(R.id.mileage_layout);
        colourLayout = (TextInputLayout) view.findViewById(R.id.colour_layout);
        nameLayout = (TextInputLayout) view.findViewById(R.id.name_layout);
        phoneLayout = (TextInputLayout) view.findViewById(R.id.phone_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.email_layout);
        addressLayout = (TextInputLayout) view.findViewById(R.id.address_layout);

        title = (EditText) view.findViewById(R.id.title);
        description = (EditText) view.findViewById(R.id.description);
        price = (EditText) view.findViewById(R.id.price);
        year = (EditText) view.findViewById(R.id.year);
        kms = (EditText) view.findViewById(R.id.kms);
        mileage = (EditText) view.findViewById(R.id.mileage);
        colour = (EditText) view.findViewById(R.id.colour);
        name = (EditText) view.findViewById(R.id.name);
        phone = (EditText) view.findViewById(R.id.phone);
        email = (EditText) view.findViewById(R.id.email);
        address = (EditText) view.findViewById(R.id.address);
        ac_label = (TextView) view.findViewById(R.id.ac_label);
        fuel_label = (TextView) view.findViewById(R.id.fuel_label);
        ac_divider = (View) view.findViewById(R.id.aircon_divider);
        fuel_divider = (View) view.findViewById(R.id.fuel_divider);

        //title.addTextChangedListener(new MyTextWatcher(title));
        //description.addTextChangedListener(new MyTextWatcher(description));
        //price.addTextChangedListener(new MyTextWatcher(price));
        //year.addTextChangedListener(new MyTextWatcher(year));
        //kms.addTextChangedListener(new MyTextWatcher(kms));
        //mileage.addTextChangedListener(new MyTextWatcher(mileage));
        //colour.addTextChangedListener(new MyTextWatcher(colour));
        //name.addTextChangedListener(new MyTextWatcher(name));
        //phone.addTextChangedListener(new MyTextWatcher(phone));
        //email.addTextChangedListener(new MyTextWatcher(email));
        //address.addTextChangedListener(new MyTextWatcher(address));

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (utils.isNetworkAvailable()) {
                    submitForm();
                }
                else
                {
                    utils.noInternetAlert(getActivity());
                }
            }
        });

        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                thirdPageListener.onSwitchToNextFragment();
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


    //Validating form
    private void submitForm() {
        if (!validateTitle()) {
            return;
        }
        if (!validateDescription()) {
            return;
        }
        if (!validatePrice()) {
            return;
        }
        if (!validateName()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePhoneOrEmail()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }

        if (category.equals("Vehicles") &&
                (subCategory.equals("Cars") || subCategory.equals("Bikes") ||
                        subCategory.equals("Vans and Jeeps") || subCategory.equals("Lorries and Trucks"))) {
            if (!validateYear()) {
                return;
            }
            if (!validateKms()) {
                return;
            }
            if (!validateMileage()) {
                return;
            }
            if (!validateColour()) {
                return;
            }
        }

        progressBar = new ProgressDialog(this.getContext());
        progressBar.setTitle("In progress");
        progressBar.setMessage("Wait while creation in progress...");
        progressBar.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressBar.show();

        ProductItem item = new ProductItem();

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
        createdDate = sdf.format(new Date());
        long time= System.currentTimeMillis();
        System.out.println("TIME " + time);
        createdDateMs = String.valueOf(time);
        System.out.println("TIME " + createdDateMs);

        item.InsertProductIntoDB(category, subCategory, title.getText().toString(), description.getText().toString(),
                condition, price.getText().toString(), year.getText().toString(), kms.getText().toString(), mileage.getText().toString(),
                colour.getText().toString(), name.getText().toString(), phone.getText().toString(),
                email.getText().toString(), address.getText().toString(), fuel, aircon, imageURL, createdDate, createdDateMs, createdBy);

        progressBar.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Request submitted successfully");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        thirdPageListener.onSwitchToNextFragment();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

    private boolean validateDescription() {
        return true;
    }

    private boolean validatePrice() {
        if (price.getText().toString().trim().isEmpty()) {
            priceLayout.setError(getString(R.string.err_msg_price));
            requestFocus(price);
            return false;
        } else {
            priceLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateYear() {
        if (year.getText().toString().trim().isEmpty()) {
            yearLayout.setError(getString(R.string.err_msg_year));
            requestFocus(year);
            return false;
        } else {
            yearLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateKms() {
        if (kms.getText().toString().trim().isEmpty()) {
            kmsLayout.setError(getString(R.string.err_msg_kms));
            requestFocus(kms);
            return false;
        } else {
            kmsLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMileage() {
        return true;
    }

    private boolean validateColour() {
        return true;
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            nameLayout.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            nameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        /*if (phone.getText().toString().trim().isEmpty()) {
            phoneLayout.setError(getString(R.string.err_msg_phone));
            requestFocus(phone);
            return false;
        } else {
            phoneLayout.setErrorEnabled(false);
        }*/

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

    private boolean validatePhoneOrEmail() {
        if (phone.getText().toString().trim().isEmpty() && email.getText().toString().trim().isEmpty()) {
            phoneLayout.setError(getString(R.string.err_msg_phone_email));
            emailLayout.setError(getString(R.string.err_msg_phone_email));
            requestFocus(phone);
            return false;
        } else {
            phoneLayout.setErrorEnabled(false);
            emailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        return true;
    }

    /*private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }*/



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
                case R.id.description:
                    validateDescription();
                    break;
                case R.id.price:
                    validatePrice();
                    break;
                case R.id.year:
                    validateYear();
                    break;
                case R.id.kms:
                    validateKms();
                    break;
                case R.id.mileage:
                    validateMileage();
                    break;
                case R.id.colour:
                    validateColour();
                    break;
                case R.id.name:
                    validateName();

                    //btnSubmit.setFocusable(true);
                    //btnSubmit.setFocusableInTouchMode(true);
                    //btnSubmit.requestFocus();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImageUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ImageUtility.checkPermission(getContext());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        uploadAndDisplay(data);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        uploadAndDisplay(data);
    }

    private void uploadAndDisplay(Intent data) {
        Bitmap bm = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds=true;
        Uri imageURI = data.getData();

        try {
            InputStream in = null;
            in = getContext().getContentResolver().openInputStream(imageURI);
            BitmapFactory.decodeStream(in, null, o);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageActions IA1 = new ImageActions();
        int scale = IA1.calculateInSampleSize(o, 500, 500);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        AssetFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = getContext().getContentResolver().openAssetFileDescriptor(data.getData(),"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IA1.ImageLoadFile(destination.toString(), this);

        uriList[clickPos] = Uri.fromFile(destination);
                ++donePos;
        if ( clickPos < 4)
            uriList[clickPos + 1] = utils.getUriToDrawable(imageId[0]);
        adapter.notifyDataSetChanged();

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        System.out.println("SCALE used size in MB..." + usedMemInMB);
        System.out.println("SCALE max heap in MB..." + maxHeapSizeInMB);

        //bm.recycle();
        //System.gc();
    }

    @Override
    public void onSuccess(String downloadUrl){

        //imageURL[clickPos] = downloadUrl;
        if(imageURL[0].trim().isEmpty()) {
            imageURL[0] = downloadUrl;
        } else if (imageURL[1].trim().isEmpty()) {
            imageURL[1] = downloadUrl;
        } else if (imageURL[2].trim().isEmpty()) {
            imageURL[2] = downloadUrl;
        } else if (imageURL[3].trim().isEmpty()) {
            imageURL[3] = downloadUrl;
        } else if (imageURL[4].trim().isEmpty()) {
            imageURL[4] = downloadUrl;
        }
    }

    @Override
    public void onFail(){

    }

    private void setVisibility() {
        if (category.equals("Vehicles") &&
                (subCategory.equals("Cars") || subCategory.equals("Bikes") ||
                        subCategory.equals("Vans and Jeeps") || subCategory.equals("Lorries and Trucks"))) {
            year.setVisibility(View.VISIBLE);
            kms.setVisibility(View.VISIBLE);
            mileage.setVisibility(View.VISIBLE);
            colour.setVisibility(View.VISIBLE);

            fuel_label.setVisibility(View.VISIBLE);
            FuelGroup.setVisibility(View.VISIBLE);
            fuel_divider.setVisibility(View.VISIBLE);

            ac_label.setVisibility(View.VISIBLE);
            AirConGroup.setVisibility(View.VISIBLE);
            ac_divider.setVisibility(View.VISIBLE);
        } else {
            year.setVisibility(View.GONE);
            kms.setVisibility(View.GONE);
            mileage.setVisibility(View.GONE);
            colour.setVisibility(View.GONE);

            fuel_label.setVisibility(View.GONE);
            FuelGroup.setVisibility(View.GONE);
            fuel_divider.setVisibility(View.GONE);

            ac_label.setVisibility(View.GONE);
            AirConGroup.setVisibility(View.GONE);
            ac_divider.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        title.addTextChangedListener(new MyTextWatcher(title));
        description.addTextChangedListener(new MyTextWatcher(description));
        price.addTextChangedListener(new MyTextWatcher(price));
        year.addTextChangedListener(new MyTextWatcher(year));
        kms.addTextChangedListener(new MyTextWatcher(kms));
        mileage.addTextChangedListener(new MyTextWatcher(mileage));
        colour.addTextChangedListener(new MyTextWatcher(colour));
        name.addTextChangedListener(new MyTextWatcher(name));
        phone.addTextChangedListener(new MyTextWatcher(phone));
        email.addTextChangedListener(new MyTextWatcher(email));
        address.addTextChangedListener(new MyTextWatcher(address));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).setToolBarLogo(View.VISIBLE);
            ((MainActivity) getActivity()).setToolBarTitle("SELL");
        }
    }

    private void uploadAndDisplayOld (Intent data) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        //InSampleSize takes a bitmap and reduces its height and width to 20% (1/5) of its original size.
        //The larger the value of inSampleSize N (where N=5 in our example), the more the bitmap is reduced in size.
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = getContext().getContentResolver().openAssetFileDescriptor(data.getData(),"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageActions IA1 = new ImageActions();
        IA1.ImageLoadFile(destination.toString(), this);

        uriList[clickPos] = Uri.fromFile(destination);
        ++donePos;
        if ( clickPos < 4)
            uriList[clickPos + 1] = utils.getUriToDrawable(imageId[0]);
        adapter.notifyDataSetChanged();

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        System.out.println("used size in MB..." + usedMemInMB);
        System.out.println("max heap in MB..." + maxHeapSizeInMB);

        //bm.recycle();
        //System.gc();
    }
}
