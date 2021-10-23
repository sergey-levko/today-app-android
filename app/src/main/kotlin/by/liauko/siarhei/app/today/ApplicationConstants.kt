package by.liauko.siarhei.app.today

/**
 * Class containing common application constants
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
object ApplicationConstants {

    /**
     * Unique identifier of notification showing current day of the year
     *
     * @since 1.0.0
     */
    const val NOTIFICATION_ID = 100

    /**
     * The maximum value of opacity for app widget background
     *
     * @since 1.0.0
     */
    const val OPACITY_MAX_VALUE = 255

    /**
     * Application package
     *
     * @since 1.1.4
     */
    const val APP_PACKAGE = "by.liauko.siarhei.app.today"

    /**
     * List of system actions for which notification update required
     *
     * @since 1.1.4
     */
    val notificationUpdateRequiredActions = listOf(
        "android.intent.action.BOOT_COMPLETED",
        "android.intent.action.MY_PACKAGE_REPLACED"
    )

    /**
     * List of old drawable IDs of widget forms.
     *
     * WILL REMOVE IN VERSION 1.0.4
     *
     * @since 1.1.4
     */
    val previousFormIds = listOf(2131230883, 2131230884, 2131230885)
}
