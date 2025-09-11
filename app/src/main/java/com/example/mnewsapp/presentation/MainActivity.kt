package com.example.mnewsapp.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mnewsapp.presentation.navigation.NavigationSetup
import com.example.mnewsapp.ui.theme.MNewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isDarkTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        installSplashScreen()
        enableEdgeToEdge(

            navigationBarStyle = if (isDarkTheme) {
                SystemBarStyle.dark(
                    scrim = "#181216".toColorInt(),
                )
            } else {
                SystemBarStyle.light(
                    scrim = "#FFF7F9".toColorInt(),
                    darkScrim = "#181216".toColorInt()
                )
            }

        )
        setContent {
            MNewsAppTheme {

                val windowSizeClass = calculateWindowSizeClass(activity = this)

                NavigationSetup(windowSize = windowSizeClass.widthSizeClass)

            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MNewsAppTheme {
       // NavigationSetup()
    }
}