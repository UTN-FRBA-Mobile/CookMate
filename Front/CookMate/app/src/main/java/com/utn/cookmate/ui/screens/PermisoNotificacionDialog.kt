package com.utn.cookmate.ui.screens

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionRequestDialog(
    permission: String,
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        }
    }

    AlertDialog(
        onDismissRequest = { /* Handle case where user dismisses dialog */ },
        title = { Text("Permisos necesarios") },
        text = { Text("La aplicación necesita permisos para ejecutar esta función.") },
        confirmButton = {
            Button(
                onClick = {
                    permissionLauncher.launch(permission)
                }
            ) {
                Text("Conceder permisos")
            }
        }
    )
}
