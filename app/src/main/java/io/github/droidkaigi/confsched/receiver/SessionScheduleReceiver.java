package io.github.droidkaigi.confsched.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SessionScheduleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: show notification
        Toast.makeText(context, "It's time!", Toast.LENGTH_SHORT).show();
    }
}
