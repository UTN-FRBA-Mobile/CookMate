package com.utn.cookmate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.Paso
import com.utn.cookmate.ui.NormalBar
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.livedata.observeAsState
import com.utn.cookmate.R

@Composable
fun PasoAPasoScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    val context = LocalContext.current

    // Crear canal de notificación para API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Cookmate Timer"
        val descriptionText = "Notifications for Cookmate timers"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("cookmate_timer_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberScrollState()
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(state)
        ) {
            // Observar el estado del ViewModel
            val appStatus = userInputViewModel.appStatus.observeAsState()

            appStatus?.value?.let { status ->
                val pasoActual = status.pasoActual?.value ?: 1
                val recetaElegida = status.recetaElegida

                recetaElegida?.let { receta ->
                    TopBar(value = receta.nombre)
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            enabled = pasoActual != 1,
                            onClick = {
                                userInputViewModel.appStatus?.value?.pasoActual?.value = pasoActual - 1
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso anterior",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                        Button(
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

                        it.imagen?.let { imagen ->
                            userInputViewModel.appStatus?.value?.imagenesDescargadas?.get(imagen)?.let { imageData ->
                                Image(
                                    bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap(),
                                    contentDescription = "Descripción de contenido"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                        NormalBar("Ingredientes requeridos")
                        Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Column {
                                if (it.ingredientes.isNotEmpty()) {
                                    it.ingredientes.forEach { ingrediente ->
                                        ingrediente.imagen?.let { imagen ->
                                            NormalBar(ingrediente.nombre + " (" + ingrediente.cantidad + ")", userInputViewModel.appStatus?.value?.imagenesDescargadas?.get(imagen))
                                            Spacer(modifier = Modifier.size(50.dp))
                                        }
                                    }
                                } else {
                                    NormalBar("Ninguno para este paso!")
                                    Spacer(modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        // Temporizador
                        it.duracion?.let { duracion ->
                            val timerSeconds = remember { mutableStateOf(duracion.times(60)) }
                            var isTimerRunning by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { isTimerRunning = !isTimerRunning }
                                ) {
                                    TextComponent(
                                        textValue = if (isTimerRunning) "Pausar" else "Iniciar temporizador",
                                        textSize = 18.sp,
                                        colorValue = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.size(20.dp))
                                TextComponent(
                                    textValue = String.format("%02d:%02d", timerSeconds?.value),
                                    textSize = 18.sp
                                )
                            }

                            LaunchedEffect(isTimerRunning) {
                                if (isTimerRunning && timerSeconds?.value!! > 0) {
                                    delay(1000L)
                                    timerSeconds?.value = timerSeconds?.value!! - 1

                                    if (timerSeconds?.value == 0) {
                                        isTimerRunning = false

                                        // Enviar notificación push
                                        val builder = NotificationCompat.Builder(context, "cookmate_timer_channel")
                                            .setSmallIcon(R.drawable.ic_timer)
                                            .setContentTitle("¡Tiempo cumplido!")
                                            .setContentText("El paso $pasoActual ha terminado.")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)

                                        with(NotificationManagerCompat.from(context)) {
                                            notify(0, builder.build())
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    userInputViewModel.appStatus?.value?.pasoActual?.value = 1
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
