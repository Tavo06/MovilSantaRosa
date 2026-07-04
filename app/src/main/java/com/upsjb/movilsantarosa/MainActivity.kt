package com.upsjb.movilsantarosa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.upsjb.movilsantarosa.core.navigation.AppNavHost
import com.upsjb.movilsantarosa.core.theme.MovilSantaRosaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovilSantaRosaTheme {
                AppNavHost()
            }
        }
    }
}

