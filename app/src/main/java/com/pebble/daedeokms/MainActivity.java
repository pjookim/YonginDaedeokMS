package com.pebble.daedeokms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pebble.daedeokms.bap.BapActivity;
import com.pebble.daedeokms.council.CouncilActivity;
import com.pebble.daedeokms.info.SchoolInfo;
import com.pebble.daedeokms.board.Notice;
import com.pebble.daedeokms.schedule.ScheduleActivity;
import com.pebble.daedeokms.settings.SettingsActivity;
import com.pebble.daedeokms.timetable.TimeTableActivity;
import com.pebble.daedeokms.todaylist.TodayList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Created by pjookim on 2015. 1. 12..
 */

public class MainActivity extends AppCompatActivity {
    ConnectivityManager cManager;
    NetworkInfo mobile;
    NetworkInfo wifi;

    private static final int MSG_TIMER_EXPIRED = 1;
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private Tracker mTracker;

    String serverStr;
    int currentVer;
    int serverVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, Splash.class));
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yongindaedeok.ms.kr/"));
                startActivity(homepage);
            }
        });

        cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (wifi.isConnected()) {
            Dialog dialog = new DaedeokDialog(this);
            dialog.show();

            run();
            try {
                currentVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            serverVer = Integer.parseInt(serverStr);
            if (currentVer < serverVer) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.update_alert_title)
                        .setMessage(R.string.update_alert_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.pebble.daedeokms")));
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog udialog = builder.create();
                udialog.show();
            }
        } else if (mobile.isConnected()) {
            Dialog dialog = new DaedeokDialog(this);
            dialog.show();
        }
    }

    public void run() {
        StringBuilder text = new StringBuilder();
        text.append("");
        try {
            URL url = new URL("http://pjookim.me/school/ver.txt");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(1000); // 1초 동안 인터넷 연결을 실패할경우 Fall 처리
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    for (; ; ) {
                        String line = br.readLine();
                        if (line == null) break;
                        text.append(line + "\n");
                    }
                    br.close();
                }
                conn.disconnect();
            }

        } catch (Exception ex) {
        }
        String tempResult = text.toString();
        serverStr = tempResult.replace("\n", "");
    }

    public void Bap(View mView) {
        startActivity(new Intent(this, BapActivity.class));
    }

    public void TimeTable(View mView) {
        startActivity(new Intent(this, TimeTableActivity.class));
    }

    public void Schedule(View mView) {
        startActivity(new Intent(this, ScheduleActivity.class));
    }

    public void Info(View mView) {
        startActivity(new Intent(this, SchoolInfo.class));
    }

    public void TodayList(View mView) {
        startActivity(new Intent(this, TodayList.class));
    }

    public void Notice(View mView) {
        startActivity(new Intent(this, Notice.class));
    }

    public void Council(View mView) {
        startActivity(new Intent(this, CouncilActivity.class));
    }

    public void Web(View mView) {
        Intent homepage = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yongindaedeok.ms.kr/"));
        startActivity(homepage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_notice:
                if (mobile.isConnected() || wifi.isConnected()) {
                    Dialog dialog = new DaedeokDialog(this);
                    dialog.show();
                } else {
                    SnackbarManager.show(
                            Snackbar.with(this)
                                    .text(R.string.need_network));
                    startTimer();
                }
                return true;
            case R.id.menu_developer:
                CustomDialog.Builder builder = new CustomDialog.Builder(this, R.string.developer_title, android.R.string.ok);
                builder.content(getString(R.string.developer_msg));
                CustomDialog customDialog = builder.build();
                customDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void onBackPressed() {
        if (!mIsBackKeyPressed) {
            mIsBackKeyPressed = true;
            mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text(R.string.before_close));
            startTimer();
        } else {
            mIsBackKeyPressed = false;
            if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) {
                finish();
            }
        }
    }

    private void startTimer() {
        mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMER_EXPIRED: {
                    mIsBackKeyPressed = false;
                }
                break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}