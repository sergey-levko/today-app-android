package by.liauko.siarhei.app.today.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import by.liauko.siarhei.app.today.util.NotificationUtil

/**
 * Class handling case when device locale has been changed
 *
 * @author Siarhei Liauko
 * @since 1.1.4
 */
class LocaleChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.LOCALE_CHANGED") {
            NotificationUtil.updateNotification(context)
        }
    }
}
