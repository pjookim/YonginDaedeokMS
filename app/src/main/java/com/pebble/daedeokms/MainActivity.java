package com.pebble.daedeokms;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pebble.daedeokms.bap.BapActivity;
import com.pebble.daedeokms.chat.ChatEnterActivity;
import com.pebble.daedeokms.council.CouncilActivity;
import com.pebble.daedeokms.info.SchoolInfo;
import com.pebble.daedeokms.board.Notice;
import com.pebble.daedeokms.schedule.ScheduleActivity;
import com.pebble.daedeokms.settings.SettingsActivity;
import com.pebble.daedeokms.timetable.TimeTableActivity;
import com.pebble.daedeokms.todaylist.TodayList;
import com.pebble.daedeokms.tool.BapTool;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import toast.library.meal.MealLibrary;

public class MainActivity extends ActionBarActivity {
    ConnectivityManager cManager;
    NetworkInfo mobile;
    NetworkInfo wifi;

    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawer;

    LinearLayout container;

    Calendar mCalendar;

    static TextView text;

    private int ver = 1;

    private ProgressDialog dialog;
    String xml;
    private Appendable sBuffer;

    private static final int MSG_TIMER_EXPIRED = 1;
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;

    private static int ONE_MINUTE = 5626;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        int firstviewshow = preference.getInt("First", 0);
        if (firstviewshow != 1) {
            Intent intent = new Intent(MainActivity.this, Tutorial.class);
            startActivity(intent);
        }
        else {
            startActivity(new Intent(this, Splash.class));
        }

