package com.pebble.daedeokms.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Tools {
    public static boolean isNetwork(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected() || mobile.isConnected();
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();
    }
    public static boolean isOnline(Context mContext) {
        ConnectivityManager mManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
