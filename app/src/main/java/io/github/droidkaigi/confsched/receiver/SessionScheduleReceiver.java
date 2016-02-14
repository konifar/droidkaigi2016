package io.github.droidkaigi.confsched.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.parceler.Parcels;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.MainActivity;
import io.github.droidkaigi.confsched.activity.SessionDetailActivity;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.PrefUtil;

public class SessionScheduleReceiver extends BroadcastReceiver {

    private static final int REMIND_MINUTES = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean shouldNotify = PrefUtil.get(context, PrefUtil.KEY_NOTIFICATION_SETTING, true);
        if (!shouldNotify) {
            return;
        }

        Session session = Parcels.unwrap(intent.getParcelableExtra(Session.class.getSimpleName()));

        // launch SessionDetailsActivity stacked with MainActivity
        Intent sessionDetailIntent = SessionDetailActivity.createIntent(context, session);
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent[] intents = {mainIntent, sessionDetailIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap appIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        String title = context.getResources().getQuantityString(
                R.plurals.schedule_notification_title, REMIND_MINUTES, REMIND_MINUTES);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_access_time_grey_600_24dp)
                .setLargeIcon(appIcon)
                .setContentTitle(title)
                .setContentText(session.title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Using fixed ID to avoid to show multiple notifications
        manager.notify(0, notification);
    }
}
