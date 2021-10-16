package by.liauko.siarhei.app.today.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
        NotificationUtil.updateNotification(context)

        val widgetStatus = context.getSharedPreferences(
            context.getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        ).getBoolean(context.getString(R.string.widget_status_key), false)

        if (widgetStatus) {
            val widgetManager = AppWidgetManager.getInstance(context)
            var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
            ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(updateIntent)
        }

        AlarmUtil.setMidnightAlarm(context)
    }
}
