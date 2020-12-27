package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG="MainActivity2";

    TextView mTextView;
    ProgressBar mProgressBar;
    private myService mservice;
    private MainActivityViewModel viewModel;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mProgressBar = findViewById(R.id.progressBar);
        mTextView = findViewById(R.id.textView);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        btn=findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUpdate();
            }
        });

        viewModel.getBonder().observe(this, new Observer<myService.miBinder>() {
            @Override
            public void onChanged(myService.miBinder miBinder) {
                if (miBinder !=null){
                    Log.d(TAG,"onChanged: connected" );
                    mservice=miBinder.getService();
                }else
                {
                    Log.d(TAG,"onChanged: unbound");
                    mservice=null;
                }
            }
        });

        viewModel.getIsProgressUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean aBoolean) {
                final Handler handler =new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (aBoolean){
                            if (viewModel.getBonder().getValue() != null){
                                if (mservice.getProgress() == mservice.getMaxValue()){
                                    viewModel.setIsUpdating(false);
                                }
                                mProgressBar.setProgress(mservice.getProgress());
                                mProgressBar.setMax(mservice.getMaxValue());
                                String progress = String.valueOf(100*mservice.getProgress()
                                        / mservice.getMaxValue())+"%";
                                mTextView.setText(progress);
                                handler.postDelayed(this,100);
                            }
                        }else {
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if (aBoolean){
                    btn.setText("pause");
                    handler.postDelayed(runnable,100);
                }else {
                    if (mservice.getProgress()==mservice.getMaxValue()){
                        btn.setText("restart");
                    }else {
                        btn.setText("start");
                    }
                }

            }
        });
    }

    private void toggleUpdate() {
        if (mservice !=null){
            if (mservice.getProgress() == mservice.getMaxValue()){
                mservice.resetTask();
                btn.setText("start");
            }else
            {
                if (mservice.getmIsPaused()){
                    mservice.unPausePretendLongRunningTask();
                    viewModel.setIsUpdating(true);
                }else {
                    mservice.pausePretendLongRunningTask();
                    viewModel.setIsUpdating(false);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }
    public void startService(){
        Intent servIntent = new Intent(this,myService.class);
         startService(servIntent);

        bindService();
    }
    public void bindService(){
        Intent servIntent = new Intent(this,myService.class);
    bindService(servIntent,viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }


}