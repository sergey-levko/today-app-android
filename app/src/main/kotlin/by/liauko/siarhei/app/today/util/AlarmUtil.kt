package by.liauko.siarhei.app.today.util

import android.app.AlarmManager
import android.app.PendingIntent
import java.util.Calendar

object AlarmUtil {

    fun setAlarm(alarmManager: AlarmManager, alarmIntent: PendingIntent) {
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
