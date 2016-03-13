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
import android.support.v4.content.ContextCompat;

import org.parceler.Parcels;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.MainActivity;
import io.github.droidkaigi.confsched.activity.SessionDetailActivity;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.prefs.DefaultPrefs;

public class SessionScheduleReceiver extends BroadcastReceiver {

    private static final int REMIND_MINUTES = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean shouldNotify = DefaultPrefs.get(context).getNotificationFlag(true);
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

        boolean headsUp = DefaultPrefs.get(context).getHeadsUpFlag(true);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_stat_notification_vector)
                .setLargeIcon(appIcon)
                .setContentTitle(title)
                .setContentText(session.title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(headsUp ? NotificationCompat.PRIORITY_HIGH : NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(ContextCompat.getColor(context, R.color.theme500))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Using fixed ID to avoid to show multiple notifications
        manager.notify(0, notification);
    }
}
