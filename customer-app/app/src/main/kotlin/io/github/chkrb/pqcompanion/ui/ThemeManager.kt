package io.github.chkrb.pqcompanion.ui

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ThemeManager(content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val darkMode = isSystemInDarkTheme()

    val window = LocalActivity.current!!.window
    window.isNavigationBarContrastEnforced = false

    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.isAppearanceLightStatusBars = !darkMode
    windowInsetsController.isAppearanceLightNavigationBars = !darkMode

    MaterialTheme(
        colorScheme = if (darkMode) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx),
        typography = Typography(),
        content = content,
    )
}
