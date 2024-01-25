package by.liauko.siarhei.app.today.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val currentDayOfYear = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR)
    private val lastDayOfYear = GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR)

    val uiState = HomeUiState(
        currentDayNumber = currentDayOfYear,
        daysLeftNumber = lastDayOfYear - currentDayOfYear,
        weekNumber = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR),
        currentDate = DateFormat.getDateInstance().format(Date(System.currentTimeMillis())),
        yearProgress = currentDayOfYear.toFloat() / lastDayOfYear
    )
}

data class HomeUiState(
    val currentDayNumber: Int,
    val daysLeftNumber: Int,
    val weekNumber: Int,
    val currentDate: String,
    val yearProgress: Float
)
