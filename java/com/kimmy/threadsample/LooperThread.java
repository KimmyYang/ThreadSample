package com.kimmy.threadsample;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by kimmy on 15/8/12.
 */
public class LooperThread extends Thread {
    public Handler mHandler;
    public Messenger mMessenger = null;
    public Handler getHandler(){
        if(mHandler == null){
            try{
                Log.i(GlobalType.TAG,"[getHandler] wait");
                synchronized(this){
                    wait();
                }
            }catch(InterruptedException e){
                Log.e(GlobalType.TAG,"[getHandler] e="+e);
            }
        }
        Log.i(GlobalType.TAG,"[getHandler] end");
        return mHandler;
    }
    public Looper getLooper(){
        return Looper.myLooper();
    }
    public boolean isInit(){
        if(mMessenger == null)return false;
        return true;
    }
    public void run(){
        Looper.prepare();
        synchronized(this){
            Log.i(GlobalType.TAG,"[run] Create Handler");
            mHandler = new Handler(){
                public void handleMessage(Message msg){
                    //process incoming msg
                    if(mMessenger == null){
                        mMessenger = msg.replyTo;
                    }

                    switch (msg.what){
                        case GlobalType.ACTION_SEND_MSG:
                            Log.i(GlobalType.TAG,"Receive ACTION_SEND_MSG");
                            if(mMessenger!=null){
                                Message respMsg = Message.obtain();
                                respMsg.what = GlobalType.ACTION_ACK;
                                try {
                                    Log.i(GlobalType.TAG,"Send ACTION_ACK");
                                    mMessenger.send(respMsg);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            Log.i(GlobalType.TAG,"Error action = "+msg.what);
                            break;
                    }
                }
            };
            notifyAll();
        }
        Looper.loop();
    }
}
