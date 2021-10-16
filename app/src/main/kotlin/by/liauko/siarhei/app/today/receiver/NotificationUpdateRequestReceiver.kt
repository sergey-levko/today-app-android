package by.liauko.siarhei.app.today.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.util.AlarmUtil
import by.liauko.siarhei.app.today.util.NotificationUtil

/**
 * Class handling case when device was rebooted or application updated
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class NotificationUpdateRequestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ApplicationConstants.notificationUpdateRequiredActions.contains(intent.action)) {
            NotificationUtil.updateNotification(context)
            AlarmUtil.setMidnightAlarm(context)
        }
    }
}
