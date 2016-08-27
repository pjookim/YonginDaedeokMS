package com.pebble.daedeokms;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import android.net.*;
import android.content.*;

import com.github.mrengineer13.snackbar.SnackBar;


public class Splash extends ActionBarActivity {
    SnackBar.Builder mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo( ConnectivityManager.TYPE_WIFI);
        NetworkInfo in = cm.getNetworkInfo( ConnectivityManager.TYPE_MOBILE);
        boolean isWifiConn = ni.isConnected();
        boolean isMobileConn = in.isConnected();

        if(isWifiConn==true || isMobileConn==true) {
        }
        else{

            mSnackBar = new SnackBar.Builder(this);
            mSnackBar.withMessage("네트워크에 연결되어 있지 않습니다.");
            mSnackBar.withStyle(SnackBar.Style.ALERT);
            mSnackBar.withActionMessage(getResources().getString(android.R.string.ok));
            mSnackBar.show();
        }

        int[] images = new int[] {R.drawable.splash_image1, R.drawable.splash_image2, R.drawable.splash_image3};

        ImageView mImageView = (ImageView)findViewById(R.id.splash1);
        int imageId = (int)(Math.random() * images.length);

        mImageView.setBackgroundResource(images[imageId]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}