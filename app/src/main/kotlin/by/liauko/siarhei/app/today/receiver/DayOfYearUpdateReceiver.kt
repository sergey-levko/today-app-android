package by.liauko.siarhei.app.today.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import by.liauko.siarhei.app.today.util.AlarmUtil
import by.liauko.siarhei.app.today.util.NotificationUtil
import by.liauko.siarhei.app.today.util.WidgetUtil

/**
 * Class handling updating current day of the year for app notification and widgets
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.updateNotification(context)
        WidgetUtil.updateWidgets(context)
        AlarmUtil.setMidnightAlarm(context)
    }
}
