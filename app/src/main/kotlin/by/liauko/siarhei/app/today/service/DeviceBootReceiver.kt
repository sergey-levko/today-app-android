package by.liauko.siarhei.app.today.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import by.liauko.siarhei.app.today.util.AlarmUtil

class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            ContextCompat.startForegroundService(context, Intent(context, DayOfYearForegroundService::class.java))
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
                PendingIntent.getBroadcast(context, 0, it, 0)
            }
            AlarmUtil.setAlarm(alarmManager, alarmIntent)
        }
    }
}
