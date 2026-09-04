package io.github.chkrb.pqcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.chkrb.pqcompanion.ui.NavManager
import io.github.chkrb.pqcompanion.ui.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent { ThemeManager { NavManager() } }
    }
}
