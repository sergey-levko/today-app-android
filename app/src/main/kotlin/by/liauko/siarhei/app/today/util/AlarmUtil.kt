package by.liauko.siarhei.app.today.util

import android.app.AlarmManager
import android.app.PendingIntent
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
}
