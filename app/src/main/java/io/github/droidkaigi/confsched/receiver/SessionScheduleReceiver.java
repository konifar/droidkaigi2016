package io.github.droidkaigi.confsched.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.activity.MainActivity;
import io.github.droidkaigi.confsched.activity.SessionDetailActivity;
import io.github.droidkaigi.confsched.model.Session;

public class SessionScheduleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Session session = Parcels.unwrap(intent.getParcelableExtra(Session.class.getSimpleName()));

        // launch SessionDetailsActivity stacked with MainActivity
        Intent sessionDetailIntent = SessionDetailActivity.createIntent(context, session);
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent[] intents = {mainIntent, sessionDetailIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap appIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_access_time_grey_600_24dp)
                .setLargeIcon(appIcon)
                .setContentTitle(context.getString(R.string.schedule_notification_title, 5))
                .setContentText(session.title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Using fixed ID to avoid to show multiple notifications
        manager.notify(0, notification);
    }
}
