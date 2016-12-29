package com.wateww.jawaposdownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "HEHEHE", Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, DownloadActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
