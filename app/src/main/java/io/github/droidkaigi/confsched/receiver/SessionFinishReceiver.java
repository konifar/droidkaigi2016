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
import io.github.droidkaigi.confsched.activity.SessionFeedbackActivity;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.LocaleUtil;
import io.github.droidkaigi.confsched.util.PrefUtil;

public class SessionFinishReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean shouldNotify = PrefUtil.get(context, PrefUtil.KEY_NOTIFICATION_SETTING, true);
        if (!shouldNotify) {
            return;
        }

        // bug fix for issue#304
        LocaleUtil.initLocale(context);

        Session session = Parcels.unwrap(intent.getParcelableExtra(Session.class.getSimpleName()));

        // launch SessionFeedbackActivity stacked with MainActivity
        Intent sessionFeedbackIntent = SessionFeedbackActivity.createIntent(context, session);
        Intent sessionDetailIntent = SessionDetailActivity.createIntent(context, session);
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent[] intents = {mainIntent, sessionDetailIntent, sessionFeedbackIntent};
        // use large number to avoid conflict with AlarmUtil
        int requestCode = session.id + 100000;
        PendingIntent pendingIntent = PendingIntent.getActivities(context, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap appIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        String title = context.getResources().getString(R.string.finish_notification_title);

        boolean headsUp = PrefUtil.get(context, PrefUtil.KEY_HEADS_UP_SETTING, true);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setLargeIcon(appIcon)
                .setContentTitle(title)
                .setContentText(session.title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(headsUp ? NotificationCompat.PRIORITY_HIGH : NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(requestCode, notification);
    }
}
