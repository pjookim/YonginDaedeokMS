package com.pebble.daedeokms.bap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pebble.daedeokms.tool.BapTool;

import java.util.ArrayList;

import com.pebble.daedeokms.R;

class BapViewHolder {
    public TextView mCalender;
    public TextView mDayOfTheWeek;
    public TextView mLunch;
}

class BapListData {
    public String mCalender;
    public String mDayOfTheWeek;
    public String mLunch;
}

public class BapListAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<BapListData> mListData = new ArrayList<BapListData>();

    public BapListAdapter(Context mContext) {
        super();

        this.mContext = mContext;
    }

    public void addItem(String mCalender, String mDayOfTheWeek, String mLunch, String mDinner) {

        BapListData addItemInfo = new BapListData();
        addItemInfo.mCalender = mCalender;
        addItemInfo.mDayOfTheWeek = mDayOfTheWeek;
        addItemInfo.mLunch = mLunch;

        mListData.add(addItemInfo);
    }

    public void clearData() {
        mListData.clear();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public BapListData getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BapViewHolder mHolder;

        if (convertView == null) {
            mHolder = new BapViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_bap_item, null);

            mHolder.mCalender = (TextView) convertView
                    .findViewById(R.id.mCalender);
            mHolder.mDayOfTheWeek = (TextView) convertView
                    .findViewById(R.id.mDayOfTheWeek);
            mHolder.mLunch = (TextView) convertView.findViewById(R.id.mLunch);

            convertView.setTag(mHolder);

        } else {
            mHolder = (BapViewHolder) convertView.getTag();
        }

        BapListData mData = mListData.get(position);

        String mCalender = mData.mCalender;

        String mDayOfTheWeek = mData.mDayOfTheWeek;
        String mLunch = mData.mLunch;
        if (BapTool.mStringCheck(mLunch))
            mLunch = mData.mLunch = mContext.getResources().getString(R.string.no_data_lunch);

        mHolder.mCalender.setText(mCalender);
        mHolder.mDayOfTheWeek.setText(mDayOfTheWeek);
        mHolder.mLunch.setText(mLunch);

        return convertView;
    }
}
