package by.liauko.siarhei.app.today.util

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import by.liauko.siarhei.app.today.R
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Utility class containing methods managing application notification
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
object NotificationUtil {

    /**
     * Creates notification which shows current day of the year
     *
     * @param context application context
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun createDayOfYearNotification(context: Context): Notification {
        val currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val bitmap = createBitmapFromText(currentDay.toString())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val builder = Notification.Builder(context, context.getString(R.string.notification_channel_id))
                            .setSmallIcon(Icon.createWithBitmap(bitmap))
                            .setContentTitle("""${context.getString(R.string.today_is_notification_text)} 
                                |${currentDay} 
                                |${context.getString(R.string.day_of_year_notification_text)}""".trimMargin())
                    builder.build()

                } else {
                    val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                            .setSmallIcon(R.drawable.ic_action_name)
                            .setContentTitle("""${context.getString(R.string.today_is_notification_text)} 
                                |${currentDay} 
                                |${context.getString(R.string.day_of_year_notification_text)}""".trimMargin())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                    builder.build()
                }
    }

    private fun createBitmapFromText(text: String): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = 100f
        paint.flags = Paint.FAKE_BOLD_TEXT_FLAG
        paint.textAlign = Paint.Align.CENTER

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val bitmap = Bitmap.createBitmap(textBounds.width() + 10, 90, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawText(text, textBounds.width() / 2f + 5, 85f, paint)

        return bitmap
    }
}
