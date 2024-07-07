package com.utn.cookmate

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.utn.cookmate.ui.screens.CookMateNavigationGraph
import com.utn.cookmate.ui.theme.CookMateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContent {
            CookMateTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(ContextCompat.getColor(this@MainActivity, R.color.purple_700))
                    )
                }
                CookMateApp()
            }
        }
    }

    @Composable
    fun CookMateApp(){
        CookMateNavigationGraph()
    }
}

