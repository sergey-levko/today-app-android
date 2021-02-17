package by.liauko.siarhei.app.today.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.presenter.DayOfYearPresenter
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var currentDateTextView: TextView
    private lateinit var dayTextView: TextView
    private lateinit var dayProgressBar: ProgressBar

    private lateinit var dayOfYearPresenter: DayOfYearPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            else
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        dayOfYearPresenter = DayOfYearPresenter()

        initElements()
        initData()
    }

    private fun initElements() {
        currentDateTextView = findViewById(R.id.current_date)
        dayTextView = findViewById(R.id.day_of_year_text)
        dayProgressBar = findViewById(R.id.progressBar)
    }

    private fun initData() {
        currentDateTextView.text = DateFormat.getDateInstance().format(Date(System.currentTimeMillis()))

        val dayOfYearData = dayOfYearPresenter.loadCurrentDayOfYear()
        dayTextView.text = dayOfYearData.currentDay.toString()
        dayProgressBar.progress = dayOfYearData.currentDay
        dayProgressBar.max = dayOfYearData.lastDayOfYear
    }
}
