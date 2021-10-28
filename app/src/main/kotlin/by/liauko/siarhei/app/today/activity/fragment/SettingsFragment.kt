package by.liauko.siarhei.app.today.activity.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.service.ApplicationToolsStatusService
import by.liauko.siarhei.app.today.receiver.DayOfYearUpdateReceiver

/**
 * Class managing application settings items
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
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
            PendingIntent.getBroadcast(requireContext(), 0, it, PendingIntent.FLAG_IMMUTABLE)
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
            statusBarSwitchKey -> ApplicationToolsStatusService(requireContext()).updateNotificationStatus(newValue as Boolean)
        }
        true
    }
}
