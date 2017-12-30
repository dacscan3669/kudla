package com.ferainc.kudla;

import com.ferainc.kudla.adapters.FullScreenImageAdapter;
import com.ferainc.kudla.Utilities.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity{

    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private ArrayList<String> _filePaths = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_image_fs_layout);

        viewPager = (ViewPager) findViewById(R.id.pager);

        //utils = new Utils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        System.out.println("STRU position is " + position);

        _filePaths = (ArrayList<String>) i.getSerializableExtra("PATHS");

        ArrayList<String> imagePaths = new ArrayList<String>();
        for (String str : _filePaths) {
            if (str != null && !str.trim().isEmpty()) {
                System.out.println("STRU is " + str);
                imagePaths.add(str);
            }
        }

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                imagePaths);
        //adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
          //      utils.getFilePaths());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
