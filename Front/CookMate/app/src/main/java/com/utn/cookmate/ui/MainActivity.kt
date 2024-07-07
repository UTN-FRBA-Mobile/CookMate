package com.utn.cookmate

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.utn.cookmate.ui.screens.CookMateNavigationGraph
import com.utn.cookmate.ui.theme.CookMateTheme

class MainActivity : ComponentActivity() {

    private val PERMISSION_REQUEST_CODE = 1001 // Código para la solicitud de permisos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        requestPermissionsIfNeeded()

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



    private fun requestPermissionsIfNeeded() {
        val requiredPermission = android.Manifest.permission.FOREGROUND_SERVICE

        if (ContextCompat.checkSelfPermission(this, requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(requiredPermission), PERMISSION_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido,
            }
        }
    }

    @Composable
    fun CookMateApp() {
        CookMateNavigationGraph()
    }
}
