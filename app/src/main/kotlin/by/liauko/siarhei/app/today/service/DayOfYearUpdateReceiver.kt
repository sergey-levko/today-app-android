package by.liauko.siarhei.app.today.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.util.NotificationUtil

class DayOfYearUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationUtil.createDayOfYearNotification(context)
        NotificationManagerCompat.from(context).notify(ApplicationConstants.NOTIFICATION_ID, notification)
    }
}
