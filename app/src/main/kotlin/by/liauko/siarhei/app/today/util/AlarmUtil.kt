package by.liauko.siarhei.app.today.util

import android.app.AlarmManager
import android.app.PendingIntent
import java.util.Calendar

object AlarmUtil {

    fun setAlarm(alarmManager: AlarmManager, alarmIntent: PendingIntent) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        )
    }
}
