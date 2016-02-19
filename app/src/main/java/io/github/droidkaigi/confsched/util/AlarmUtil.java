package io.github.droidkaigi.confsched.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.receiver.SessionFinishReceiver;
import io.github.droidkaigi.confsched.receiver.SessionScheduleReceiver;

public class AlarmUtil {

    private static final long REMIND_DURATION_MINUTES_FOR_START = TimeUnit.MINUTES.toMillis(5);
    private static final long REMIND_DURATION_MINUTES_FOR_FINISH = TimeUnit.MINUTES.toMillis(5);

    public static void handleSessionAlarm(Context context, Session session) {
        if (session.checked) {
            registerSessionAlarm(context, session);
        } else {
            unregisterSessionAlarm(context, session);
        }
    }

    private static void registerSessionAlarm(Context context, Session session) {


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (session.shouldRegisterNotifyStart(REMIND_DURATION_MINUTES_FOR_START)) {
            long triggerAtMillisForStart = calculateStartNotifyTime(session, context);
            PendingIntent senderForStart = buildSessionStartAlarmSender(context, session);
            alarmManager.set(AlarmManager.RTC, triggerAtMillisForStart, senderForStart);
        }

        if (session.shouldRegisterNotifyFinish(REMIND_DURATION_MINUTES_FOR_FINISH)) {
            long triggerAtMillisForFinish = calculateEndNotifyTime(session, context);
            PendingIntent senderForFinish = buildSessionFinishAlarmSender(context, session);
            alarmManager.set(AlarmManager.RTC, triggerAtMillisForFinish, senderForFinish);
        }
    }

    private static void unregisterSessionAlarm(Context context, Session session) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent senderForStart = buildSessionStartAlarmSender(context, session);
        alarmManager.cancel(senderForStart);

        PendingIntent senderForFinish = buildSessionFinishAlarmSender(context, session);
        alarmManager.cancel(senderForFinish);
    }

    private static PendingIntent buildSessionStartAlarmSender(Context context, Session session) {
        Intent intent = new Intent(context, SessionScheduleReceiver.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        return PendingIntent.getBroadcast(context, session.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent buildSessionFinishAlarmSender(Context context, Session session) {
        Intent intent = new Intent(context, SessionFinishReceiver.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        int requestCode = -session.id; // use negated id to avoid conflict with start and finish
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static long calculateStartNotifyTime(Session session, Context context) {
        return session.getDisplaySTime(context).getTime() - REMIND_DURATION_MINUTES_FOR_START;
    }

    private static long calculateEndNotifyTime(Session session, Context context) {
        return session.getDisplayETime(context).getTime() + REMIND_DURATION_MINUTES_FOR_FINISH;
    }
}
