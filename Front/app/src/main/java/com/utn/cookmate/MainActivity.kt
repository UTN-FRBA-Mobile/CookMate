package com.utn.cookmate

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.utn.cookmate.connection.NetworkChecker
import com.utn.cookmate.ui.screens.CookMateNavigationGraph
import com.utn.cookmate.ui.theme.CookMateTheme

class MainActivity : ComponentActivity() {
    private val networkChecker by lazy {
        NetworkChecker(getSystemService(ConnectivityManager::class.java))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CookMateTheme {
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