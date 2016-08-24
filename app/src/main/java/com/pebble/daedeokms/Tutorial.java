package com.pebble.daedeokms;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.Window;

import android.content.*;
import android.view.WindowManager;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.Calendar;

public class Tutorial extends Activity {

    private ViewPager mPager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);

        mPager = (ViewPager)findViewById(R.id.view_pager);
        mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));

        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .type(SnackbarType.MULTI_LINE)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("페이지를 왼쪽으로 넘겨서 튜토리얼을 완료하세요!")
                        .actionLabel("확인") // action button label
                        .actionColor(Color.parseColor("#ff009688"))
                        .actionListener(new ActionClickListener() {
                                            @Override
                                            public void onActionClicked(Snackbar snackbar) {

                                            }
                                        }
                        )
        , this); // action button's ActionClickListener
    }

    private View.OnClickListener mCloseButtonClick = new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            int infoFirst=1;

            SharedPreferences a = getSharedPreferences("a", MODE_PRIVATE);

            SharedPreferences.Editor editor = a.edit();

            editor.putInt("First", infoFirst);

            editor.commit();

            finish();

        }

    };

    private class BkPagerAdapter extends PagerAdapter{
        private LayoutInflater mInflater;

        public BkPagerAdapter( Context con) {
            super();
            mInflater = LayoutInflater.from(con);
        }

        @Override public int getCount() { return 4; } // return *; ( * 이 뷰페이저 개수 )

        // 뷰페이저 설정
        @Override public Object instantiateItem(View pager, int position) {
            View v = null;
            if(position==0){
                v = mInflater.inflate(R.layout.tutorial1, null);

            }
            else if(position==1){
                v = mInflater.inflate(R.layout.tutorial2, null);

            }
            else if(position==2){
                v = mInflater.inflate(R.layout.tutorial3, null);

            }
            else{
                v = mInflater.inflate(R.layout.tutorial4, null);
                v.findViewById(R.id.close).setOnClickListener(mCloseButtonClick);


/** 만약 버튼을 만드셨다면 넣어주세요.
 v.findViewById(R.id.버튼 아이디).setOnClickListener(mButtonClick); **/
            }

            ((ViewPager)pager).addView(v, 0);
            return v;
        }

        @Override public void destroyItem(View pager, int position, Object view) {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

        @Override public void finishUpdate(View arg0) {}
        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
        @Override public Parcelable saveState() { return null; }
        @Override public void startUpdate(View arg0) {}
    }
    public void onBackPressed()
    {
        SnackbarManager.show(
                Snackbar.with(this)
                        .text("튜토리얼을 완료해주세요!"));
    }
}