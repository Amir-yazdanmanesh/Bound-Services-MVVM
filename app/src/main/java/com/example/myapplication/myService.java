package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class myService extends Service {
    private static final String TAG="myService";

    private IBinder mBinder =new miBinder();
    private Handler mHandler;
    private int mprogress,mMaxVAlue;
    private Boolean mIsPaused;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler =new Handler();
        mprogress=0;
        mIsPaused=true;
        mMaxVAlue=5000;

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class miBinder extends Binder{
        myService getService(){
            return myService.this;
        }
    }

    public void startPretendLongRunningTask(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mprogress >= mMaxVAlue || mIsPaused){
                    Log.d(TAG,"run: removing callbacks");
                    mHandler.removeCallbacks(this);

                    pausePretendLongRunningTask();
                }else {
                    Log.d(TAG,"run:progress: "+ mprogress);
                    mprogress +=100;
                    mHandler.postDelayed(this,100);

                }
            }
        };
        mHandler.postDelayed(runnable,100);
    }

    public void pausePretendLongRunningTask() {
        mIsPaused = true
                ;
    }
    public void unPausePretendLongRunningTask() {
        mIsPaused = false
        ;
        startPretendLongRunningTask();
    }
    public Boolean getmIsPaused(){
        return mIsPaused;
    }
    public int getProgress(){
        return mprogress;
    }
    public int getMaxValue(){
        return mMaxVAlue;
    }

    public void resetTask(){
        mprogress=0;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}
