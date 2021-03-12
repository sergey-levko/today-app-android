package by.liauko.siarhei.app.today.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.RemoteViews
import android.widget.Toast
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.model.WidgetParameters
import by.liauko.siarhei.app.today.service.ApplicationToolsStatusService
import java.util.Calendar
import java.util.GregorianCalendar

open class DayOfYearWidget : AppWidgetProvider() {

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

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        ApplicationToolsStatusService(context).updateWidgetStatus(true)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        ApplicationToolsStatusService(context).updateWidgetStatus(false)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (COPY_ACTION == intent.action) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    context.getString(R.string.clipboard_day_of_year_label),
                    GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
                )
            )
            Toast.makeText(
                context,
                R.string.day_copied_toast_message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

const val COPY_ACTION = "COPY"

internal fun createAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    parameters: WidgetParameters
) {
    val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val width = options.get(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) as Int
    val textSize = if (width < 110) 20f else 40f

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_day_of_year)
    views.setImageViewResource(R.id.widget_background, parameters.form)
    views.setInt(R.id.widget_background, "setColorFilter", parameters.backgroundColor)
    views.setInt(R.id.widget_background, "setImageAlpha", parameters.opacity)
    views.setTextViewText(R.id.widget_text, GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString())
    views.setTextColor(R.id.widget_text, parameters.textColor)
    views.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, textSize)

    val intent = Intent(context, DayOfYearWidget::class.java)
    intent.action = COPY_ACTION
    views.setOnClickPendingIntent(R.id.widget_text, PendingIntent.getBroadcast(context, 0, intent, 0))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_day_of_year)
    views.setTextViewText(R.id.widget_text, GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
