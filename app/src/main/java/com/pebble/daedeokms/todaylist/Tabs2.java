package com.pebble.daedeokms.todaylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pebble.daedeokms.R;

@SuppressLint("ValidFragment")
public class Tabs2 extends Fragment {
    Context mContext;

    public Tabs2(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_list2, null);

        return view;
    }

}