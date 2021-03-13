package by.liauko.siarhei.app.today.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.util.NotificationUtil

/**
 * Foreground service showing notification with current day of the year
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearForegroundService : Service() {
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationUtil.createDayOfYearNotification(this)
        startForeground(ApplicationConstants.NOTIFICATION_ID, notification)

        return START_NOT_STICKY
    }
}
