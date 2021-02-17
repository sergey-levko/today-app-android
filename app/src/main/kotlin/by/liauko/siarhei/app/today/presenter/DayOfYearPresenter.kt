package by.liauko.siarhei.app.today.presenter

import by.liauko.siarhei.app.today.model.DayOfYearModel
import java.util.*

class DayOfYearPresenter {

    fun loadCurrentDayOfYear(): DayOfYearModel {
        val calendar = GregorianCalendar.getInstance()

        return DayOfYearModel(
                calendar.get(Calendar.DAY_OF_YEAR),
                calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
        )
    }
}
