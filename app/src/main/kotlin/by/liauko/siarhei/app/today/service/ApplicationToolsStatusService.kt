package by.liauko.siarhei.app.today.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.util.AlarmUtil

/**
 * Service class handling app notification and widget statuses
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class ApplicationToolsStatusService(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preferences_name),
        Context.MODE_PRIVATE
    )
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
        PendingIntent.getBroadcast(context, 0, it, 0)
    }

    /**
     * Managing displaying app notification and alarm work
     *
     * @param status boolean parameter determined if notification displaying or not
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun updateNotificationStatus(status: Boolean) {
        val widgetStatus = sharedPreferences.getBoolean(context.getString(R.string.widget_status_key), false)
        sharedPreferences.edit().putBoolean(context.getString(R.string.notification_status_key), status).apply()
        updateDeviceBootReceiverState(status)
        if (status) {
            ContextCompat.startForegroundService(context, Intent(context, DayOfYearForegroundService::class.java))
            if (!widgetStatus) {
                AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
            }
        } else {
            context.stopService(Intent(context, DayOfYearForegroundService::class.java))
            if (!widgetStatus) {
                alarmManager.cancel(alarmIntent)
            }
        }
    }

    /**
     * Managing app widgets and alarm work
     *
     * @param status boolean parameter determined if app widget displaying or not
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun updateWidgetStatus(status: Boolean) {
        val notificationStatus = sharedPreferences.getBoolean(context.getString(R.string.notification_status_key), false)
        sharedPreferences.edit().putBoolean(context.getString(R.string.widget_status_key), status).apply()
        updateDeviceBootReceiverState(status)
        if (!notificationStatus) {
            if (status) {
                AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
            } else {
                alarmManager.cancel(alarmIntent)
            }
        }
    }

    private fun updateDeviceBootReceiverState(enable: Boolean) {
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, DeviceBootReceiver::class.java),
            if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
