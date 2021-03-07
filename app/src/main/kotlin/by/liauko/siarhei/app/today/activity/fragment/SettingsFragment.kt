package by.liauko.siarhei.app.today.activity.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.service.DayOfYearForegroundService
import by.liauko.siarhei.app.today.service.DayOfYearUpdateReceiver
import by.liauko.siarhei.app.today.service.DeviceBootReceiver
import by.liauko.siarhei.app.today.util.AlarmUtil

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var totalDayOfYearKey: String
    private lateinit var statusBarSwitchKey: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)

        sharedPreferences = requireContext().getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(requireContext(), DayOfYearUpdateReceiver::class.java).let {
            PendingIntent.getBroadcast(requireContext(), 0, it, 0)
        }

        totalDayOfYearKey = getString(R.string.total_days_key)
        statusBarSwitchKey = getString(R.string.status_bar_key)
        notificationManager = NotificationManagerCompat.from(requireContext())

        findPreference<SwitchPreferenceCompat>(totalDayOfYearKey)!!.onPreferenceChangeListener = preferenceChangeListener
        findPreference<SwitchPreferenceCompat>(statusBarSwitchKey)!!.onPreferenceChangeListener = preferenceChangeListener
        findPreference<Preference>("version")!!.summary = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName
    }

    private val preferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        when (preference.key) {
            totalDayOfYearKey -> sharedPreferences.edit().putBoolean(totalDayOfYearKey, newValue as Boolean).apply()
            statusBarSwitchKey -> {
                updateDeviceBootReceiverState(newValue as Boolean)
                if (newValue) {
                    ContextCompat.startForegroundService(requireContext(), Intent(requireContext(), DayOfYearForegroundService::class.java))
                    AlarmUtil.setAlarm(alarmManager, alarmIntent)
                } else {
                    requireContext().stopService(Intent(requireContext(), DayOfYearForegroundService::class.java))
                    alarmManager.cancel(alarmIntent)
                }
            }
        }
        true
    }

    private fun updateDeviceBootReceiverState(enable: Boolean) {
        requireContext().packageManager.setComponentEnabledSetting(
                ComponentName(requireContext(), DeviceBootReceiver::class.java),
                if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )
    }
}
