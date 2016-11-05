package com.pebble.daedeokms;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import android.net.*;
import android.content.*;

import com.github.mrengineer13.snackbar.SnackBar;

/**
 * Created by pjookim on 2015. 1. 15..
 */

public class Splash extends Activity {
    SnackBar.Builder mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2500);

        int[] images = new int[] {R.drawable.splash_image1, R.drawable.splash_image2, R.drawable.splash_image3};

        ImageView mImageView = (ImageView)findViewById(R.id.splash1);
        int imageId = (int)(Math.random() * images.length);

        mImageView.setImageResource(images[imageId]);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo( ConnectivityManager.TYPE_WIFI);
        NetworkInfo in = cm.getNetworkInfo( ConnectivityManager.TYPE_MOBILE);
        boolean isWifiConn = ni.isConnected();
        boolean isMobileConn = in.isConnected();

        if(isWifiConn==false && isMobileConn==false) {
            mSnackBar = new SnackBar.Builder(this);
            mSnackBar.withMessage(getResources().getString(R.string.no_network));
            mSnackBar.withStyle(SnackBar.Style.ALERT);
            mSnackBar.withActionMessage(getResources().getString(android.R.string.ok));
            mSnackBar.show();
        }
    }
}