package com.pebble.daedeokms;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.net.*;

public class DaedeokDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;

    String text;

    TextView tv;

    public DaedeokDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.yh);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork()
                .build());

        try {
            StringBuffer sBuffer = new StringBuffer();
            String URL = "http://pjookim.com/school/dialog_r.txt";
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(20000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        sBuffer.append(line + "\n");
                    }
                    br.close();
                    conn.disconnect();
                }
            }
            text = sBuffer.toString();
            tv.setText(text);
        } catch (Exception e) {
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                dismiss();
                break;
        }
    }
}