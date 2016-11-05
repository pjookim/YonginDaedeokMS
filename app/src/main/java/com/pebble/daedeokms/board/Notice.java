package com.pebble.daedeokms.board;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pebble.daedeokms.AnalyticsApplication;
import com.pebble.daedeokms.R;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjookim on 2015. 2. 20..
 */

public class Notice extends AppCompatActivity {
    private static String URL_PRIMARY = "http://www.yongindaedeok.ms.kr";
    private static String GETNOTICE = "/wah/main/bbs/board/list.htm?menuCode=16";
    private String url;
    private URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    private String BC_T;

    ArrayList<ListData> mListData = new ArrayList<>();

    SwipeRefreshLayout layout;

    private Tracker mTracker;

    @Override
    protected void onStop() {
        super.onStop();
        if ( progressDialog != null)
            progressDialog.dismiss();

        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_notice);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Notice");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.yongindaedeok.ms.kr/wah/main/bbs/board/list.htm?menuCode=16");
                i.setData(u);
                startActivity(i);
            }
        });

        layout = (SwipeRefreshLayout) findViewById(R.id.layout);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BBSAdapter = new BBSListAdapter(Notice.this);
                try {
                    process();
                    BBSAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }
                layout.setRefreshing(false);
            }
        });

        BBSList = (ListView)findViewById(R.id.listView);
        BBSAdapter = new BBSListAdapter(this);
        BBSList.setAdapter(BBSAdapter);
        BBSList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        ListData mData = mListData.get(position);
                        String URL_BCS = mData.mUrl;

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PRIMARY + URL_BCS)));
                    }
                });


        url = URL_PRIMARY + GETNOTICE;

        if(isInternetCon()) {
            Toast.makeText(Notice.this, R.string.no_internet_stop, Toast.LENGTH_SHORT).show();
            finish();
        }else{
            try {
                process();
                BBSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");
            }
        }
    }


    private void process() throws IOException {

        new Thread() {

            @Override
            public void run() {

                Handler Progress = new Handler(Looper.getMainLooper());
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Notice.this, "", getString(R.string.parsing_notice));
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8"));
                    source.fullSequentialParse();
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV);

                for(int arrnum = 0;arrnum < tabletags.size(); arrnum++){


                    if(tabletags.get(arrnum).toString().equals("<div class=\"Board_List\">")) {
                        BBSlocate = arrnum;
                        Log.d("BBSLOCATES", arrnum+"");
                        break;
                    }
                }

                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0);


                for(int C_TR = 0; C_TR < BBS_TABLE.getAllElements(HTMLElementName.TR).size();C_TR++){
                    try {
                        Element BBS_TR = (Element) BBS_TABLE.getAllElements(HTMLElementName.TR).get(C_TR);
                        Element BC_TYPE = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0);
                        Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(1);
                        Element BC_a = (Element) BC_info.getAllElements(HTMLElementName.A).get(0);
                        String BCS_url = BC_a.getAttributeValue("href");

                        Element BC_writer = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(2);
                        Element BC_date = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(3);

                        String BCS_title = BC_a.getContent().toString();
                        String BCS_type = BC_TYPE.getContent().toString();
                        String BCS2_writer = BC_writer.getContent().toString();
                        String BCS_writer = BCS2_writer.replace("\n", "");
                        String BCS_date = BC_date.getContent().toString();


                        mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer, BCS_date));
                        Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);

                    }catch(Exception e){
                        Log.d("BCSERROR",e+"");
                    }
                }
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BBSAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }, 0);
            }

        }.start();
    }

    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return !mobile.isConnected() && !wifi.isConnected();
    }

    class ViewHolder {

        public TextView mType;
        public TextView mTitle;
        public TextView mUrl;
        public TextView mWriter;
        public TextView mDate;
    }


    public class BBSListAdapter extends BaseAdapter {
        private Context mContext = null;

        public BBSListAdapter(Context mContext) {
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.itemstyle, null);

                holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
                holder.mWriter = (TextView) convertView.findViewById(R.id.item_writer);
                holder.mDate = (TextView) convertView.findViewById(R.id.item_date);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);


            if(mData.mType.equals("<img src=\"/tp/board/type001/images/blt_notice.gif\" width=\"22\" height=\"11\" alt=\"공지\">")){
                holder.mTitle.setText(Html.fromHtml("<font color=#7B1FA2>[공지] </font>" +mData.mTitle)); //"공지" 의 색깔을 부분적으로 약간 진하게 수정.
            }else {
                holder.mTitle.setText(mData.mTitle);
            }
            holder.mDate.setText(mData.mDate);

            return convertView;

        }


    }

    public class ListData {

        public String mType;
        public String mTitle;
        public String mUrl;
        public String mWriter;
        public String mDate;


        public ListData()  {


        }

        public ListData(String mType,String mTitle,String mUrl,String mWriter,String mDate)  {
            this.mType = mType;
            this.mTitle = mTitle;
            this.mUrl = mUrl;
            this.mWriter = mWriter;
            this.mDate = mDate;

        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }
}
