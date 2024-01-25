package by.liauko.siarhei.app.today.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AppBlue,
    onPrimary = AppBlack,
    primaryContainer = AppLightBlue,
    onPrimaryContainer = AppBlue,
    secondary = AppBlue,
    onSecondary = AppWhite,
    secondaryContainer = AppBlue,
    onSecondaryContainer = OnSecondaryContainer,
    background = AppWhite,
    onBackground = AppBlack,
    surface = AppWhite,
    onSurface = AppBlack,
    error = AppRed,
    onError = AppWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = AppLightBlue,
    onPrimary = AppWhite,
    primaryContainer = AppBlue,
    onPrimaryContainer = AppLightBlue,
    secondary = AppLightBlue,
    onSecondary = AppBlack,
    secondaryContainer = AppLightBlue,
    onSecondaryContainer = AppBlue,
    background = AppDark,
    onBackground = AppWhite,
    surface = AppDark,
    onSurface = AppWhite,
    error = AppRed,
    onError = AppWhite
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            window.statusBarColor = AppWhite.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
