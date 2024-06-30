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

                // Use SideEffect to apply changes to the system UI (status bar color)
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(ContextCompat.getColor(this@MainActivity, R.color.purple_700))
                    )
                }
                CookMateApp()
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
            }
        }
    }

    @Composable
    fun CookMateApp(){
        CookMateNavigationGraph()
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CookMateTheme {
//        Greeting("Android")
//    }
//}