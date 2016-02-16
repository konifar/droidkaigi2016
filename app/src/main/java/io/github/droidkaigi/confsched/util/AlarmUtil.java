package io.github.droidkaigi.confsched.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.receiver.SessionScheduleReceiver;

public class AlarmUtil {

    private static final long REMIND_DURATION_MINUTES_FOR_START = TimeUnit.MINUTES.toMillis(5);

    public static void handleSessionAlarm(Context context, Session session) {
        if (session.checked) {
            registerSessionAlarm(context, session);
        } else {
            unregisterSessionAlarm(context, session);
        }
    }

    public static void registerSessionAlarm(Context context, Session session) {
        if (!session.shouldNotify(REMIND_DURATION_MINUTES_FOR_START)) {
            return;
        }

        PendingIntent sender = buildSessionAlarmSender(context, session);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAtMillis = calculateStartNotifyTime(session, context);
        alarmManager.set(AlarmManager.RTC, triggerAtMillis, sender);
    }

    public static void unregisterSessionAlarm(Context context, Session session) {
        PendingIntent sender = buildSessionAlarmSender(context, session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private static PendingIntent buildSessionAlarmSender(Context context, Session session) {
        Intent intent = new Intent(context, SessionScheduleReceiver.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        return PendingIntent.getBroadcast(context, session.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private static long calculateStartNotifyTime(Session session, Context context) {
        return session.getDisplaySTime(context).getTime() - REMIND_DURATION_MINUTES_FOR_START;
    }
}
