package by.liauko.siarhei.app.today.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.RemoteViews
import android.widget.Toast
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.service.ApplicationToolsStatusService
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Class managing app widget with size 1x1
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
open class DayOfYearWidget : AppWidgetProvider() {

    /**
     * Updates each active widget.
     *
     * @param context application context
     * @param appWidgetManager an [AppWidgetManager] object
     * @param appWidgetIds the application widget identifiers for which an update is needed
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Called when widget is removed.
     *
     * @param context application context
     * @param appWidgetIds the application widget identifiers which was removed
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            context.getSharedPreferences(context.getString(R.string.widget_shared_preferences_name), Context.MODE_PRIVATE)
                .edit()
                .remove("${appWidgetId}_form_name")
                .remove("${appWidgetId}_background_color")
                .remove("${appWidgetId}_opacity")
                .remove("${appWidgetId}_text_color")
                .apply()
        }
    }

    /**
     * Called when the first widget is created.
     *
     * @param context application context
     */
    override fun onEnabled(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
        ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
        if (ids.size > 1) {
            ApplicationToolsStatusService(context).updateWidgetStatus(true)
        }
    }

    /**
     * Called when the last widget with particular class is disabled. Need to check if widgets with
     * other class still exist.
     *
     * @param context application context
     */
    override fun onDisabled(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
        ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
        if (ids.isEmpty()) {
            ApplicationToolsStatusService(context).updateWidgetStatus(false)
        }
    }

    /**
     * Copy current day of the year to clipboard and show toast with message.
     *
     * @param context application context
     * @param intent intent being received
     */
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

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
    val textSize = if (appWidgetId in ids) 20f else 40f

    val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.widget_shared_preferences_name),
        Context.MODE_PRIVATE
    )

    val isDefaultTheme = sharedPreferences.getBoolean("${appWidgetId}_is_default", true)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_day_of_year)

    views.setImageViewResource(
        R.id.widget_background,
        context.resources.getIdentifier(
            sharedPreferences.getString(
                "${appWidgetId}_form_name",
                context.resources.getResourceEntryName(R.drawable.widget_background_circle)
            ),
            "drawable",
            ApplicationConstants.APP_PACKAGE
        )
    )
    views.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, textSize)
    views.setTextViewText(
        R.id.widget_text,
        GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
    )

    if (!isDefaultTheme) {
        views.setInt(
            R.id.widget_background,
            "setColorFilter",
            sharedPreferences.getInt(
                "${appWidgetId}_background_color",
                context.getColor(R.color.widgetBackground)
            )
        )
        views.setInt(
            R.id.widget_background,
            "setImageAlpha",
            sharedPreferences.getInt(
                "${appWidgetId}_opacity",
                ApplicationConstants.OPACITY_MAX_VALUE
            )
        )
        views.setTextColor(
            R.id.widget_text,
            sharedPreferences.getInt("${appWidgetId}_text_color", context.getColor(R.color.primary))
        )
    }

    val intent = Intent(context, DayOfYearWidget::class.java)
    intent.action = COPY_ACTION
    views.setOnClickPendingIntent(R.id.widget_text, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
