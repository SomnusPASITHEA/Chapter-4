package com.bytedance.clockapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.bytedance.clockapplication.widget.Clock;

public class MainActivity extends AppCompatActivity {

    private View mRootView;
    private Clock mClockView;
    /**private Handler drawHandler = new Handler(){};
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mClockView.postInvalidate();
        }
    };**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root);
        mClockView = findViewById(R.id.clock);
        new RefreshViewThread("FirstRefreshThread").start();
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClockView.setShowAnalog(!mClockView.isShowAnalog());
            }
        });
    }
    public class RefreshViewThread extends HandlerThread implements Handler.Callback{
        public static final int MSG_REFRESH = 1;
        private Handler refreshWorkingHandler;
        public RefreshViewThread(String name){
            super(name);
        }
        public RefreshViewThread(String name,int priority){
            super(name,priority);
        }
        @Override
        protected void onLooperPrepared(){
            refreshWorkingHandler = new Handler(getLooper(),this);
            refreshWorkingHandler.sendEmptyMessage(MSG_REFRESH);
        }
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REFRESH:
                    mClockView.postInvalidate();
                    refreshWorkingHandler.sendEmptyMessageDelayed(MSG_REFRESH,500);
            }
            return true;
        }
    }

}
