package com.pebble.daedeokms.chat;

import android.content.*;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.*;
import org.apache.http.message.*;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pebble.daedeokms.R;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends ActionBarActivity
{
    private String id;
    private String chat;
    private String chatdata;
    private EditText cd;
    private TextView tv;
    private ScrollView mScrollview;
    private CountDownTimer chattimer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
                scroll();
            }
        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        id = intent.getExtras().getString("nicname");

        cd = (EditText) findViewById(R.id.cd);
        tv = (TextView) findViewById(R.id.tv);

        mScrollview = (ScrollView) findViewById(R.id.scroll);

        chattimer = new CountDownTimer(1000000000*1000000000,2000) { //_timer 객체에 10*1000 밀리초(10초) 가 1000밀리초마다 1씩달게 합니다.
            public void onTick(long millisUntilFinished)
            {
                reload();
                scroll();
            }
            public void onFinish()
            {
                chattimer = null;
            }
        };
    }
    public void onStart()
    {
        super.onStart();
        try
        {
            HttpClient hc = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://pjookim.com/school/chat.php");

            List<NameValuePair> data = new ArrayList<NameValuePair>();
            // 현재 시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
// 현재 시간을 저장 한다.
            Date date = new Date(now);
// 시간 포맷으로 만든다.
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm");
            String time = sdfNow.format(date);
            data.add(new BasicNameValuePair("chat", "\n" + id + "님이 접속하셨습니다. - " + time));
            post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
            hc.execute(post);

            reload();
            cd.requestFocus();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    scroll();
                }
            }, 1000); // 1초후 실행
        } catch (Exception e) {
            e.getMessage();
            scroll();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try
        {
            HttpClient hc = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://pjookim.com/school/chat.php");

            List<NameValuePair> data = new ArrayList<NameValuePair>();
            // 현재 시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
// 현재 시간을 저장 한다.
            Date date = new Date(now);
// 시간 포맷으로 만든다.
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm");
            String time = sdfNow.format(date);
            data.add(new BasicNameValuePair("chat","\n" + id + "님이 퇴장하셨습니다. - " + time));
            post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
            hc.execute(post);
            reload();
            chattimer.onFinish();
            chattimer = null;
        }
        catch (Exception e)
        {
            e.getMessage();
            scroll();
        }
    }
    public void post(View v)
    {

        chat = cd.getText().toString();

        if(chat.getBytes().length <= 0){
            SnackbarManager.show(
                    Snackbar.with(this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("메시지를 입력하세요."));
        }
        else {

            chat = chat.replace("시발", "**");
            chat = chat.replace("씨발", "**");
            chat = chat.replace("ㅅㅂ", "**");
            chat = chat.replace("ㅆㅂ", "**");
            chat = chat.replace("섹", "*");
            chat = chat.replace("무현", "**");
            chat = chat.replace("개새끼", "***");
            chat = chat.replace("병신", "**");
            chat = chat.replace("창년", "**");
            chat = chat.replace("닥쳐", "**");
            chat = chat.replace("호구", "**");

            try {
                HttpClient hc = new DefaultHttpClient();

                HttpPost post = new HttpPost("http://pjookim.com/school/chat.php");

                List<NameValuePair> data = new ArrayList<NameValuePair>();
                // 현재 시간을 msec으로 구한다.
                long now = System.currentTimeMillis();
// 현재 시간을 저장 한다.
                Date date = new Date(now);
// 시간 포맷으로 만든다.
                SimpleDateFormat sdfNow = new SimpleDateFormat("aa hh:mm");
                String time = sdfNow.format(date);
                data.add(new BasicNameValuePair("chat", "\n" + id + " : " + chat + " [" + time + "]"));
                post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
                hc.execute(post);

                cd.setText("");

                StringBuilder sBuffer = new StringBuilder();

                String urlAddr = "http://pjookim.com/school/chat.txt";

                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {//Start if
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//Start if
                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        while (true) {//Start While
                            String line = br.readLine();
                            if (line == null) {//Start if
                                break;
                            }//end if
                            sBuffer.append(line + "\n");
                        }//end while
                        br.close();
                        conn.disconnect();
                    }//end if
                }//end if
                chatdata = sBuffer.toString();

                tv.setText(String.valueOf(chatdata));
                scroll();
            } catch (Exception e) {
                e.getMessage();
                scroll();
            }
        }
    }
    public void reload() // 새로고침. 쓸때는 reload();라고 이벤트치는칸에 넣어주시면 됩니다.
    {
        try
        {
            StringBuilder sBuffer = new StringBuilder();

            String urlAddr = "http://pjookim.com/school/chat.txt";

            URL url = new URL(urlAddr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null)
            {//Start if
                conn.setConnectTimeout(20000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {//Start if
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while (true)
                    {//Start While
                        String line = br.readLine();
                        if (line == null)
                        {//Start if
                            break;
                        }//end if
                        sBuffer.append(line + "\n");
                    }//end while
                    br.close();
                    conn.disconnect();
                }//end if
            }//end if
            chatdata = sBuffer.toString();

            tv.setText(String.valueOf(chatdata));
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
    public void scroll(){
        mScrollview.post(new Runnable() {
            @Override
            public void run() {
                mScrollview.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
