package com.ferainc.kudla;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferainc.kudla.Utilities.CustomViewPager;
import com.ferainc.kudla.fragments.AboutUsFrag;
import com.ferainc.kudla.fragments.BuyItemDetailsFrag;
import com.ferainc.kudla.fragments.ContactsCreateFrag;
import com.ferainc.kudla.fragments.ContactsMainFrag;
import com.ferainc.kudla.fragments.ContactsResultsFrag;
import com.ferainc.kudla.fragments.BuyMainFrag;
import com.ferainc.kudla.fragments.BuyResultsFrag;
import com.ferainc.kudla.fragments.ReportCreateFrag;
import com.ferainc.kudla.fragments.SellMainFrag;
import com.ferainc.kudla.interfaces.FirstPageFragmentListener;
import com.ferainc.kudla.interfaces.SecondPageFragmentListener;
import com.ferainc.kudla.interfaces.ThirdPageFragmentListener;
import com.ferainc.kudla.itemclasses.ProductItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    //private ViewPager viewPager;
    private CustomViewPager viewPager;
    private MyAdapter mAdapter;
    TextView mTitle;
    ImageView logo;
    String[] title = {"CONTACTS", "BUY", "SELL", "Add Contact", "Contact Us", "About Us"};
    private static String menuSelect = "NONE";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText(title[0]);
            logo = (ImageView) toolbar.findViewById(R.id.logo);
            //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

            mAdapter = new MyAdapter(getSupportFragmentManager());
            //viewPager = (ViewPager)findViewById(R.id.viewpager);
            viewPager = (CustomViewPager)findViewById(R.id.viewpager);
            viewPager.setAdapter(mAdapter);
            viewPager.setPagingEnabled(false);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                    List<Fragment> fragments = fragmentManager.getFragments();
                    if (fragments != null) {
                        for (Fragment fragment : fragments) {
                            if (fragment != null && fragment.isVisible()) {

                                switch (position) {
                                    case 0:
                                        if (fragment.getClass().toString().contains("ContactsMainFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE);
                                            mTitle.setText(title[position]);
                                        } else if (fragment.getClass().toString().contains("ContactsResultsFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                            setToolBarLogo(View.GONE);
                                            String contactType = fragment.getArguments().getString("TYPE", "CONTACTS");
                                            mTitle.setText(contactType);
                                        } else if (fragment.getClass().toString().contains("ContactsCreateFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE);
                                            mTitle.setText(title[3]);
                                        } else if (fragment.getClass().toString().contains("ReportCreateFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE);
                                            mTitle.setText(title[4]);
                                        } else if (fragment.getClass().toString().contains("AboutUsFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE);
                                            mTitle.setText(title[5]);
                                        }
                                        break;
                                    case 1:
                                        if (fragment.getClass().toString().contains("BuyMainFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE);
                                        } else if (fragment.getClass().toString().contains("BuyResultsFrag") || fragment.getClass().toString().contains("BuyItemDetailsFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                            setToolBarLogo(View.GONE); }
                                        mTitle.setText(title[position]);
                                        break;
                                    case 2:
                                        if (fragment.getClass().toString().contains("SellMainFrag")) {
                                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                            setToolBarLogo(View.VISIBLE); }
                                        mTitle.setText(title[position]);
                                        break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    System.out.println("AUTH FirebaseAuth onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    System.out.println("AUTH FirebaseAuth onAuthStateChanged:signed_out");
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("AUTH signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("AUTH signInWithEmail:failed" + task.getException());
                        }

                        // ...
                    }
                });*/

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("AUTH FirebaseAuth signInAnonymously:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            System.out.println("AUTH FirebaseAuth signInAnonymously" + task.getException());

                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //finish(); // close this activity and return to preview activity (if there is any)
            onBackPressed();
        } else if (item.getItemId() == R.id.create_report) {
            menuSelect = "REPORT";
            onBackPressed();
        } else if (item.getItemId() == R.id.about) {
            menuSelect = "ABOUT";
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0) {
            if (mAdapter.getItem(0) instanceof ContactsResultsFrag) {
                ((ContactsResultsFrag) mAdapter.getItem(0)).backPressed();
            } else if ((mAdapter.getItem(0) instanceof ContactsMainFrag) && menuSelect.equals("REPORT")) {
                ((ContactsMainFrag) mAdapter.getItem(0)).backPressed();
            } else if ((mAdapter.getItem(0) instanceof ContactsMainFrag) && menuSelect.equals("ABOUT")) {
                ((ContactsMainFrag) mAdapter.getItem(0)).backPressed();
            } else if (mAdapter.getItem(0) instanceof ContactsCreateFrag) {
                ((ContactsCreateFrag) mAdapter.getItem(0)).backPressed();
            } else if (mAdapter.getItem(0) instanceof ReportCreateFrag) {
                ((ReportCreateFrag) mAdapter.getItem(0)).backPressed();
            } else if (mAdapter.getItem(0) instanceof AboutUsFrag) {
                ((AboutUsFrag) mAdapter.getItem(0)).backPressed();
            } else {
                this.moveTaskToBack(true);
            }
        }
        else if(viewPager.getCurrentItem() == 1) {
            if (mAdapter.getItem(1) instanceof BuyResultsFrag) {
                ((BuyResultsFrag) mAdapter.getItem(1)).backPressed();
            }
            else if (mAdapter.getItem(1) instanceof BuyItemDetailsFrag) {
                ((BuyItemDetailsFrag) mAdapter.getItem(1)).backPressed();
            }
            else if (mAdapter.getItem(1) instanceof BuyMainFrag) {
                viewPager.setCurrentItem(0);
                //finish();
            }
        }
        else if(viewPager.getCurrentItem() == 2) {
            if (mAdapter.getItem(2) instanceof SellMainFrag) {
                viewPager.setCurrentItem(1);
                //finish();
            }
        }
    }

    private static class MyAdapter extends FragmentPagerAdapter {

        private final class FirstPageListener implements
                FirstPageFragmentListener {
            public void onSwitchToNextFragment(String contactType) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
                        .commit();

                if (menuSelect.equals("REPORT")) {
                    menuSelect = "NONE";
                    mFragmentAtPos0 = ReportCreateFrag.createInstance(FirstListener);
                } else if (menuSelect.equals("ABOUT")) {
                    menuSelect = "NONE";
                    mFragmentAtPos0 = AboutUsFrag.createInstance(FirstListener);
                } else if (contactType.equals("ADD")) {
                    mFragmentAtPos0 = ContactsCreateFrag.createInstance(FirstListener);
                } else {
                    if (mFragmentAtPos0 instanceof ContactsMainFrag) {
                        //mFragmentAtPos0 = new ListItemMainFragment(listener);
                        mFragmentAtPos0 = ContactsResultsFrag.createInstance(FirstListener);
                        Bundle args = new Bundle();
                        args.putString("TYPE", contactType);
                        mFragmentAtPos0.setArguments(args);
                    } else { // Instance of NextFragment
                        mFragmentAtPos0 = ContactsMainFrag.createInstance(FirstListener);
                    }
                }

                notifyDataSetChanged();
            }
        }

        private final class SecondPageListener implements
                SecondPageFragmentListener {
            public void onSwitchToNextFragment(String category, String subCategory, ProductItem listItem, boolean backPressed) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos1)
                        .commit();
                if (mFragmentAtPos1 instanceof BuyMainFrag) {
                    mFragmentAtPos1 = BuyResultsFrag.createInstance(SecondListener);
                    Bundle args = new Bundle();
                    args.putString("CATEGORY", category);
                    args.putString("SUBCATEGORY", subCategory);
                    mFragmentAtPos1.setArguments(args);
                } else if (mFragmentAtPos1 instanceof BuyResultsFrag && !(backPressed)) {
                    mFragmentAtPos1 = BuyItemDetailsFrag.createInstance(SecondListener);
                    Bundle args1 = new Bundle();
                    args1.putString("CATEGORY", category);
                    args1.putString("SUBCATEGORY", subCategory);
                    args1.putSerializable("ITEM", listItem);
                    mFragmentAtPos1.setArguments(args1);
                } else if (mFragmentAtPos1 instanceof BuyItemDetailsFrag && (backPressed)) {
                    mFragmentAtPos1 = BuyResultsFrag.createInstance(SecondListener);
                    Bundle args = new Bundle();
                    args.putString("CATEGORY", category);
                    args.putString("SUBCATEGORY", subCategory);
                    mFragmentAtPos1.setArguments(args);
                }else{ // Instance of NextFragment
                    mFragmentAtPos1 = BuyMainFrag.createInstance(SecondListener);
                    Bundle args = new Bundle();
                    args.putString("CATEGORY", category);
                    args.putString("SUBCATEGORY", subCategory);
                    mFragmentAtPos1.setArguments(args);
                }

                notifyDataSetChanged();
            }
        }

        private final class ThirdPageListener implements
                ThirdPageFragmentListener {
            public void onSwitchToNextFragment() {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos2)
                        .commit();
                mFragmentAtPos2 = SellMainFrag.createInstance(ThirdListener);

                notifyDataSetChanged();
            }
        }


        private String[] titles = { "CONTACTS", "BUY", "SELL" };
        private final FragmentManager mFragmentManager;
        public Fragment mFragmentAtPos0;
        public Fragment mFragmentAtPos1;
        public Fragment mFragmentAtPos2;
        private Context context;
        FirstPageListener FirstListener = new FirstPageListener();
        SecondPageListener SecondListener = new SecondPageListener();
        ThirdPageListener ThirdListener = new ThirdPageListener();

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0
                    if (mFragmentAtPos0 == null)
                    {
                        //mFragmentAtPos0 = new OneFragment(listener);
                        mFragmentAtPos0 = ContactsMainFrag.createInstance(FirstListener);
                    }
                    System.out.println(mFragmentAtPos0.toString());
                    return mFragmentAtPos0;

                case 1: // Fragment # 1
                    if (mFragmentAtPos1 == null)
                    {
                        //mFragmentAtPos0 = new OneFragment(listener);
                        mFragmentAtPos1 = BuyMainFrag.createInstance(SecondListener);
                    }
                    //return new BuyMainFrag();
                    return mFragmentAtPos1;
                case 2:// Fragment # 2
                    if (mFragmentAtPos2 == null)
                    {
                        //mFragmentAtPos0 = new OneFragment(listener);
                        mFragmentAtPos2 = SellMainFrag.createInstance(ThirdListener);
                    }
                    return mFragmentAtPos2;
                    //return new SellMainFrag();
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public int getItemPosition(Object object)
        {
            if (object instanceof ContactsMainFrag &&
                    mFragmentAtPos0 instanceof ContactsResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsResultsFrag &&
                    mFragmentAtPos0 instanceof ContactsMainFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsCreateFrag &&
                    mFragmentAtPos0 instanceof ContactsMainFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsMainFrag &&
                    mFragmentAtPos0 instanceof ContactsCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsCreateFrag &&
                    mFragmentAtPos0 instanceof ContactsResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsResultsFrag &&
                    mFragmentAtPos0 instanceof ContactsCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ReportCreateFrag &&
                    mFragmentAtPos0 instanceof ContactsMainFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsMainFrag &&
                    mFragmentAtPos0 instanceof ReportCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ReportCreateFrag &&
                    mFragmentAtPos0 instanceof ContactsResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsResultsFrag &&
                    mFragmentAtPos0 instanceof ReportCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ReportCreateFrag &&
                    mFragmentAtPos0 instanceof ContactsCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsCreateFrag &&
                    mFragmentAtPos0 instanceof ReportCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof AboutUsFrag &&
                    mFragmentAtPos0 instanceof ContactsMainFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsMainFrag &&
                    mFragmentAtPos0 instanceof AboutUsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof AboutUsFrag &&
                    mFragmentAtPos0 instanceof ContactsResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsResultsFrag &&
                    mFragmentAtPos0 instanceof AboutUsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof AboutUsFrag &&
                    mFragmentAtPos0 instanceof ContactsCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ContactsCreateFrag &&
                    mFragmentAtPos0 instanceof AboutUsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof AboutUsFrag &&
                    mFragmentAtPos0 instanceof ReportCreateFrag) {
                return POSITION_NONE;
            }
            if (object instanceof ReportCreateFrag &&
                    mFragmentAtPos0 instanceof AboutUsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof BuyMainFrag &&
                    mFragmentAtPos1 instanceof BuyResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof BuyResultsFrag &&
                    mFragmentAtPos1 instanceof BuyMainFrag) {
                return POSITION_NONE;
            }
            if (object instanceof BuyResultsFrag &&
                    mFragmentAtPos1 instanceof BuyItemDetailsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof BuyItemDetailsFrag &&
                    mFragmentAtPos1 instanceof BuyResultsFrag) {
                return POSITION_NONE;
            }
            if (object instanceof SellMainFrag ) {
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }

    }

    public void setToolBarTitle(String title){ mTitle.setText(title); }

    public void setToolBarLogo(int flag){
        logo.setVisibility(flag);
    }
}