package com.pebble.daedeokms.schedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mrengineer13.snackbar.SnackBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pebble.daedeokms.R;

public class ScheduleActivity extends ActionBarActivity {
    Toolbar mToolbar;

    ArrayList<Item> mItem = new ArrayList<Item>();
    ListView mListview;

    SnackBar.Builder mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SharedPreferences preference2 = getSharedPreferences("b", MODE_PRIVATE);
        int firstviewshow = preference2.getInt("Schedule", 0);
        if (firstviewshow != 1) {
            SnackBar.Builder mSnackBar = new SnackBar.Builder(this);
            mSnackBar.withMessage("추후 상기사항은 학교사정에 따라 변경 될 수 있습니다.");
            mSnackBar.withStyle(SnackBar.Style.INFO);
            mSnackBar.withActionMessage(getResources().getString(android.R.string.ok));
            mSnackBar.show();

            int infoFirst=1;

            SharedPreferences b = getSharedPreferences("b", MODE_PRIVATE);

            SharedPreferences.Editor editor = b.edit();

            editor.putInt("Schedule", infoFirst);

            editor.commit();
        }

        mListview = (ListView) findViewById(R.id.mListView);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long ld) {
                EntryItem mEntryItem = (EntryItem) mItem.get(position);
                SimpleDateFormat sdFormat = new SimpleDateFormat(
                        "yyyy.MM.dd (E)", Locale.KOREA);
                Calendar mCalendar = Calendar.getInstance();

                try {
                    mCalendar.setTime(sdFormat.parse(mEntryItem.mSummary));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                getTime(mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        mItem.add(new SectionItem("2016년 8월 일정"));
        mItem.add(new EntryItem("광복절", "2016.08.15 (월)", true));
        mItem.add(new EntryItem("개학일", "2016.08.16 (화)"));
        mItem.add(new EntryItem("학급회의(2, 3학년)", "2016.08.19 (금)"));
        mItem.add(new EntryItem("동아리", "2015.08.26 (금)"));
        mItem.add(new EntryItem("미래 명함 만들기 대회(2, 3학년)", "2016.08.31 (수)"));

        mItem.add(new SectionItem("2016년 9월 일정"));
        mItem.add(new EntryItem("8/26 5교시 수업(2, 3학년)", "2016.09.02 (금)"));
        mItem.add(new EntryItem("1학년 영어 듣기 평가", "2016.09.06 (화)"));
        mItem.add(new EntryItem("2학년 영어 듣기 평가", "2016.09.07 (수)"));
        mItem.add(new EntryItem("3학년 영어 듣기 평가", "2016.09.08 (목)"));
        mItem.add(new EntryItem("동아리", "2016.09.09 (금)"));
        mItem.add(new EntryItem("추석 연휴", "2016.09.14 (수)", true));
        mItem.add(new EntryItem("추석", "2016.09.15 (목)", true));
        mItem.add(new EntryItem("추석 연휴", "2016.09.16 (금)", true));
        mItem.add(new EntryItem("동아리", "2016.09.23 (금)"));
        mItem.add(new EntryItem("9/9 5교시 수업(2, 3학년)", "2016.09.30 (금)"));

        mItem.add(new SectionItem("2016년 10월 일정"));
        mItem.add(new EntryItem("개천절", "2016.10.03 (월)", true));
        mItem.add(new EntryItem("1차 지필평가(2, 3학년)", "2016.10.06 (목)"));
        mItem.add(new EntryItem("1차 지필평가(2, 3학년), 진로(1학년)", "2016.10.07 (금)"));
        mItem.add(new EntryItem("한글날", "2016.10.09 (일)", true));
        mItem.add(new EntryItem("1차 지필평가(2, 3학년), 학생 상담주간(친구 사랑주간)", "2016.10.10 (월)"));
        mItem.add(new EntryItem("1차 지필평가(2, 3학년), 학생 상담주간(친구 사랑주간)", "2016.10.11 (화)"));
        mItem.add(new EntryItem("학생 상담 주간(친구 사랑주간)", "2016.10.12 (수)"));
        mItem.add(new EntryItem("학생 상담 주간(친구 사랑주간)", "2016.10.13 (목)"));
        mItem.add(new EntryItem("학생 상담 주간, 친구 사랑의 날", "2016.10.14 (금)"));
        mItem.add(new EntryItem("학부모 상담주간 수업공개(1학년)", "2016.10.17 (월)"));
        mItem.add(new EntryItem("학부모 상담주간 수업공개(1학년)", "2016.10.18 (화)"));
        mItem.add(new EntryItem("학부모 상담주간 수업공개(2학년)", "2016.10.19 (수)"));
        mItem.add(new EntryItem("학부모 상담주간 수업공개(2, 3학년)", "2016.10.20 (목)"));
        mItem.add(new EntryItem("학부모 상담주간 수업공개(3학년), 동아리", "2016.10.21 (금)"));
        mItem.add(new EntryItem("진로 골든벨 대회", "2016.10.26 (수)"));
        mItem.add(new EntryItem("체육 대회", "2016.10.27 (목)"));
        mItem.add(new EntryItem("축제", "2016.10.28 (금)"));

        mItem.add(new SectionItem("2016년 11월 일정"));
        mItem.add(new EntryItem("10/27 1, 2교시 수업(1학년)", "2016.11.03 (목)"));
        mItem.add(new EntryItem("학급 회의(2, 3학년)", "2016.11.04 (금)"));
        mItem.add(new EntryItem("학생 회장 선거", "2016.11.07 (월)"));
        mItem.add(new EntryItem("10/27 3, 4교시 수업(1학년)", "2016.11.10 (목)"));
        mItem.add(new EntryItem("9/23 5교시 수업(2, 3학년), 10/28 1, 2교시 수업(1학년)", "2016.11.11 (금)"));
        mItem.add(new EntryItem("대학 수학 능력 시험(학교장 재량 휴업일)", "2016.11.17 (목)", true));
        mItem.add(new EntryItem("단기 방학(학교장 재량 휴업일)", "2016.11.18 (금)", true));
        mItem.add(new EntryItem("10/28 3, 4교시 수업(1학년), 친구 사랑의 날(2, 3학년)", "2016.11.25 (금)"));
        mItem.add(new EntryItem("2차 지필평가(3학년)", "2016.11.28 (월)"));
        mItem.add(new EntryItem("2차 지필평가(3학년)", "2016.11.29 (화)"));
        mItem.add(new EntryItem("2차 지필평가(3학년)", "2016.11.30 (수)"));

        mItem.add(new SectionItem("2016년 12월 일정"));
        mItem.add(new EntryItem("성매매 예방 교육(2, 3학년)", "2016.12.02 (금)"));
        mItem.add(new EntryItem("10/21 5교시 수업(2, 3학년)", "2016.12.09 (금)"));
        mItem.add(new EntryItem("2차 지필평가(2학년)", "2016.12.12 (월)"));
        mItem.add(new EntryItem("2차 지필평가(2학년)", "2016.12.13 (화)"));
        mItem.add(new EntryItem("2차 지필평가(2학년)", "2016.12.14 (수)"));
        mItem.add(new EntryItem("동아리", "2016.12.16 (금)"));
        mItem.add(new EntryItem("아동학대 및 가정폭력 예방교육", "2016.12.19 (월)"));
        mItem.add(new EntryItem("12/16 5교시 수업(2, 3학년)", "2016.12.23 (금)"));
        mItem.add(new EntryItem("성탄절", "2016.12.25 (일)", true));
        mItem.add(new EntryItem("합창 대회", "2016.12.29 (목)"));
        mItem.add(new EntryItem("학급 회의(2, 3학년)", "2016.12.30 (금)"));

        mItem.add(new SectionItem("2017년 1월 일정"));
        mItem.add(new EntryItem("신정", "2017.01.01 (금)", true));
        mItem.add(new EntryItem("겨울철 안전 사고 예방교육", "2017.01.02 (월)"));
        mItem.add(new EntryItem("졸업식, 종업식", "2017.01.04 (수)"));

        EntryAdapter adapter = new EntryAdapter(this, mItem);
        mListview.setAdapter(adapter);
    }

    private void getTime(int year, int month, int day) {
        String Text;

        try {
            Calendar myTime = Calendar.getInstance();

            long nowTime = myTime.getTimeInMillis();
            myTime.set(year, month, day);
            long touchTime = myTime.getTimeInMillis();

            long diff = (touchTime - nowTime);

            boolean isPast = false;
            if (diff < 0) {
                diff = -diff;
                isPast = true;
            }

            int diffInt = (int) (diff /= 24 * 60 * 60 * 1000);

            if (isPast)
                Text = "선택하신 날짜는 " + diffInt + "일전 날짜입니다";
            else
                Text = "선택하신 날짜까지 " + diffInt + "일 남았습니다";
        } catch (Exception ex) {
            Text = "오류가 발생하였습니다";
        }

        mSnackBar = new SnackBar.Builder(this);
        mSnackBar.withMessage(Text);
        mSnackBar.withStyle(SnackBar.Style.INFO);
        mSnackBar.withActionMessage(getResources().getString(android.R.string.ok));
        mSnackBar.show();
    }

    public interface Item {
        public boolean isSection();
    }
}
