package com.pebble.daedeokms.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pebble.daedeokms.R;

public class SchoolInfo extends ActionBarActivity {
    Toolbar mToolbar;

    ListView mListView;
    InfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);

        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new InfoAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);

        getListItem();
    }

    public void getListItem() {
        mAdapter.addItem("교표.교훈", R.drawable.schoolmark, null, null, false);
        mAdapter.addItem("교화", R.drawable.schoolflower, "수련", null, false);
        mAdapter.addItem("교목", R.drawable.schooltree, "소나무", null, false);
        mAdapter.addItem("약도", R.drawable.schoolmap, "경기도 용인시 수지구 죽전로 293번길 12", null, true);
        mAdapter.addItem("교가", R.drawable.schoolsong, null, null, true);
        mAdapter.addItem("교무실 전화", 0, null, "070-8611-2969", false);
        mAdapter.addItem("행정실 전화", 0, null, "070-8611-2973", false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String mUri = "tel:";
                switch (position) {
                    case 5:
                    case 6:
                        mUri += mAdapter.getItem(position).mText2;
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse(mUri)));
                        } catch (Exception e) {

                        }
                        break;
                }
            }
        });

        mAdapter.notifyDataSetChanged();
    }
}
