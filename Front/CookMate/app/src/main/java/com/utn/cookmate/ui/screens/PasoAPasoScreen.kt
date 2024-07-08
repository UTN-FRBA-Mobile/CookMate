package com.utn.cookmate.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.utn.cookmate.R
import com.utn.cookmate.NotificationService
import com.utn.cookmate.service.CronometroService
import com.utn.cookmate.ui.NormalBar
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PasoAPasoScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    val appStatus by userInputViewModel.appStatus.observeAsState()
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Manejar el caso en que el permiso no fue concedido
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.FOREGROUND_SERVICE)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        val state = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .verticalScroll(state)
        ) {
            appStatus?.let { status ->
                val pasoActual = status.pasoActual.value ?: 1
                val recetaElegida = status.recetaElegida

                recetaElegida?.let { receta ->
                    TopBar(value = receta.nombre)
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                            enabled = pasoActual != 1,
                            onClick = {
                                userInputViewModel.appStatus.value?.pasoActual?.value = pasoActual - 1
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso anterior",
                                textSize = 18.sp,
                                colorValue = if (pasoActual != 1) Color.White else Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                            enabled = pasoActual != receta.listaPasos.size,
                            onClick = {
                                userInputViewModel.appStatus?.value?.pasoActual?.value = pasoActual + 1
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso siguiente",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                    }

                    val paso = receta.listaPasos.getOrNull(pasoActual - 1)

                    paso?.let {
                        Spacer(modifier = Modifier.size(20.dp))
                        NormalBar("Paso $pasoActual")
                        Spacer(modifier = Modifier.size(15.dp))
                        TextComponent(it.descripcion, textSize = 20.sp)
                        Spacer(modifier = Modifier.size(20.dp))

                        it.imagen.let { imagen ->
                            userInputViewModel.appStatus.value?.imagenesDescargadas?.get(imagen)?.let { imageData ->
                                Image(
                                    bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap(),
                                    contentDescription = "Descripción de contenido"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                        NormalBar("Ingredientes requeridos")
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            if (it.ingredientes.isNotEmpty()) {
                                it.ingredientes.forEach { ingrediente ->
                                    ingrediente.imagen.let { imagen ->
                                        NormalBar(ingrediente.nombre + " (" + ingrediente.cantidad + ")", userInputViewModel.appStatus?.value?.imagenesDescargadas?.get(imagen))
                                        Spacer(modifier = Modifier.size(20.dp))
                                    }
                                }
                            } else {
                                NormalBar("Ninguno para este paso!")
                                Spacer(modifier = Modifier.size(20.dp))
                            }
                        }

                        val postNotificationPermission= rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                        val notificationService= NotificationService(context)
                        LaunchedEffect(key1 = true ){
                            if(!postNotificationPermission.status.isGranted){
                                postNotificationPermission.launchPermissionRequest()
                            }
                        }

                        // Temporizador
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            it.duracion?.let { duracion ->
                                Temporizador(
                                    duracion = duracion,
                                    onTimerFinished = {
                                        userInputViewModel.appStatus.value?.pasoActual?.value = pasoActual + 1
                                    },
                                    context = LocalContext.current
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                                onClick = {
                                    userInputViewModel.appStatus.value?.pasoActual?.value = 1
                                    navController.navigate(Routes.MIS_RECETAS_SCREEN)
                                }
                            ) {
                                TextComponent(
                                    textValue = "Volver",
                                    textSize = 18.sp,
                                    colorValue = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Temporizador(
    duracion: Int,
    onTimerFinished: () -> Unit,
    context: Context
) {
    var timerSeconds by remember { mutableStateOf(duracion * 60) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val serviceIntent = remember { Intent(context, CronometroService::class.java) }

    LaunchedEffect(duracion) {
        if (isTimerRunning) {
            context.stopService(serviceIntent)
        }
        timerSeconds = duracion * 60
        isTimerRunning = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                isTimerRunning = !isTimerRunning
                if (isTimerRunning) {
                    serviceIntent.action = "START"
                    serviceIntent.putExtra("duration", timerSeconds)
                    ContextCompat.startForegroundService(context, serviceIntent)
                } else {
                    serviceIntent.action = "PAUSE"
                    context.startService(serviceIntent)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isTimerRunning) "Pausar" else "Iniciar paso",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format("%02d:%02d", timerSeconds / 60, timerSeconds % 60),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LaunchedEffect(isTimerRunning) {
            while (isTimerRunning && timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
            if (timerSeconds == 0) {
                isTimerRunning = false
                onTimerFinished()
                context.stopService(serviceIntent)
            }
        }
    }
}
