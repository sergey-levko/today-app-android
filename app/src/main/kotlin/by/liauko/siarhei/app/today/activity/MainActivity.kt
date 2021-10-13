package by.liauko.siarhei.app.today.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

/**
 * Class managing items of main application screen
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        AppCompatDelegate.setDefaultNightMode(
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            else
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        createNotificationChannel()

        sharedPreferences = applicationContext.getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)

        viewBinding.settingsFab.setOnClickListener { startActivity(
            Intent(applicationContext, SettingsActivity::class.java)
        ) }

        initData()
    }

    override fun onResume() {
        super.onResume()

        initData()
    }

    private fun initData() {
        viewBinding.currentDate.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))

        val showTotalDays = sharedPreferences.getBoolean(getString(R.string.total_days_key), false)

        val currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDayOfYear = GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR)
        if (showTotalDays) {
            viewBinding.dayOfYearText.text = getString(R.string.total_days_format, currentDay, lastDayOfYear)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                viewBinding.dayOfYearText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38f)
            }
        } else {
            viewBinding.dayOfYearText.text = currentDay.toString()
            viewBinding.dayOfYearText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48f)
        }
        viewBinding.dayOfYearText.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.clipboard_day_of_year_label), currentDay.toString()))
            Toast.makeText(
                applicationContext,
                R.string.day_copied_toast_message,
                Toast.LENGTH_SHORT
            ).show()
        }
        viewBinding.progressBar.progress = currentDay
        viewBinding.progressBar.max = lastDayOfYear
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "Channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(getString(R.string.notification_channel_id), name, importance).apply {
                description = descriptionText
            }
            channel.setSound(null, null)
            channel.enableLights(false)
            channel.enableVibration(false)
            channel.setShowBadge(false)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
