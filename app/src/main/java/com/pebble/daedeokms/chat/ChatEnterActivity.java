package com.pebble.daedeokms.chat;

import android.os.*;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import android.content.*;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pebble.daedeokms.R;

public class ChatEnterActivity extends ActionBarActivity
{
    public static final String KEY_MY_PREFERENCE = "my_preference";
    private String id;
    private EditText eid;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_enter);

        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);

        int firstviewshow = preference.getInt("NoName", 0);

        eid = (EditText) findViewById(R.id.name);

        SharedPreferences prefs = getSharedPreferences("chat", MODE_PRIVATE);
        String text = prefs.getString(KEY_MY_PREFERENCE, "");

        // 첫번째 인자는 키, 두번째 인자는 키에 대한 데이터가 존재하지 않을 경우의 디폴트값
        final EditText edit = (EditText) findViewById(R.id.name);
        edit.setText(text);

        if (firstviewshow != 0) {

            id = eid.getText().toString();

            Intent intent = new Intent(this,ChatActivity.class);
            intent.putExtra("nicname", id);
            startActivity(intent);
            finish();
        }
    }
    public void go(View v) {
        id = eid.getText().toString();

        if (id.getBytes().length <= 0) {
            SnackbarManager.show(
                    Snackbar.with(this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("닉네임을 입력해 주세요"));
        }
        else if (id.getBytes().length >= 21) {
            SnackbarManager.show(
                    Snackbar.with(this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("닉네임의 길이가 너무 깁니다"));
        }
        else {

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("nicname", id);
            startActivity(intent);

            int infoFirst = 1;

            SharedPreferences a = getSharedPreferences("a", MODE_PRIVATE);

            SharedPreferences.Editor editor = a.edit();

            editor.putInt("NoName", infoFirst);

            editor.commit();

            finish();
        }
    }
    protected void onStop() {
        super.onStop();
        EditText editText = (EditText) findViewById(R.id.name);
        String text = editText.getText().toString();

        // 데이타를저장합니다.
        SharedPreferences prefs = getSharedPreferences("chat", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MY_PREFERENCE, text);
        editor.commit();
    }
}