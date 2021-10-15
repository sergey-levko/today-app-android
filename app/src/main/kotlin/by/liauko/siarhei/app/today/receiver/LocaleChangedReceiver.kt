package by.liauko.siarhei.app.today.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.service.DayOfYearForegroundService

class LocaleChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.LOCALE_CHANGED") {
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
        }
    }
}
