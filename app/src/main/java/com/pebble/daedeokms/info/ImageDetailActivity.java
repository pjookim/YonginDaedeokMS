package com.pebble.daedeokms.info;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.pebble.daedeokms.R;

public class ImageDetailActivity extends ActionBarActivity {
    Toolbar mToolbar;

    ViewTouchImage mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mImageView = (ViewTouchImage) findViewById(R.id.mImageView);
        }

    }
}