package com.pebble.daedeokms.council;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.io.File;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pebble.daedeokms.AnalyticsApplication;
import com.pebble.daedeokms.spreadsheets.GoogleSheetTask;
import com.pebble.daedeokms.R;
import com.pebble.daedeokms.tool.Database;
import com.pebble.daedeokms.tool.Preference;
import com.pebble.daedeokms.tool.TimeTableTool;
import com.pebble.daedeokms.tool.Tools;

/**
 * Created by pjookim on 2016. 7. 26..
 */

public class CouncilActivity extends AppCompatActivity {
    public static final String NoticeDBName = "StudentNotice.db";
    public static final String NoticeTableName = "NoticeInfo";

    boolean isAdmin;

    ListView mListView;
    NoticeAdapter mAdapter;
    ProgressDialog mDialog;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_council);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("CouncilActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        isAdmin = new Preference(getApplicationContext()).getBoolean("userAdmin_1", false);

        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new NoticeAdapter(this);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showNoticeData(true);
            }
        });

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.mFab);
        if (isAdmin) {
            mFab.setVisibility(View.VISIBLE);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdmin) {
                    //startActivity(new Intent(getApplicationContext(), NoticeSendActivity.class).putExtra("userAdmin_1", true));
                } else {
                    Snackbar.make(view, R.string.user_info_require_message, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        showNoticeData(false);
    }

    private void showNoticeData(boolean forceUpdate) {
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();

        if (Tools.isOnline(getApplicationContext())) {
            if (Tools.isWifi(getApplicationContext()) || forceUpdate) {
                getNoticeDownloadTask mTask = new getNoticeDownloadTask();
                mTask.execute("https://docs.google.com/spreadsheets/d/1lrL3m0OuZj_LC-1v4QEEhwpsGkuq6pkj06AIKImb3rM/pubhtml?gid=2081220100&single=true");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.no_wifi_title);
                builder.setMessage(R.string.no_wifi_msg);
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        offlineData();
                    }
                });
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getNoticeDownloadTask mTask = new getNoticeDownloadTask();
                        mTask.execute("https://docs.google.com/spreadsheets/d/1lrL3m0OuZj_LC-1v4QEEhwpsGkuq6pkj06AIKImb3rM/pubhtml?gid=2081220100&single=true");
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        } else {
            offlineData();
        }
    }

    private void offlineData() {
        if (new File(TimeTableTool.mFilePath + NoticeDBName).exists()) {
            showListViewDate();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }

        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    class getNoticeDownloadTask extends GoogleSheetTask {
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload() {
            mDialog = new ProgressDialog(CouncilActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_title));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            new File(TimeTableTool.mFilePath + NoticeDBName).delete();
            mDatabase = new Database();

            this.startRowNumber = 0;
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (mDatabase != null)
                mDatabase.release();

            showListViewDate();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row) {
            if (startRowNumber == position) {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                // remove deviceId
                for (int i = 0; i < row.length - 1; i++) {
                    Column.append(row[i]);
                    Column.append(" text, ");
                }

                mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, NoticeDBName, NoticeTableName, Column.substring(0, Column.length() - 2));
            } else {
                int length = row.length;
                for (int i = 0; i < length - 1; i++) {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(NoticeTableName);
            }
        }
    }

    private void showListViewDate() {
        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, NoticeDBName);
        Cursor mCursor = mDatabase.getData(NoticeTableName, "*");

        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToNext();

            String date = mCursor.getString(1);
            String title = mCursor.getString(2);
            String message = mCursor.getString(3);

            mAdapter.addItem(title, message, date);
        }

        mAdapter.notifyDataSetChanged();

        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}

