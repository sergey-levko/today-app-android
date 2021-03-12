package by.liauko.siarhei.app.today.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import by.liauko.siarhei.app.today.R
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {

    private lateinit var currentDateTextView: TextView
    private lateinit var dayTextView: TextView
    private lateinit var dayProgressBar: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            else
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        createNotificationChannel()

        sharedPreferences = applicationContext.getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)

        initElements()
        initData()
    }

    override fun onResume() {
        super.onResume()

        initData()
    }

    private fun initElements() {
        currentDateTextView = findViewById(R.id.current_date)
        dayTextView = findViewById(R.id.day_of_year_text)
        dayProgressBar = findViewById(R.id.progressBar)

        findViewById<ImageButton>(R.id.settings_button).setOnClickListener { startActivity(Intent(applicationContext, SettingsActivity::class.java)) }
    }

    private fun initData() {
        currentDateTextView.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))

        val showTotalDays = sharedPreferences.getBoolean(getString(R.string.total_days_key), false)

        val currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDayOfYear = GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR)
        val dayText = if (showTotalDays) {
            currentDay.toString() + getString(R.string.days_delimiter) + lastDayOfYear.toString()
        } else {
            currentDay.toString()
        }
        dayTextView.text = dayText
        dayTextView.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.clipboard_day_of_year_label), currentDay.toString()))
            Toast.makeText(
                applicationContext,
                R.string.day_copied_toast_message,
                Toast.LENGTH_SHORT
            ).show()
        }
        dayProgressBar.progress = currentDay
        dayProgressBar.max = lastDayOfYear
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "Channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
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
