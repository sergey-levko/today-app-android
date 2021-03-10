package by.liauko.siarhei.app.today.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.presenter.DayOfYearPresenter
import by.liauko.siarhei.app.today.service.ApplicationToolsStatusService

class DayOfYearAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        ApplicationToolsStatusService(context).updateWidgetStatus(true)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        ApplicationToolsStatusService(context).updateWidgetStatus(false)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val dayOfYearPresenter = DayOfYearPresenter()
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.day_of_year_app_widget)
    views.setTextViewText(R.id.appwidget_text, dayOfYearPresenter.loadCurrentDayOfYear().currentDay.toString())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
