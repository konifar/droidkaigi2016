package io.github.droidkaigi.confsched.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.receiver.SessionScheduleReceiver;

@Singleton
public class AlarmUtil {

    private static final long REMIND_DURATION_MINUTES_FOR_START = TimeUnit.MINUTES.toMillis(5);

    private Context context;

    @Inject
    public AlarmUtil(Context context) {
        this.context = context;
    }

    public void handleSessionAlarm(Session session) {
        if (session.checked) {
            registerSessionAlarm(session);
        } else {
            unregisterSessionAlarm(session);
        }
    }

    private void registerSessionAlarm(Session session) {
        if (!session.shouldNotify(REMIND_DURATION_MINUTES_FOR_START)) {
            return;
        }

        PendingIntent sender = buildSessionAlarmSender(session);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAtMillis = calculateStartNotifyTime(session);
        alarmManager.set(AlarmManager.RTC, triggerAtMillis, sender);
    }

    private void unregisterSessionAlarm(Session session) {
        PendingIntent sender = buildSessionAlarmSender(session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private PendingIntent buildSessionAlarmSender(Session session) {
        Intent intent = new Intent(context, SessionScheduleReceiver.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        return PendingIntent.getBroadcast(context, session.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private long calculateStartNotifyTime(Session session) {
        return session.getDisplaySTime(context).getTime() - REMIND_DURATION_MINUTES_FOR_START;
    }
}
