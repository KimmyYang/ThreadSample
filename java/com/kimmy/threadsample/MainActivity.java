package com.kimmy.threadsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.os.Messenger;


public class MainActivity extends Activity {

    private LooperThread mLooper = null;
    private Messenger mMyMessager = null;
    private Messenger mMessager = null;
    private MyHandler mMyHandler = null;
    private Button sendMsgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();

        //looper handle
        mLooper = new LooperThread();
        mLooper.start();
        mMessager = new Messenger(mLooper.getHandler());

        //my handler
        mMyHandler = new MyHandler();
        mMyMessager = new Messenger(mMyHandler);
    }
    private void initWidget(){
        sendMsgBtn = (Button)findViewById(R.id.sendLooperMsgBtn);
        sendMsgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
               Message msg = Message.obtain();
               if(mLooper!= null && mLooper.isInit()==false)msg.replyTo = mMyMessager;
                try {
                    Log.i(GlobalType.TAG, "Send ACTION_SEND_MSG");
                    msg.what = GlobalType.ACTION_SEND_MSG;
                    mMessager.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            //do something
            int action = msg.what;
            if (action == GlobalType.ACTION_ACK) {
                Log.i(GlobalType.TAG, "Receive ACTION_ACK");
            }
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
