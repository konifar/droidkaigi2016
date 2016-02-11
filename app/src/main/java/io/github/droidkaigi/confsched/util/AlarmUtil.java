package io.github.droidkaigi.confsched.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.receiver.SessionScheduleReceiver;

public class AlarmUtil {
    public static void registerSessionAlarm(Context context, Session session){
        PendingIntent sender = buildSessionAlarmSender(context, session);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAtMillis = calculateStartNotifyTime(session);
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

    private static long calculateStartNotifyTime(Session session) {
        // DEBUG
        //return 0;

        // 5minutes early
        return session.stime.getTime() - 300000;
    }
}
