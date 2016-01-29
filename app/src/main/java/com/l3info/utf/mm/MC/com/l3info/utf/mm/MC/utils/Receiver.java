package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils;

import android.content.Context;
import android.content.Intent;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui.MainActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by mm on 27/01/16.
 */
public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.putExtras(intent.getExtras());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }
}