package by.liauko.siarhei.app.today.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import by.liauko.siarhei.app.today.receiver.DayOfYearUpdateReceiver
import java.util.Calendar

/**
 * Utility class containing methods managing application alarms
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
object AlarmUtil {

    /**
     * Setting up alarm which will execute at midnight
     *
     * @param alarmManager [AlarmManager] instance to configure alarm
     * @param alarmIntent action to perform when the alarm goes off
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun setMidnightAlarm(alarmManager: AlarmManager, alarmIntent: PendingIntent) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    /**
     * Setting up alarm which will execute at midnight
     *
     * @param context application context
     *
     * @author Siarhei Liauko
     * @since 1.0.3
     */
    fun setMidnightAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
            PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        setMidnightAlarm(alarmManager, alarmIntent)
    }
}
