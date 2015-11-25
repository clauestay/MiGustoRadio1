package com.yuki91.radiobase.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Familia on 16/11/2015.
 */
public class Conectividad extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LiveConnectivityManager.singleton(context).notifyConnectionChange();
    }
}
