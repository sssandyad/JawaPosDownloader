package com.wateww.jawaposdownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        context.startActivity(new Intent(context,
                DownloadActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
