package by.liauko.siarhei.app.today.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.util.AlarmUtil

/**
 * Class handling case when device was rebooted
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val notificationStatus = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE
            ).getBoolean(context.getString(R.string.notification_status_key), false)

            if (notificationStatus) {
                ContextCompat.startForegroundService(
                    context,
                    Intent(context, DayOfYearForegroundService::class.java)
                )
            }
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
                PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
            }
            AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
        }
    }
}
