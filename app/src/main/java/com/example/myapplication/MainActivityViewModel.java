package com.example.myapplication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG="MainActivityViewModel";

    private MutableLiveData<Boolean> mIsProgressUpdating = new MutableLiveData<>();
    private MutableLiveData<myService.miBinder> mbinder = new MutableLiveData<>();

    public ServiceConnection serviceConnection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: conected to service");
            myService.miBinder miBinder =(myService.miBinder) iBinder;
            mbinder.postValue(miBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mbinder.postValue(null);
        }
    };
    public LiveData<Boolean> getIsProgressUpdating(){
        return mIsProgressUpdating;
    }

    public LiveData<myService.miBinder> getBonder(){
        return mbinder;
    }
    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

     public void setIsUpdating(Boolean isUpdating){
        mIsProgressUpdating.postValue(isUpdating);
     }
}
