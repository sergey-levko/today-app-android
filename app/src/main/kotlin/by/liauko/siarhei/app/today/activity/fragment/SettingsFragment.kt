package by.liauko.siarhei.app.today.activity.fragment

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.receiver.DayOfYearUpdateReceiver
import by.liauko.siarhei.app.today.service.ApplicationToolsStatusService
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
    private lateinit var statusBarSwitch: SwitchPreferenceCompat

    private lateinit var permissionRequestActivity: ActivityResultLauncher<String>
    private lateinit var androidSettingsActivity: ActivityResultLauncher<Intent>

    // getPackageInfo(String, int) is deprecated in Android 13
    @Suppress("DEPRECATION")
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

        statusBarSwitch = findPreference(statusBarSwitchKey)!!
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            statusBarSwitch.isChecked = false
            ApplicationToolsStatusService(requireContext()).updateNotificationStatus(
                false
            )
        }

        findPreference<SwitchPreferenceCompat>(totalDayOfYearKey)!!.onPreferenceChangeListener = preferenceChangeListener
        statusBarSwitch.onPreferenceChangeListener = preferenceChangeListener
        findPreference<Preference>("version")!!.summary =
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    requireContext().packageManager.getPackageInfo(
                        requireContext().packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    ).versionName
                }
                else -> {
                    requireContext().packageManager.getPackageInfo(
                        requireContext().packageName,
                        0
                    ).versionName
                }
            }

        initPermissionRequestActivity()
        initAndroidSettingsActivity()
    }

    private val preferenceChangeListener =
        Preference.OnPreferenceChangeListener { preference, newValue ->
            when (preference.key) {
                totalDayOfYearKey -> sharedPreferences.edit()
                    .putBoolean(totalDayOfYearKey, newValue as Boolean).apply()
                statusBarSwitchKey -> handleStatusBarSwitchChange(newValue as Boolean)
            }
            true
        }

    private fun initPermissionRequestActivity() {
        permissionRequestActivity = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                ApplicationToolsStatusService(requireContext()).updateNotificationStatus(
                    true
                )
            } else {
                statusBarSwitch.isChecked = false
            }
        }
    }

    private fun initAndroidSettingsActivity() {
        androidSettingsActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {
                ApplicationToolsStatusService(requireContext()).updateNotificationStatus(
                    true
                )
            } else {
                statusBarSwitch.isChecked = false
            }
        }
    }

    private fun handleStatusBarSwitchChange(newValue: Boolean) {
        if (newValue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    ApplicationToolsStatusService(requireContext()).updateNotificationStatus(
                        true
                    )
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
                    dialogBuilder.setTitle(R.string.notification_permission_dialog_title)
                        .setMessage(R.string.notification_permission_dialog_text)
                        .setPositiveButton(R.string.open) { dialog, _ ->
                            val settingsIntent =
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            settingsIntent.putExtra(
                                Settings.EXTRA_APP_PACKAGE,
                                requireContext().packageName
                            )
                            androidSettingsActivity.launch(settingsIntent)
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.cancel) { dialog, _ ->
                            statusBarSwitch.isChecked = false
                            dialog.cancel()
                        }
                        .create()
                        .show()
                }
                else -> {
                    permissionRequestActivity.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            ApplicationToolsStatusService(requireContext()).updateNotificationStatus(
                newValue
            )
        }
    }
}
