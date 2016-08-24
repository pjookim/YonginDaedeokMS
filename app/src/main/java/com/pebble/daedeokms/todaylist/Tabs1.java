package com.pebble.daedeokms.todaylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pebble.daedeokms.R;

@SuppressLint("ValidFragment")
public class Tabs1 extends Fragment {

    ListView mListView;
    TodayListAdapter mAdapter;
    Context mContext;

    public Tabs1(Context context) {
        mContext = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_list1, null);

        return view;
    }

}