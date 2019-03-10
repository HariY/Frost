package frost.test.com.frosttest.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class Frost extends Application {


    private int cacheSizeInKb;
    private Frost.NetworkChangeReceiver networkListener = null;
    private NetworkChangeCallBack networkCallBack = null;
    @Override
    public void onCreate() {
        super.onCreate();
        calculateEfortableCacheSize();
        registerNetworkListener();

    }
    private void calculateEfortableCacheSize(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClassInMb = am.getMemoryClass();
        cacheSizeInKb = (1024 * memoryClassInMb)/8;
    }


    public int getCacheSize(){
        return cacheSizeInKb;
    }


    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String statusMsg = null;

            try {
                int code = NetworkUtil.getConnectivityStatus(context);
                statusMsg = NetworkUtil.getConnectivityStatusString(code);

                if (networkCallBack != null) {
                    if (NetworkUtil.TYPE_NOT_CONNECTED == code) {
                        networkCallBack.onNetWorkDisconnected(statusMsg);
                    } else if ((NetworkUtil.TYPE_WIFI == code)
                            || (NetworkUtil.TYPE_MOBILE == code)) {
                        networkCallBack.onNetWorkConnected(statusMsg, code);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                statusMsg = null;
            }
        }
    }

    public void registerNetworkListener() {
        if (networkListener == null) {
            networkListener = new Frost.NetworkChangeReceiver();
            IntentFilter networkChangeFilter = new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkListener, networkChangeFilter);
        }
    }

    public void unregisterNetworkListener() {
        if (networkListener != null) {
            unregisterReceiver(networkListener);
            networkListener = null;
        }
    }

    public void setNetworkCallBack(NetworkChangeCallBack networkCallBack) {
        this.networkCallBack = networkCallBack;
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterNetworkListener();
    }

}