        /*
        Intent intent2 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent2, 0);
        try
        {
            // 내일 아침 8시 10분에 처음 시작해서, 24시간 마다 실행되게
            Date tomorrow = new SimpleDateFormat("hh:mm:ss").parse("10:07:00");
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tomorrow.getTime(), 24 * 60 * 60 * 1000, sender);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, ChatEnterActivity.class));
            }
        });

        cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (mobile.isConnected() || wifi.isConnected()) {
            Dialog dialog = new SongDialog(this);
            dialog.show();
        }
        else {

        }

        /*try {
            URL url = new URL("http://pjookim.com/ver.txt");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String str = null;
            while((str = reader.readLine()) != null){
                sb.append(str);
            }

            text.setText(sb.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String version;
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = i.versionName;
        } catch (Exception e) {
            version = "";
        }

        SharedPreferences pref = getSharedPreferences("pref", MainActivity.MODE_PRIVATE); // UI 상태를 저장합니다.
        SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니 다
        editor.putString("check_version", version); // 저장할 값들을 입력 합니다.
        editor.commit(); // 저장합니다.

        String check_version = pref.getString("check_version", "");//
        String check_status = pref.getString("check_status", "");
        if (!check_version.equals(check_status)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("로그인");
            builder.setCancelable(false);

            *dialog.xml 읽어들이기
            //Layout 리소스를 로드할 수 있는 객체
            LayoutInflater inflater = getLayoutInflater();

            //"/res/layout/dialog.xml" 파일을 로드하기
            //--> "OK"버튼이 눌러지면, 이 객체에 접근해서 포함된 EditText객체를 취득
            final View loginView = inflater.inflate(R.layout.dialog, null);

            //Dialog에 Message 대신, XML 레이아웃을 포함시킨다.
            builder.setView(loginView);


            *취소버튼 처리
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "CANCEL 눌러짐", Toast.LENGTH_SHORT).show();
                }
            });


            *확인버튼 처리
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create();
            builder.show();
        }
    */
            /*

            AlertDialog alert = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("공!지!")
                    .setMessage()
                    .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                .setNegativeButton("더 이상 보지 않기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String version;
                                try {
                                    PackageInfo i = getPackageManager()
                                            .getPackageInfo(getPackageName(), 0);
                                    version = i.versionName;
                                } catch (Exception e) {
                                    version = "";
                                }
                                SharedPreferences pref =
                                        getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
                                SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다
                                editor.putString("check_status", version);
                                editor.commit(); // 저장합니다.
                                dialog.cancel();
                            }
                        }).show();}
*/

        }

    public View getTodayBapData() {
        View mView = getLayoutInflater().inflate(R.layout.activity_main_cardview_bap, null);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        BapTool.restoreBapDateClass mData =
                BapTool.restoreBapData(this, year, month, day);

        if (!mData.isBlankDay) {
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

            LinearLayout todayBapLayout = (LinearLayout) mView.findViewById(R.id.todayBapLayout);
            todayBapLayout.setVisibility(View.VISIBLE);

            TextView todayBapType = (TextView) mView.findViewById(R.id.todayBapType);
            TextView todayBapData = (TextView) mView.findViewById(R.id.todayBapData);

            /**
             * hour : 0~23
             *
             * 0~13 : Lunch
             * 14~23 : Dinner
             */
            String mTodayMeal;
            {
                todayBapType.setText(R.string.today_lunch);
                mTodayMeal = mData.Lunch;
                todayBapData.setText(!MealLibrary.isMealCheck(mTodayMeal) ? getResources().getString(R.string.no_data_lunch) : mTodayMeal);
            }
        }

        return mView;
    }
    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            //Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            //calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 13, 0, 0);


            Date tomorrow = null;
            try {
                tomorrow = new SimpleDateFormat("hh:mm:ss").parse("13:57:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //알람 예약
            //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, tomorrow.getTime(), 24 * 60 * 60 * 1000, sender);
        }
    }

    /*
    public View getTodayTimeTable() {
        View mView = getLayoutInflater().inflate(R.layout.activity_main_cardview_timetable, null);

        Preference mPref = new Preference(getApplicationContext());

        int mGrade = mPref.getInt("myGrade", -1);
        int mClass = mPref.getInt("myClass", -1);

        if (mGrade == -1 || mClass == -1) {
            return mView;
        }

        int DayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (DayOfWeek > 1 && DayOfWeek < 7) {
            DayOfWeek -= 2;
        } else {
            return mView;
        }

        boolean mFileExists = new File(TimeTableTool.mFilePath + TimeTableTool.TimeTableDBName).exists();
        if (!mFileExists)
            return mView;

        String mTimeTable = "";
        LinearLayout todayTimeTableLayout = (LinearLayout) mView.findViewById(R.id.todayTimeTableLayout);
        TextView todayOfWeek = (TextView) mView.findViewById(R.id.todayOfWeek);
        TextView todayTimeTable = (TextView) mView.findViewById(R.id.todayTimeTable);

        Database mDatabase = new Database();
        mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, TimeTableTool.TimeTableDBName, TimeTableTool.tableName, "");

        Cursor mCursor = mDatabase.getData(TimeTableTool.tableName, "*");

        mCursor.moveToPosition((DayOfWeek * 7) + 1);

        for (int period = 1; period <= 7; period++) {
            String mSubject;

            if (mGrade == 1) {
                mSubject = mCursor.getString((mClass * 2) - 2);
//                mRoom = mCursor.getString((mClass * 2) - 1);
            } else if (mGrade == 2) {
                mSubject = mCursor.getString(18 + (mClass * 2));
//                mRoom = mCursor.getString(19 + (mClass * 2));
            } else {
                mSubject = mCursor.getString(39 + mClass);
//                mRoom = null;
            }

//            if (mSubject != null && !mSubject.isEmpty()
//                    && mSubject.indexOf("\n") != -1)
//                mSubject = mSubject.replace("\n", "(") + ")";

            String tmp = Integer.toString(period) + ". " + mSubject;
            mTimeTable += tmp;

            if (mCursor.moveToNext()) {
                mTimeTable += "\n";
            }
        }

        todayTimeTableLayout.setVisibility(View.VISIBLE);
        todayOfWeek.setText(String.format(getString(R.string.today_timetable), TimeTableTool.mDisplayName[DayOfWeek]));
        todayTimeTable.setText(mTimeTable);

        return mView;
    }
    */

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

    public void Chat(View mView) {
        startActivity(new Intent(this, ChatEnterActivity.class));
    }

    public void Web(View mView) {
        Intent myIntent = new Intent
                (Intent.ACTION_VIEW, Uri.parse
                        ("http://www.yongindaedeok.ms.kr/"));
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void onBackPressed()
    {
        if (mIsBackKeyPressed == false)
        {
            mIsBackKeyPressed = true;
            mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("'뒤로'버튼 한번 더 누르시면 종료됩니다."));
            startTimer();
        } else {
            mIsBackKeyPressed = false;
            if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC)))
            {
                finish();
            }
        }
    }

    private void startTimer()
    {
        mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private Handler mTimerHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMER_EXPIRED: {
                    mIsBackKeyPressed = false;
                }
                break;
            }
        }
    };
}