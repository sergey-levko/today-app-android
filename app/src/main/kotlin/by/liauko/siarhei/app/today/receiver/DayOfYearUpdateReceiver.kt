package by.liauko.siarhei.app.today.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.util.AlarmUtil
import by.liauko.siarhei.app.today.util.NotificationUtil
import by.liauko.siarhei.app.today.widget.DayOfYearBigWidget
import by.liauko.siarhei.app.today.widget.DayOfYearWidget

/**
 * Class handling updating current day of the year for app notification and widgets
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
        val notificationStatus = sharedPreferences.getBoolean(context.getString(R.string.notification_status_key), false)
        val widgetStatus = sharedPreferences.getBoolean(context.getString(R.string.widget_status_key), false)

        if (notificationStatus) {
            val notification = NotificationUtil.createDayOfYearNotification(context)
            NotificationManagerCompat.from(context)
                .notify(ApplicationConstants.NOTIFICATION_ID, notification)
        }

        if (widgetStatus) {
            val widgetManager = AppWidgetManager.getInstance(context)
            var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
            ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(updateIntent)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
            PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
    }
}
